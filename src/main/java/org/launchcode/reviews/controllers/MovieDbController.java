package org.launchcode.reviews.controllers;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.reviews.models.Movie;
import org.launchcode.reviews.models.Review;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class MovieDbController extends AbstractController{
	
	
	@RequestMapping(value = "/movie", method = RequestMethod.GET)
	public String searchByTitle() {
		return "movie";
	}
	
	@RequestMapping(value = "/movie", method = RequestMethod.POST)
	public String searchByTitle(HttpServletRequest request, Model model) throws IOException {
		
	try {
		
		String preTitle = request.getParameter("preTitle"); //the text the user inputs into the search field
		
		String title = preTitle.trim().replace(' ', '+'); //preTitle altered to fit into url.....
		                                                  // ex. "star wars" changed to "star+wars"
		List<List<String>> titleResults = Movie.getSearchResults(title);
		
		String button = request.getParameter("button");
		
		List<String> resultArray = titleResults.get(0);
		List<String> idArray = titleResults.get(1);
		List<String> yrArray = titleResults.get(2);
		
    	if(resultArray.size() == 0){
    		String error = "No result found, must type valid title";
    		model.addAttribute("error", error);
    		return "template";
    	}
    	

		model.addAttribute("preTitle", preTitle);   //displays the user provided title to be
    	model.addAttribute("resultArray", resultArray);  //searched, and provides the results from 
    	model.addAttribute("idArray", idArray);              //the tMDB database.
    	model.addAttribute("yrArray", yrArray);
    	
    	try {

	    	for(int i = 0; i < idArray.size(); i++){
	    		
		    	if(button.equals(String.valueOf(i))){
		    		
		    		String movieID = "";
		    		movieID = idArray.get(i);
		    		model.addAttribute("movieID", movieID);
		    		
		    		return "redirect:/movie/" + movieID;
		    	}
	    	}
	    	
		} catch (IndexOutOfBoundsException e) {
			String error = "Result not found";
			model.addAttribute("error", error);
			return "template";
		}
		
	} catch (IOException e) {
		String error = "No result found, must type valid title";
		model.addAttribute("error", error);
		return "template";
	}			
    	return "movie";
    	
    }
	
	@RequestMapping(value = "/movie/{movieID}", method = RequestMethod.GET)
	public String movieIDGet(HttpServletRequest request, @PathVariable String movieID, Model model) throws IOException {
		
		try {
			        
	        String title = Movie.getTitle(movieID);
	        model.addAttribute("title", title);
	      
	     //code below returns movieInfo - year, tagline, overview, picture
	        
			List<String> movieInfo = Movie.getMovieInfo(movieID);
			List<Review> reviewsByTitle = reviewDao.findByMovieID(movieID);
			Double ratingTotal = 0.0;
			
			for(int i = 0; i < reviewsByTitle.size(); i++){
				ratingTotal += reviewsByTitle.get(i).getRating();
			}
			Double avgRating = ratingTotal/reviewsByTitle.size();
		
			if(reviewsByTitle.size() != 0){
				model.addAttribute("avgRating", avgRating);
			} else {
				String noAvg = "Film not reviewed yet";
				model.addAttribute("noAvg", noAvg);
			}
						
	        String year = movieInfo.get(0);
	        String tagline = movieInfo.get(1);
	        String overview = movieInfo.get(2);
	        String picture = movieInfo.get(3);
	        	
	        	if(year == null ||year.equals("\"\"")){
	        		year = "No Release Date Found";
	        	}
	        	
	        	model.addAttribute("year", year);

	        	if(tagline == null || tagline.equals("null") || tagline.equals("\"\"")){
	        		tagline = "No Tagline Found";
	        	}
	        	
	        	model.addAttribute("tagline", tagline);

	        	if(overview == null || overview.equals("null") || overview.equals("\"\"")){
	        		overview = "No Overview Found";
	        	}
	        	
	        	model.addAttribute("overview", overview);

	        	model.addAttribute("picture", picture);
	        	
		
		//code below returns cast/director names as links to their individual pages
	        	
	        List<List<String>> castInfo = Movie.getCast(movieID);
	        
	        List<String> charArray = castInfo.get(0);
	        List<String> nameArray = castInfo.get(1);
	        List<String> idArray = castInfo.get(2);
	        
	        model.addAttribute("charArray", charArray);   
	    	model.addAttribute("idArray", idArray);  
			model.addAttribute("nameArray", nameArray);
			
			String dirName = "";
			String dirID = "";
			
			try{
			
			List<String> director = Movie.getDirector(movieID);
			
			dirName = director.get(0);
			dirID = director.get(1);
			
			} catch (IndexOutOfBoundsException e) {
				dirName = "Not found";
				dirID = "Not found";
			}
			
			model.addAttribute("dirName", dirName);
			model.addAttribute("dirID", dirID);
	    	
		} catch (FileNotFoundException e) {
			String error = "There is no movie with ID#" + movieID;
			model.addAttribute("error", error);
			return "err";
		}
		
		return "movieInfo";
		
	}
	

	@RequestMapping(value = "/movie/{movieID}", method = RequestMethod.POST)
	public String movieID(HttpServletRequest request, @PathVariable String movieID, Model model) throws IOException {
		
		List<List<String>> castInfo = Movie.getCast(movieID);
        
        List<String> idArray = castInfo.get(2);
		
		String button = request.getParameter("button");
		String actorID = "";
		
		for(int i = 0; i < idArray.size(); i++){
    		
	    	if(button.equals(String.valueOf(i))){
	    		
	    		actorID = idArray.get(i);
	    		model.addAttribute("actorID", actorID);
	    		
	    		return "redirect:/person/" + actorID ;
	    	}
    	}	
		List<String> director = Movie.getDirector(movieID);
		
		String dirName = "";
		String dirID = "";
		
		try{
		
		dirName = director.get(0);
		dirID = director.get(1);
		
		} catch (IndexOutOfBoundsException e) {
			return "redirect:/movie/" + movieID;
		}
		
		dirName = director.get(0);
		dirID = director.get(1);
		
		if(button.equals(dirName)){
			return"redirect:/person/" + dirID ;
		}
		
		if(button.equals("see_all")){
			List<Review> reviews = reviewDao.findByMovieID(movieID);
			if(reviews.isEmpty()){
				String error = "This film hasn't been reviewed";
				String author = Movie.getTitle(movieID);
				model.addAttribute("author", author);
				model.addAttribute("error", error);
			}
			model.addAttribute("reviews", reviews);
			return "index";
		}
		
    	model.addAttribute("movieID", movieID);
			
		return "redirect:/newpost/{movieID}";
		
	}
	

}
