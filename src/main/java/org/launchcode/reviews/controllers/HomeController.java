package org.launchcode.reviews.controllers;


import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.reviews.models.Review;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
public class HomeController extends AbstractController {


	
	@RequestMapping(value = "/")
	public String reviewIndex(HttpServletRequest request, Model model) {
		
		List<Review> reviews = reviewDao.findAll();
		
		Collections.reverse(reviews); //returns the most recent review first
//		
//		Double ratingTotal = 0.0;
//		int denom = 0;
//		Double [] avgRating = new Double[reviews.size()];
//		
//		List<String> idArray = new ArrayList<>();
//		
//			for(Review review: reviews){
//				idArray.add(review.getMovieID());
//			}
//			 
//			for(int i = 0; i< reviews.size(); i++){
//				denom = 1;
//				ratingTotal = reviews.get(i).getRating();
//				
//					for(int j = i+1; j < reviews.size(); j++){
//						
//						if(idArray.get(i) == idArray.get(j)){
//							
//
//						ratingTotal += reviews.get(j).getRating();
//						denom++;
//						}
//						
//					}
//					avgRating[i] = ratingTotal/denom;
//					
//					
//			}
//			
//		
//			
//			
//			model.addAttribute("denom", denom);
//			model.addAttribute("ratingTotal", ratingTotal);
//			
//			model.addAttribute("avgRating", avgRating);
//	
			model.addAttribute("reviews", reviews);
			
			return "index";
	}
	
	
}


