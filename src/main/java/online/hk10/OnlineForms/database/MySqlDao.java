package online.hk10.OnlineForms.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import online.hk10.OnlineForms.database.entities.Field;
import online.hk10.OnlineForms.database.entities.FieldType;
import online.hk10.OnlineForms.database.entities.Form;
import online.hk10.OnlineForms.database.entities.FormResponse;
import online.hk10.OnlineForms.database.entities.User;

public class MySqlDao implements FormsDAO {	
	
	private static final String databaseName = "onlineforms";
	private static final String username = "root"; // change this later
	private static final String password = "root"; 
	private Logger logger;
	
	@Autowired
	private PasswordEncoder encoder; 
	
	private Connection connection;
	private PreparedStatement selectFormByFormId;
	private PreparedStatement selectFieldByFormId;
	private PreparedStatement selectFieldValueByField;
	private PreparedStatement selectResponseByFormId;
	private PreparedStatement selectAnswerMapByResponse;
	private PreparedStatement selectUserByUsername;
	private PreparedStatement selectFormByOwnerUsername;
	
	private PreparedStatement insertResponse;
	private PreparedStatement insertAnswer;
	private PreparedStatement insertForm;
	private PreparedStatement insertField;
	private PreparedStatement insertFieldValue;
	private PreparedStatement insertUser;
	
	
	public MySqlDao() throws SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://" + username + ":" + password + "@localhost:3306/" + databaseName);
		prepareStatements();
		logger = LoggerFactory.getLogger(getClass());
		logger.debug("Succesfully created MySQL Connection");
	}
	
	private void prepareStatements() throws SQLException {
		// see (*)selectFormByFormId = connection.prepareStatement("SELECT BIN_TO_UUID(form_id, true), form_name, form_description, owner_username FROM form WHERE form_id = UUID_TO_BIN(?)"); // ?
		selectFormByFormId = connection.prepareStatement("SELECT BIN_TO_UUID(form_id), form_name, form_description, owner_username FROM form WHERE form_id = UUID_TO_BIN(?)"); // ?
		selectFieldByFormId = connection.prepareStatement("SELECT * FROM field WHERE form_id = UUID_TO_BIN(?) ORDER BY field_order");
		selectFieldValueByField = connection.prepareStatement("SELECT * FROM field_value WHERE form_id = UUID_TO_BIN(?) AND title = ? ORDER BY val_order");
		selectResponseByFormId = connection.prepareStatement("SELECT * FROM response WHERE form_id = UUID_TO_BIN(?)");
		selectAnswerMapByResponse = connection.prepareStatement("SELECT * FROM answermap WHERE form_id = UUID_TO_BIN(?) AND response_id = ?");
		selectUserByUsername = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
		selectFormByOwnerUsername = connection.prepareStatement("SELECT BIN_TO_UUID(form_id) FROM form WHERE owner_username = ?");
		
		insertResponse = connection.prepareStatement("INSERT INTO response VALUES (?, UUID_TO_BIN(?))");
		insertAnswer = connection.prepareStatement("INSERT INTO answermap VALUES (?, UUID_TO_BIN(?), ?, ?)");
		insertForm = connection.prepareStatement("INSERT INTO form VALUES (UUID_TO_BIN(?), ?, ?, ?)");
		insertField = connection.prepareStatement("INSERT INTO field VALUES (UUID_TO_BIN(?), ?, ?, ?, ?)");
		insertFieldValue = connection.prepareStatement("INSERT INTO field_value VALUES (UUID_TO_BIN(?), ?, ?, ?)");
		insertUser = connection.prepareStatement("INSERT INTO user VALUES(?, ?)");
	}
	
	@Override
	public UUID createForm(Form form) {
		form.setFormId(UUID.randomUUID());
		
		try {
			insertForm.setString(1, form.getFormId().toString());
			insertForm.setString(2, form.getFormName());
			insertForm.setString(3, form.getDescription());
			insertForm.setString(4, form.getOwnerUsername());
			
			if (insertForm.executeUpdate() != 1 || !insertFormFields(form))
				return null;
			
			return form.getFormId();
		} catch (SQLException e) {
			logger.debug("CreateForm: " + e.getMessage());
			return null;
		}
	}

	// inserts form fields in appropriate tables
	private boolean insertFormFields(Form form) throws SQLException {
		List<Field> fields = form.getFields();
		
		int fieldOrder = 1;
		for (Field i : fields) {
			insertField.setString(1, form.getFormId().toString());
			insertField.setString(2, i.getTitle());
			insertField.setString(3, i.getDescription());
			insertField.setString(4, i.getType().toString());
			insertField.setInt(5, fieldOrder++);
			
			if (insertField.executeUpdate() != 1)
				return false;
			
			if (i.getType() != FieldType.TEXT_FIELD && !insertFieldValues(i, form.getFormId()))
				return false;
				
		}
		
		return true;
	}

	private boolean insertFieldValues(Field field, UUID formId) throws SQLException {
		String[] values = field.getValues();
		
		int valueOrder = 1;
		for (String i : values) {
			insertFieldValue.setString(1, formId.toString());
			insertFieldValue.setString(2, field.getTitle());
			insertFieldValue.setString(3, i);
			insertFieldValue.setInt(4, valueOrder++);
			
			if (insertFieldValue.executeUpdate() != 1)
				return false;
		}
		
		return true;
	}
	
	@Override
	public Form getForm(UUID formId) {
		try {
			selectFormByFormId.setString(1, formId.toString());
			ResultSet rSet = selectFormByFormId.executeQuery();
			
			if (rSet.next()) {
				Form result = new Form();
				
				String formidstr = rSet.getString(1);
				result.setFormId(UUID.fromString(formidstr));
				result.setFormName(rSet.getString(2));
				result.setDescription(rSet.getString(3));
				result.setOwnerUsername(rSet.getString(4));
				
				populateFields(result);

				return result;
			} else {
				logger.debug("Form Doesn't Exist UUID: " + formId);
				return null;
			}
		} catch (SQLException e) { 
			logger.debug("getForm query failed: " + e.getMessage());
			return null;
		}
	}
	
	/*
	 * Fills form with fields from appropriate tables
	 * @param form form object with basic information (id, name, description, owner) but no fields.
  	 */
	private void populateFields(Form form) throws SQLException {
		selectFieldByFormId.setString(1, form.getFormId().toString());		
		ResultSet rSet = selectFieldByFormId.executeQuery();
		
		logger.debug("Found " + rSet.getFetchSize() + " fields for form: " + form.getFormName());
		
		List<Field> fields = new ArrayList<Field>();
		while (rSet.next()) {
			Field field = new Field();
			field.setTitle(rSet.getString(2));
			field.setDescription(rSet.getString(3));
			field.setType(FieldType.valueOf(rSet.getString(4)));
			
			if (field.getType() != FieldType.TEXT_FIELD)
				populateFieldValues(field, form.getFormId());
			
			logger.debug("got field: " + form.getFormName() + ", " + field.getTitle());
			fields.add(field);
		}
		
		form.setFields(fields);
	}

	private void populateFieldValues(Field field, UUID formId) throws SQLException {
			selectFieldValueByField.setString(1, formId.toString());
			selectFieldValueByField.setString(2, field.getTitle());
			
			ResultSet rSet = selectFieldValueByField.executeQuery();
			
			List<String> fieldValues = new ArrayList<String>();
			
			while (rSet.next()) 
				fieldValues.add(rSet.getString(3));
			
			String[] values = new String[fieldValues.size()];
			fieldValues.toArray(values);
			
			field.setValues(values);
	}

	@Override
	public List<FormResponse> getAllResponses(UUID formId) {
		List<FormResponse> result = new ArrayList<FormResponse>();
		try {
			selectResponseByFormId.setString(1, formId.toString());
			ResultSet responses = selectResponseByFormId.executeQuery();
			
			while (responses.next()) {
				FormResponse someResponse = new FormResponse();
				someResponse.setResponseId(responses.getInt(1));
				someResponse.setFormId(formId);
				
				populateAnswerMap(someResponse);
				
				result.add(someResponse);
			}
			
			return result;
		} catch (SQLException e ) {
			logger.debug("Failed to obtain form: " + formId.toString() + " responses: " + e.getMessage());
			return null;
		}
	}

	/* takes a FormResponse object with only formId and responseId
	 * and fetches and sets answermap from appropriate tables*/
	private void populateAnswerMap(FormResponse response) throws SQLException {
		Map<String, String> answerMap = new TreeMap<String, String>();
		selectAnswerMapByResponse.setString(1, response.getFormId().toString());
		selectAnswerMapByResponse.setInt(2, response.getResponseId());
			
		ResultSet responseanswers = selectAnswerMapByResponse.executeQuery();
			
		while (responseanswers.next()) {
			answerMap.put(responseanswers.getString(3), responseanswers.getString(4));
		}
			
		response.setResponse(answerMap);
	}

	// if randomly generated number collides with existing responsid -> fail. too lazy to handle this.
	@Override
	public boolean insertResponse(FormResponse response) {
		try {
			int responseId = (int) (Math.random() * ((double)Integer.MAX_VALUE)); // bro this is bad
			response.setResponseId(responseId);
		
			insertResponse.setInt(1, responseId);
			insertResponse.setString(2, response.getFormId().toString());
			
			logger.debug("Inserting response, rows affected: " + insertResponse.executeUpdate());
			
			return insertAnswerMap(response);
		} catch (SQLException e) {
			logger.debug(e.getMessage());
			return false;
		}
	}
	
	private boolean insertAnswerMap(FormResponse response) throws SQLException {
		insertAnswer.setInt(1, response.getResponseId());
		insertAnswer.setString(2, response.getFormId().toString());
		
		Map<String, String> answermap = response.getResponse();
		
		Set<String> keySet = answermap.keySet();
		for (String key : keySet) {
			insertAnswer.setString(3, key);
			insertAnswer.setString(4, answermap.get(key));
			
			if (insertAnswer.executeUpdate() != 1)
				return false;
		}
		
		return true;
	}


	@Override
	public boolean createUser(User user) { 
		try {
			insertUser.setString(1, user.getUsername());
			insertUser.setString(2, encoder.encode(user.getPassword()));
			
			if (insertUser.executeUpdate() != 1)
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			selectUserByUsername.setString(1, username);
			ResultSet rSet = selectUserByUsername.executeQuery();
			
			if (!rSet.next())
				throw new UsernameNotFoundException("User: " + username + " was not found.");
			
			return new User(rSet.getString(1), rSet.getString(2));
		} catch (SQLException e) {
			logger.debug("Failed to load user: " + e.getMessage());
			return null;
		}
	}

	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		return null;
	}

	@Override
	public List<UUID> getFormsOwnedByUser(String username) {
		List<UUID> result = new ArrayList<UUID>();
		
		try {
			selectFormByOwnerUsername.setString(1, username);
			
			ResultSet rs = selectFormByOwnerUsername.executeQuery();
			
			while (rs.next()) 
				result.add(UUID.fromString(rs.getString(1)));
			
		} catch (SQLException e) {
			logger.debug("Error In Finding Forms Owned By User " + username +". : " + e.getMessage());
			return null;
		}
		
		return result;
	}
}

