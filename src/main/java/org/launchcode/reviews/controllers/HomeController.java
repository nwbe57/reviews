package org.launchcode.reviews.controllers;


import java.util.ArrayList;
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
		
		List<Double >ratings = new ArrayList<Double>();
		
		String movieID = "";
		for(Review review: reviews){
			movieID = review.getMovieID();
		
			model.addAttribute("movieID", movieID);
		
			List<Review> reviewsByTitle = reviewDao.findByMovieID(movieID);
			Double avgRating = review.getAvgRating(movieID, reviewsByTitle);
			ratings.add(avgRating);
		
	}
		model.addAttribute("ratings", ratings);
		model.addAttribute("reviews", reviews);
			
		return "index";
	}
	
	
}


