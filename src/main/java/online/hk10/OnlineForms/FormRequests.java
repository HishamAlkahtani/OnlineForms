package online.hk10.OnlineForms;

import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import online.hk10.OnlineForms.database.FormsDAO;
import online.hk10.OnlineForms.database.ExcelUtil;
import online.hk10.OnlineForms.database.entities.Form;
import online.hk10.OnlineForms.database.entities.FormResponse;
import online.hk10.OnlineForms.database.entities.User;


@Controller
public class FormRequests {

	@Autowired
	private FormsDAO db;
	private Logger logger;
	
	
	public FormRequests() {
		this.logger = LoggerFactory.getLogger(getClass());
	}

	/* a hacky way for client-side to tell if
	 * current user is authenticated */
	@GetMapping("/isLoggedIn") 
	@ResponseBody
	public Map<String, Boolean> isLoggedIn(Authentication auth) {
		Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
		map.put("isLoggedIn", auth.isAuthenticated());
		return map;
	}
	
	/* userDashboard shows user list of the forms
	 * they created. */
	@GetMapping("/userDashboard")
	public String getUserDashboard(Principal principal, Model model) {
		List<UUID> userFormsIds = db.getFormsOwnedByUser(principal.getName());
		List<Form> forms = new ArrayList<Form>();
		
		for (UUID i : userFormsIds) 
			forms.add(db.getForm(i));
		
		model.addAttribute("forms", forms);
		
 		return "userDashboard";
	}
	
	// Returns form details as JSON, used for client-side
	// rendering
	@GetMapping("/getform/{formId}")
	@ResponseBody
	public Form getForm(@PathVariable("formId") String formId) {
		System.out.println("REMOVE");
		logger.debug("Recieved request for form with UUID: " + formId);
		Form res = db.getForm(UUID.fromString(formId));
		logger.debug("Form with UUID " + formId + ": " + res.getFormName() + " was found.");
		return res;
	}
	
	/* Form html page will have formId only, then
	 * client-side javascript will get necessary 
	 * form information from /getForm endpoint and
	 * render the form*/
	@GetMapping("/form/{formId}")
	public String getFormPage(@PathVariable("formId") UUID formId, Model model) {
		model.addAttribute("formId", formId);
		return "form";
	}
	
	// Endpoint where users submit thier responses
	@PostMapping("/form/{formId}/submit")
	public String submitForm(@PathVariable UUID formId, ServletRequest request) {
		Map<String, String[]> response = request.getParameterMap();
		if (db.insertResponse(new FormResponse(formId, response)))
			return "redirect:/success.html";
		else
			return "redirect:/error";
	}
	
	// Returns responses as JSON data
	@GetMapping("/responsesRaw/{formId}")
	@ResponseBody
	public List<FormResponse> getAllResponses(@PathVariable UUID formId) {
		return db.getAllResponses(formId);
	}
	
	// Returns responses page
	@GetMapping("/responses/{formId}")
	public String getAllResponsesPage(@PathVariable UUID formId, Model model) {
		model.addAttribute("formId", formId);
		return "responses";
	}
	
	// Returns responses as an excel file
	@GetMapping("/responsesExcel/{formId}")
	public ResponseEntity<byte[]> getResponsesExcel(@PathVariable UUID formId) {
		Form requestedForm = db.getForm(formId);
		ByteArrayOutputStream excelFile = ExcelUtil.convertToExcel(db.getForm(formId), db.getAllResponses(formId));
		return ResponseEntity
				.ok()
				.header("Content-Disposition", "attachment; filename=\"" + requestedForm.getFormName() + ".xlsx\"")
				.body(excelFile.toByteArray());
	}
	
	@GetMapping("/createForm")
	public String requestedForm() {
		return "createForm";
	}
	
	@PostMapping("/createForm")
	@ResponseBody
	public String createForm(Principal principal, @RequestBody Form form) {
		form.setOwnerUsername(principal.getName());
		UUID formId = db.createForm(form);
		logger.debug("Successfully created form with ID: " + formId.toString());
		return formId.toString();
	}
	
	@PostMapping("/register")
	public String createUser(User user) {
		if (db.createUser(user))
			return "redirect:/login";
		else
			return "redirect:/error";
	}
	
	@GetMapping("/register")
	public String getRegisterPage() {
		return "register";
	}
	
	@GetMapping("/login")
	public String getLoginPage() {
		return "loginPage";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletResponse response) {
		logger.debug("logout method called");
		Cookie clearJsessionId = new Cookie("JSESSIONID", "");
		clearJsessionId.setMaxAge(0);
		response.addCookie(clearJsessionId);
		return "redirect:/";
	}
	
}
