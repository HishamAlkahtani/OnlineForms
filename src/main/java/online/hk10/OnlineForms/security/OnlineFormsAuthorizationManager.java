package online.hk10.OnlineForms.security;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import online.hk10.OnlineForms.database.FormsDAO;
import online.hk10.OnlineForms.database.entities.User;

public class OnlineFormsAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
	@Autowired
	private FormsDAO db;
	private static Logger logger = LoggerFactory.getLogger(OnlineFormsAuthorizationManager.class);
	
	/* Only authorize the owner of the form to view the form's results. */
	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		var reject = new AuthorizationDecision(false);
		
		Authentication authn = authentication.get();
		Object principal = authn.getPrincipal();
		
		if (!(principal instanceof User))
			return reject;
		
		User userDetails = (User) principal; 

		// should be "/responses/{formId}" or "/responsesRaw/{formId}"
		String uri = object.getRequest().getRequestURI();

		// should be ["", "responses", "{formId}"]
		String[] tokens = uri.split("/");

		if (tokens.length != 3)
			return reject;
		
		logger.debug("Checking Authorization of User: " + userDetails.getUsername() + " to acces form: " + tokens[2] + ". URI: " + uri);
		List<UUID> userOwnedForms = db.getFormsOwnedByUser(userDetails.getUsername());
		
		UUID formToBeAccessed = UUID.fromString(tokens[2]);
		
		for (UUID i : userOwnedForms) 
			if (i.equals(formToBeAccessed)) {
				logger.debug("Authorized  " + userDetails.getUsername() + " to acces form: " + tokens[2] + ". URI: " + uri);
				return new AuthorizationDecision(true);
			}
	
		return reject;
	}
	

}
