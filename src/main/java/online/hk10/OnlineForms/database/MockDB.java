package online.hk10.OnlineForms.database;

import online.hk10.OnlineForms.database.entities.Form;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import online.hk10.OnlineForms.database.entities.FormResponse;
import online.hk10.OnlineForms.database.entities.User;

/* Mock in-memory database, 
for testing purposes and whatnot.*/
@Component
public class MockDB implements FormsDAO{

	@Autowired
	private PasswordEncoder encoder;
	
	private List<FormRepository> formsAndResponses; 
	private List<Form> forms;
	private List<User> users;

	public MockDB() {
		formsAndResponses = new ArrayList<FormRepository>();
		forms = new ArrayList<Form>();
		users = new ArrayList<User>();
	}
	
	public List<FormResponse> getAllResponses(UUID formId) {
		Optional<FormRepository> repo = formsAndResponses.stream().filter((a) -> {
			return a.getFormId().equals(formId);
		}).findFirst();
		
		if (repo.isPresent())
			return repo.get().getResponses();
		return null;
	}
	
	public boolean insertResponse(FormResponse response) {
		FormRepository repo;
		
		Optional<FormRepository> a = formsAndResponses.stream().filter((b) -> {
			return b.getFormId() == response.getFormId();
		}).findFirst();
		
		
		if (a.isEmpty()) {
			repo = new FormRepository(response.getFormId());
			formsAndResponses.add(repo);
		} else
			repo = a.get();
		
		repo.insert(response);
		return true;
	}
	
	public Form getForm(UUID formId) {
		Optional<Form> form = forms.stream().filter((f) -> {
			return f.equals(formId);
		}).findFirst();
		
		if (form.isPresent())
			return form.get();
		return null;
	}

	public UUID createForm(Form form) {
		UUID formId = UUID.randomUUID();
		form.setFormId(formId);
		forms.add(form);
		return formId;
	}


	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		for (User i : users)
			if (i.getUsername().equals(username))
				return i;
		throw new UsernameNotFoundException("User " + username + " Not Found.");
	}
	
	@Override
	public boolean createUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		users.add(user);
		return true;
	}

	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		return null;
	}
	
	@Override
	public List<UUID> getFormsOwnedByUser(String username) {
		List<UUID> result = new ArrayList<UUID>();
		for (Form i : forms) 
			if (i.getOwnerUsername() != null && i.getOwnerUsername().equals(username))
				result.add(i.getFormId());
		return result;
	}

}

class FormRepository {
	private UUID formId;
	private List<FormResponse> responses;
	
	public FormRepository(UUID formId) {
		this.formId = formId;
		responses = new LinkedList<FormResponse>();
	}
	
	public void insert(FormResponse r) {
		responses.add(r);
	}
	
	public List<FormResponse> getResponses() {
		return responses;
	}
	
	public UUID getFormId() {
		return formId;
	}
}
