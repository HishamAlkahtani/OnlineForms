	package online.hk10.OnlineForms.database.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class FormResponse {
	private UUID formId;
	private int responseId;
	private Map<String, String> response;
	
	public FormResponse(UUID formId, Map<String, String[]> args) {
		this.formId = formId;
		response = new HashMap<String, String>();
		
		args.forEach((a, b) -> {
			// do not include csrf token in response
			if (!a.equals("_csrf"))
				response.put(a,b[0]);
		});
	}
	
	public FormResponse() {}
	
	public UUID getFormId() {
		return formId;
	}
	
	public Map<String, String> getResponse() {
		return response;
	}
	
	public int getResponseId() {
		return responseId;
	}

	public void setResponseId(int responseId) {
		this.responseId = responseId;
	}

	public void setFormId(UUID formId) {
		this.formId = formId;
	}
	
	public void setResponse(Map<String, String> response) {
		this.response = response;
	}
}
