package online.hk10.OnlineForms;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Error implements ErrorController {

	@GetMapping("/error")
	@ResponseBody
	public String error() {
		return "ERROR!";
	}
		
}
