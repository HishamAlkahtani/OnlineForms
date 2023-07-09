package online.hk10.OnlineForms.database.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

// Defines form and its fields.
public class Form {
	private UUID formId;
	private String ownerUsername;
	private String formName;
	private String description;
	private String[] columns; // will help tabulation and storage
	private List<Field> fields;
	
	public Form(UUID formId, String formName, List<Field> fields) {
		this.formId = formId;
		this.formName = formName;
		this.fields = fields;
		createColumns();
	}
	
	public Form() {
	}
	
	private void createColumns() {
		if (fields == null)
			return;
		
		LinkedList<String> temp = new LinkedList<String>();
		for (Field i : fields) {
			if (i.type == FieldType.CHECKBOXES)
				for (String j : i.values) 
					temp.add(j);
			else 
				temp.add(i.title);
			
		}
		
		columns = new String[temp.size()];
		for (int i = 0; i < columns.length; i++) 
			columns[i] = temp.get(i);
	}
	

	public UUID getFormId() {
		return formId;
	}

	public void setFormId(UUID formId) {
		this.formId = formId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
		createColumns();
	}
	
	public String[] getColumns() {
		return columns;
	}
	
	public String getOwnerUsername() {
		return ownerUsername;
	}
	
	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}

	public boolean equals(Form a) {
		return this.formId == a.formId;
	}
	
	public boolean equals(UUID formId) {
		return this.formId.equals(formId);
	}
	
	public String toString() {
		String result = "formId: ";
		result += formId.toString() + "\n";
		result += "FormName: " + formName + "\n";
		if (fields != null) {
			result += "Fields: ";
			for (Field i : fields) result += i.title + ", "; 
		}
		return result;
	}
	
}
