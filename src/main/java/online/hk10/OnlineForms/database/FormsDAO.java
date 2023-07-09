package online.hk10.OnlineForms.database;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;

import online.hk10.OnlineForms.database.entities.Form;
import online.hk10.OnlineForms.database.entities.FormResponse;
import online.hk10.OnlineForms.database.entities.User;

public interface FormsDAO extends UserDetailsService, UserDetailsPasswordService {	
	/* returns id of created form, formId of passed object (form) will be ignored. */
	UUID createForm(Form form); 
	
	Form getForm(UUID formId);
	
	List<FormResponse> getAllResponses(UUID formId);
	
	/* responseId of passed object (response) will be ignored and randomly generated */
	boolean insertResponse(FormResponse response);
	
	boolean createUser(User user);
	
	List<UUID> getFormsOwnedByUser(String username);
}
