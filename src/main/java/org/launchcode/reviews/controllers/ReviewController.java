package org.launchcode.reviews.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.reviews.models.Movie;
import org.launchcode.reviews.models.Review;
import org.launchcode.reviews.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class ReviewController extends AbstractController {
	
	
	@RequestMapping(value = "/newpost/{movieID}", method = RequestMethod.GET)
	public String newPostForm(HttpServletRequest request, @PathVariable String movieID, Model model) throws IOException {
		
		try {
		
			List<String> movieInfo = Movie.getMovieInfo(movieID);
			
			User user = getUserFromSession(request.getSession());
			if(user == null){
				String error = "Must be logged in to write a review";
				model.addAttribute("error", error);
				return "err";
			}
			
	        String title = movieInfo.get(0);
	        model.addAttribute("title", title);
			model.addAttribute("movieID", movieID);
		
		} catch (FileNotFoundException e) {
			String error = "Movie with ID#" + movieID + " not found";
			model.addAttribute("error", error);
			return "err";
		}
		return "newpost";
	}
	
	@RequestMapping(value = "/newpost/{movieID}", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, @PathVariable String movieID, Model model) throws IOException {
		
		List<String> movieInfo = Movie.getMovieInfo(movieID);
		
		String body = request.getParameter("body");
		String error = request.getParameter("error");
		String title = movieInfo.get(0);
        Double rating = 0.0;
		User author = getUserFromSession(request.getSession());

		try{
			rating = Double.parseDouble(request.getParameter("rating"));
			if(rating == null || rating < 0.0 || rating > 10.0){
				
				error = "Must have rating between 0.0 and 10.0";
				model.addAttribute("title", title);
				model.addAttribute("body", body);
				model.addAttribute("error", error);
				return "newpost";
			}

		} catch (NumberFormatException e) {
			error = "Must have rating between 0.0 and 10.0";
			model.addAttribute("title", title);
			model.addAttribute("body", body);
			model.addAttribute("error", error);
			return "newpost";
		}
		
		
		if(body == null || body.equals("")){
			error = "Must have review text";
			model.addAttribute("title", title);
			model.addAttribute("body", body);
			model.addAttribute("rating", rating);
			model.addAttribute("error", error);
			return "newpost";
			
		} else {
			
			Review review = new Review(title, movieID, rating, body, author);

			reviewDao.save(review);
		
			String username = author.getUsername();
			int uid = review.getUid();
			
			model.addAttribute("title", title);
			model.addAttribute("body", body);
			model.addAttribute("username", username);
			model.addAttribute("rating", rating);
			
			String urlPost = username + "/" + uid;
			
			return "redirect:/" + urlPost; //redirect to the new post's page 
		}
  
	}
	
	@RequestMapping(value = "/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(HttpServletRequest request, @PathVariable String username,
		                   @PathVariable String uid, Model model) {
		int id = 0; 
		String error = "There is no review with ID# " + uid + " for username " + username;
		
		try {
	        id = Integer.parseInt(uid); //catches exception when user tries to enter text instead
	               //of int for uid.  Had to make PathVar uid a String to make this work.
		} catch(NumberFormatException ex){ 
	    	model.addAttribute("error", error);
			return "err";
	    }
		
		Review review = reviewDao.findByUid(id);
		List<Review> AllReviews = reviewDao.findAll();
		
		if(review == null || !AllReviews.contains(review)){
			
			model.addAttribute("error", error);
			return "err";
		}
		
		User user = userDao.findByUsername(username);
		
		List<Review> userReviews = user.getReviews();
		
		String movieID = review.getMovieID();
		List<Review> reviewsByTitle = reviewDao.findByMovieID(movieID);

		double avgRating = review.getAvgRating(movieID, reviewsByTitle);
					
		if(userReviews.contains(review)){
			
			model.addAttribute("review", review);
			model.addAttribute("avgRating", avgRating);
			
			if(user.equals(getUserFromSession(request.getSession()))){
				
				return "userReview";
				
			}
		} else {
			
			model.addAttribute("error", error);
			return "err";
		}
		
		return "review";
	
	}
	
	@RequestMapping(value = "/{username}/{uid}", method = RequestMethod.POST)
	public String editReview(HttpServletRequest request, @PathVariable String username, 
			                            @PathVariable int uid, Model model) {
		
		Review review = reviewDao.findByUid(uid);
			
		model.addAttribute("review", review);
	
		String movieID = review.getMovieID();
		List<Review> reviewsByTitle = reviewDao.findByMovieID(movieID);
		
		Double avgRating = review.getAvgRating(movieID, reviewsByTitle);
		model.addAttribute("avgRating", avgRating);
			
		return "review";
		
	}
	
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public String userReviews(@PathVariable String username, Model model) {
		
		
		User user = userDao.findByUsername(username);
		
		List<User> users = userDao.findAll();
		
		if(!users.contains(user)){
			
			String error = "Username " + username + " does not exist";
			model.addAttribute("error", error);
			
			return "err";
			
		} else {
			
			List<Review> reviews = user.getReviews();	
			Collections.reverse(reviews);
			String author = user.getUsername();
			List<Double >ratings = new ArrayList<Double>();
			List<String> firstLines = new ArrayList<String>();
			
			String movieID = "";
			for(Review review: reviews){
				movieID = review.getMovieID();
			
				model.addAttribute("movieID", movieID);
				
				List<Review> reviewsByTitle = reviewDao.findByMovieID(movieID);
				Double avgRating = review.getAvgRating(movieID, reviewsByTitle);
				ratings.add(avgRating);
				
				String x = Review.getFirstLine(review);
				firstLines.add(x);
			
			}
			
			model.addAttribute("firstLines", firstLines);
			model.addAttribute("ratings", ratings);
			model.addAttribute("reviews", reviews);
			model.addAttribute("author", author);
		
			return "index";
		
		}
	
	}
	
}
