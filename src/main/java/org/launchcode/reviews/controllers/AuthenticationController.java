package org.launchcode.reviews.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.reviews.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	

	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		model.addAttribute("username", username);
		
		
		if(!User.isValidUsername(username)){
			String username_error = "Not a valid username";
			model.addAttribute("username_error", username_error);
			return "signup";
		}
		 
		else if(!User.isValidPassword(password)){
			final String password_error = "Not a valid password";
			model.addAttribute("password_error", password_error);
			return "signup";
		}
		
		else if(!password.equals(verify)){
			final String verify_error = "Passwords must match";
			model.addAttribute("verify_error", verify_error);
			return "signup";
		
		} else {
	
			User user = new User(username, password);
			userDao.save(user);
			HttpSession session = request.getSession();
			setUserInSession(session, user);
			
			return "redirect:/newpost";
		}
		
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		
		String password = request.getParameter("password");
		String username = request.getParameter("username");
		String error = request.getParameter("error");
		
		model.addAttribute("username", username);
		
		User user = userDao.findByUsername(username);
		
		List<User> users = userDao.findAll();
	
		
		if(!users.contains(user)){
			
			error = "Invalid Username";
			model.addAttribute("error", error);
	    	return "login";
	    	
		} else if(!user.isMatchingPassword(password)){
			
			error = "Invalid Password";
			model.addAttribute("error", error);
			return "login";
		
		} else {
			HttpSession session = request.getSession();
			setUserInSession(session, user);
		
			return "redirect:/";
		}	
		
}
		
	
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
