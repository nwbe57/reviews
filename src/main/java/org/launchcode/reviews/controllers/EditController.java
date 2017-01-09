package org.launchcode.reviews.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.reviews.models.Review;
import org.launchcode.reviews.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class EditController extends AbstractController {
		
		
	@RequestMapping(value = "/edit/{username}/{uid}", method = RequestMethod.GET)
	public String editPost(HttpServletRequest request, @PathVariable String username,
			         @PathVariable int uid, Model model) throws IOException {
	
		User user = userDao.findByUsername(username);	
			
		if(user.equals(getUserFromSession(request.getSession()))){
			
			Review review = reviewDao.findByUid(uid);
			String movieID = review.getMovieID();
			List<Review> reviewsByTitle = reviewDao.findByMovieID(movieID);
			Double avgRating = review.getAvgRating(movieID, reviewsByTitle);
			model.addAttribute("avgRating", avgRating);
			model.addAttribute("review", review);
			
			return "edit";
			
		} else {
			String error = "Only author can change review";
			model.addAttribute("error", error);
			return "template";
		}
			
	}
		
	
	@RequestMapping(value = "/edit/{username}/{uid}", method = RequestMethod.POST)
	public String editPostForm(HttpServletRequest request, @PathVariable String username, 
			                @PathVariable int uid, Model model) throws IOException {
		
			Review review = reviewDao.findByUid(uid);
			String body = review.getBody();
			Double rating = review.getRating();
			User user = userDao.findByUsername(username);
			String error = "";
			
			if(user.equals(getUserFromSession(request.getSession()))){
				
				model.addAttribute("review", review);
				
				String newBody = request.getParameter("body").trim();
				String button = request.getParameter("button");
				
				String movieID = review.getMovieID();
				
				List<Review> reviewsByTitle = reviewDao.findByMovieID(movieID);
				Double avgRating = review.getAvgRating(movieID, reviewsByTitle);
				
				if(button.equals("delete")){
					
					reviewDao.delete(review);
					return "redirect:/{username}";
				}
				
				try {
			
				@SuppressWarnings("unused")
				Double newRating = Double.parseDouble(request.getParameter("rating"));
				
				} catch (NumberFormatException e) {
					
					model.addAttribute("avgRating", avgRating);
					
					error = "Rating must be between 0.0 and 10.0";
					model.addAttribute("error", error);				
					
					return "edit";
				}
				
				Double newRating = Double.parseDouble(request.getParameter("rating"));
				
				if(!body.equals(newBody) || !rating.equals(newRating)){
				
					review.setBody(newBody);
					review.setRating(newRating);
	
					if(newBody == null || newBody.equals("")){
						
						error = "Must have review text";
						model.addAttribute("body", body);
						model.addAttribute("error", error);
						model.addAttribute("avgRating", avgRating);
						
					} else if(newRating < 0.0 || newRating > 10.0) {
						
						error = "Rating must be between 0.0 and 10.0";
						model.addAttribute("error", error);
						model.addAttribute("avgRating", avgRating);
						
					} else {
						
					    if(button.equals("save")){
					    	review.setModified(new Date());	
					    	reviewDao.save(review);
					    	
					    	avgRating = review.getAvgRating(movieID, reviewsByTitle);
							model.addAttribute("avgRating", avgRating);	
							
					    }  
					}
					
				return "edit"; 	
				}
			}
		
		return "redirect:/{username}/{uid}";
		
	}
	
}
