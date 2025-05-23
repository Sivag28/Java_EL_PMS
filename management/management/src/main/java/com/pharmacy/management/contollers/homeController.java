package com.pharmacy.management.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pharmacy.management.services.HomeService;


@Controller
public class homeController {
	@GetMapping({"/","/stock/login"})
    public String showLoginPage() {
        return "stock/login"; // login.html
    }

    // Handle login form
    @PostMapping({"/","/stock/login"})
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        // Simple hardcoded check (replace with DB logic later)
        if ("Pharmaplus".equals(username) && "Pharma".equals(password)) {
            return "redirect:/supplier/home"; // success
        } else {
            model.addAttribute("error", "Invalid username or password!");
            return "stock/login"; // return to login page with error
        }
    }
	@GetMapping("/supplier/home") 
	public String home() { 
		return "supplier/home";
		}
	
	@GetMapping({"/info","/supplier/info"}) 
	public String info() { 
		return "supplier/info";
		}
	
		@GetMapping({"/expert", "/supplier/AskExpert"})
	public String expert() { 
		return "supplier/AskExpert";
		}
	
	@GetMapping({"/question"}) 
	public String submitQuestion() { 
		return "supplier/home";
		}
	
		@GetMapping({"/invoice", "/supplier/invoice"})
	public String invoice() { 
		return "supplier/invoice";
		}
	
	@Autowired
    private HomeService homeService;

    @PostMapping("/question")
    public String submitQuestion(@RequestParam String question, @RequestParam String name, @RequestParam String email) {
        // Construct email message
    	String subject = "Expert Assistance Required: Inquiry from " + name;
    	String body = """
                      Dear Expert,
                      
                      I hope this message finds you well. My name is """ + name + " and I am reaching out to you with a question that I believe falls within your area of expertise.\n\n" +
    	              "Question:\n" + question + "\n\n" +
    	              "I would greatly appreciate it if you could take some time to look into this matter. Your expert opinion and advice would be invaluable to me.\n\n" +
    	              "Please feel free to reply to this email (" + email + ") at your earliest convenience.\n\n" +
    	              "Thank you in advance for your time and assistance.\n\n" +
    	              "Best regards,\n" + name;


        // Use the autowired HomeService bean to send the email
        homeService.sendEmail("siva.g282005@gmail.com", subject, body);

        // Redirect to a confirmation page
        return "redirect:/supplier/home";
    }
    
    

}
