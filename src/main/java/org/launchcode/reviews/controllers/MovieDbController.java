package org.launchcode.reviews.controllers;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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
	
	List<List<String>> nowPlaying = new ArrayList<>(); //declared these 2 arrays above the methods to be able
	List<List<String>> titleResults = new ArrayList<>(); //to pass the same arrays from GET to POST of the
	String title = "";                                             //same URL
	
	
	@RequestMapping(value = "/SearchMovie", method = RequestMethod.GET)
	public String searchByTitle() {
		return "movie";
	}
	
	@RequestMapping(value = "/SearchMovie", method = RequestMethod.POST)
	public String searchByTitle(HttpServletRequest request, Model model) throws IOException {
		
		String preTitle = request.getParameter("preTitle"); //the text the user inputs into the search field
		
		title = preTitle.trim().replace(' ', '+'); //preTitle altered to fit into url.....
		                                                  // ex. "star wars" changed to "star+wars"
		return "redirect:/SearchMovie/" + title;
    	
    }
	
	
	@RequestMapping(value = "/SearchMovie/{title}", method = RequestMethod.GET)
	public String getTitleURL(HttpServletRequest request, @PathVariable String title, Model model) throws IOException {
		
		try {
			
			String preTitle = request.getParameter("preTitle"); //the text the user inputs into the search field
			
			titleResults = Movie.getSearchResults(title,"1");
			
			List<String> resultArray = titleResults.get(0);
			List<String> idArray = titleResults.get(1);
			List<String> yrArray = titleResults.get(2);
			List<String> picArray = titleResults.get(3);
			List<String> totalNum = titleResults.get(4);
			
	    	if(resultArray.size() == 0){
	    		String error = "No result found, must type valid title";
	    		model.addAttribute("error", error);
	    		return "err";
	    	}
	    	
	    	for(int i = 0; i < yrArray.size(); i++){
	    		if(yrArray.get(i).equals("") || yrArray.get(i).equals("null")){
	    			String noYr = "Yr Not Found";
	    			yrArray.set(i, noYr);
	    		}
	    		if(!picArray.get(i).contains("jpg")){
	        		picArray.set(i, "/images/b/noImage.jpg");
	        	}
	    	}
	    	
	    	Integer pages = Integer.parseInt(totalNum.get(0));
			
			List<String> pageNum = new ArrayList<>();
			
			for(int i = 1; i <= pages; i++){
				pageNum.add(String.valueOf(i));
			}
			
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("preTitle", preTitle);   //displays the user provided title to be
	    	model.addAttribute("resultArray", resultArray);  //searched, and provides the results from 
	    	model.addAttribute("idArray", idArray);              //the tMDB database.
	    	model.addAttribute("yrArray", yrArray);
	    	model.addAttribute("picArray", picArray);
	    	
			
		} catch (IOException e) {
			String error = "No result found, must type valid title";
			model.addAttribute("error", error);
			return "err";
		}			
		
		return "movie";
	}
	
	
	@RequestMapping(value = "/SearchMovie/{title}", method = RequestMethod.POST)
	public String postTitleURL(HttpServletRequest request, @PathVariable String title, Model model) throws IOException {
	
		try{
			
			String preTitle = "";
			String button = request.getParameter("button");
			
			if(button.equals("search")){
				preTitle = request.getParameter("preTitle"); //the text the user inputs into the search field
				
				title = preTitle.trim().replace(' ', '+'); //preTitle altered to fit into url.....
				                                                  // ex. "star wars" changed to "star+wars"
				return "redirect:/SearchMovie/" + title;
			}
			
			titleResults = Movie.getSearchResults(title,"1");
			
			List<String> resultArray = titleResults.get(0);
			List<String> idArray = titleResults.get(1);
			List<String> yrArray = titleResults.get(2);
			List<String> picArray = titleResults.get(3);
			List<String> totalNum = titleResults.get(4);
			
	    	if(resultArray.size() == 0){
	    		String error = "No result found, must type valid title";
	    		model.addAttribute("error", error);
	    		return "err";
	    	}
	    	
	    	for(int i = 0; i < yrArray.size(); i++){
	    		if(yrArray.get(i).equals("") || yrArray.get(i).equals("null")){
	    			String noYr = "Yr Not Found";
	    			yrArray.set(i, noYr);
	    		}
	    		if(!picArray.get(i).contains("jpg")){
	        		picArray.set(i, "/images/b/noImage.jpg");
	        	}
	    	}
	    	
	    	Integer pages = Integer.parseInt(totalNum.get(0));
			
			List<String> pageNum = new ArrayList<>();
			
			for(int i = 1; i <= pages; i++){
				pageNum.add(String.valueOf(i));
			}
			
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("preTitle", preTitle);   //displays the user provided title to be
	    	model.addAttribute("resultArray", resultArray);  //searched, and provides the results from 
	    	model.addAttribute("idArray", idArray);              //the tMDB database.
	    	model.addAttribute("yrArray", yrArray);
	    	model.addAttribute("picArray", picArray);
	    	
	    	for(int i = 0; i < idArray.size(); i++){
	    		
		    	if(button.equals(String.valueOf(i))){
		    
		    		String movieID = "";
		    		movieID = idArray.get(i);
		    		model.addAttribute("movieID", movieID);
		    		
		    		return "redirect:/Movie/" + movieID;
		    	}
		    	
	    	}
	    	
		} catch (IOException e) {
			String error = "No result found, must type valid title";
			model.addAttribute("error", error);
			return "err";
		}			
	
	return "movie";
	}
	
	
	@RequestMapping(value = "/SearchMovie/{title}/{page}", method = RequestMethod.GET)
	public String getTitlePage(HttpServletRequest request, @PathVariable String page, @PathVariable String title, Model model) throws IOException{

		String error = "Must enter a valid page #";
		
		try {
			
			titleResults = Movie.getSearchResults(title,page);
			
			List<String> resultArray = titleResults.get(0);
			List<String> idArray = titleResults.get(1);
			List<String> yrArray = titleResults.get(2);
			List<String> picArray = titleResults.get(3);
			List<String> totalNum = titleResults.get(4);
			
			for(int i = 0; i < yrArray.size(); i++){
	    		if(yrArray.get(i).equals("") || yrArray.get(i).equals("null")){
	    			String noYr = "Yr Not Found";
	    			yrArray.set(i, noYr);
	    		}
	    		if(!picArray.get(i).contains("jpg")){
	        		picArray.set(i, "/images/b/noImage.jpg");
	        	}
	    	}
			
			int pages = Integer.parseInt(totalNum.get(0));
			
			List<String> pageNum = new ArrayList<>();
			
			for(int i = 1; i <= pages; i++){
				pageNum.add(String.valueOf(i));
			}
			
			try {
				Integer pg = Integer.parseInt(page);
				if(pg > pages || pg < 1){
					model.addAttribute("error", error);
					return "err";
				}
			} catch (NumberFormatException e) {
				model.addAttribute("error", error);
				return "err";
			}
			
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("resultArray", resultArray);  
	    	model.addAttribute("idArray", idArray);             
	    	model.addAttribute("yrArray", yrArray);
	    	model.addAttribute("picArray", picArray);
	    	model.addAttribute("title", title);
	    	
		} catch (IOException e) {
			model.addAttribute("error", error);
			return "err";
		}
	    	
	    	return "movie";
	}
	
	@RequestMapping(value = "/SearchMovie/{title}/{page}", method = RequestMethod.POST)
	public String postTitlePage(HttpServletRequest request, @PathVariable int page,  @PathVariable String title, Model model) throws IOException {
			
		String button = request.getParameter("button"); 
		
		String preTitle = "";
		
		if(button.equals("search")){
			preTitle = request.getParameter("preTitle"); //the text the user inputs into the search field
			
			title = preTitle.trim().replace(' ', '+'); //preTitle altered to fit into url.....
			                                                  // ex. "star wars" changed to "star+wars"
			return "redirect:/SearchMovie/" + title;
		}
		
		List<String> resultArray = titleResults.get(0);
		List<String> idArray = titleResults.get(1);
		List<String> yrArray = titleResults.get(2);
		List<String> picArray = titleResults.get(3);
		List<String> totalNum = titleResults.get(4);
		
		for(int i = 0; i < picArray.size(); i++){
			if(!picArray.get(i).contains("jpg")){
	    		picArray.set(i, "/images/b/noImage.jpg");
	    	}
		}
			
		Integer pages = Integer.parseInt(totalNum.get(0));
		
		List<String> pageNum = new ArrayList<>();
			
		for(int i = 1; i <= pages; i++){
			pageNum.add(String.valueOf(i));
		}
		
		model.addAttribute("resultArray", resultArray);   
    	model.addAttribute("idArray", idArray);
    	model.addAttribute("yrArray", yrArray);
    	model.addAttribute("picArray", picArray);
    	model.addAttribute("pageNum", pageNum);
    	model.addAttribute("title", title);
		
    	for(int i = 0; i < idArray.size(); i++){
    		
	    	if(button.equals(String.valueOf(i))){
	    
	    		String movieID = "";
	    		movieID = idArray.get(i);
	    		model.addAttribute("movieID", movieID);
	    		
	    		return "redirect:/Movie/" + movieID;
	    	}
	    	
    	}
		return "movie";
	}
	
	
	@RequestMapping(value = "/NowPlaying", method = RequestMethod.GET)
	public String getNowPlaying(HttpServletRequest request, Model model) throws IOException {
		
		nowPlaying = Movie.getNowPlaying(1);  
		
		List<String> resultArray = nowPlaying.get(0);
		List<String> idArray = nowPlaying.get(1);
		List<String> yrArray = nowPlaying.get(2);
		List<String> picArray = nowPlaying.get(3);
		List<String> totalNum = nowPlaying.get(4);
		
		for(int i = 0; i < picArray.size(); i++){
			if(!picArray.get(i).contains("jpg")){
	    		picArray.set(i, "/images/b/noImage.jpg");
	    	}
		}
		
		Integer pages = Integer.parseInt(totalNum.get(0));
		
		List<String> pageNum = new ArrayList<>();
			
		model.addAttribute("resultArray", resultArray);   
		model.addAttribute("idArray", idArray);
    	model.addAttribute("yrArray", yrArray);
    	model.addAttribute("picArray", picArray);
    	model.addAttribute("totalNum", totalNum);
    	
		for(int i = 1; i <= pages; i++){
			pageNum.add(String.valueOf(i));
		}
		
		model.addAttribute("pageNum", pageNum);
		
		return "nowPlaying";
	}
	
	@RequestMapping(value = "/NowPlaying", method = RequestMethod.POST)
	public String postNowPlaying(HttpServletRequest request, Model model) throws IOException {
		
		String button = request.getParameter("button"); 
		
		List<String> resultArray = nowPlaying.get(0);
		List<String> idArray = nowPlaying.get(1);
		List<String> yrArray = nowPlaying.get(2);
		List<String> picArray = nowPlaying.get(3);
		List<String> totalNum = nowPlaying.get(4);
		
		for(int i = 0; i < picArray.size(); i++){
			if(!picArray.get(i).contains("jpg")){
	    		picArray.set(i, "/images/b/noImage.jpg");
	    	}
		}
			
		Integer pages = Integer.parseInt(totalNum.get(0));
		
		List<String> pageNum = new ArrayList<>();
			
		for(int i = 1; i <= pages; i++){
			pageNum.add(String.valueOf(i));
		}
		
		model.addAttribute("resultArray", resultArray);   
    	model.addAttribute("idArray", idArray);
    	model.addAttribute("yrArray", yrArray);
    	model.addAttribute("picArray", picArray);
    	model.addAttribute("totalNum", totalNum);
    	model.addAttribute("pageNum", pageNum);
		
    	for(int i = 0; i < idArray.size(); i++){
    		
	    	if(button.equals(String.valueOf(i))){
	    
	    		String movieID = "";
	    		movieID = idArray.get(i);
	    		model.addAttribute("movieID", movieID);
	    		
	    		return "redirect:/Movie/" + movieID;
	    	}
	    	
    	}
    	
		return "nowPlaying";
		
	}
	
	
	
	@RequestMapping(value = "/NowPlaying/{page}", method = RequestMethod.GET)
	public String getNowPlayingPage(HttpServletRequest request, @PathVariable String page, Model model) throws IOException {
		try {
		
			int pg = Integer.parseInt(page);
			
			nowPlaying = Movie.getNowPlaying(pg);  
			
			List<String> resultArray = nowPlaying.get(0);
			List<String> idArray = nowPlaying.get(1);
			List<String> yrArray = nowPlaying.get(2);
			List<String> picArray = nowPlaying.get(3);
			List<String> totalNum = nowPlaying.get(4);
				
			for(int i = 0; i < picArray.size(); i++){
				if(!picArray.get(i).contains("jpg")){
		    		picArray.set(i, "/images/b/noImage.jpg");
		    	}
			}
			
			Integer pages = Integer.parseInt(totalNum.get(0));
			
	    		if(pg > pages){
	    			String error = "Please enter a valid page number";
	    			model.addAttribute("error", error);
	    			return "err";
	    		}
			
			List<String> pageNum = new ArrayList<>();
				
			for(int i = 1; i <= pages; i++){
				pageNum.add(String.valueOf(i));
			}
			
			model.addAttribute("resultArray", resultArray);   
			model.addAttribute("idArray", idArray);
	    	model.addAttribute("yrArray", yrArray);
	    	model.addAttribute("picArray", picArray);
	    	model.addAttribute("totalNum", totalNum);
	    	model.addAttribute("pageNum", pageNum);
    	
		} catch (Exception e) {
			String error = "Please enter a valid page number";
			model.addAttribute("error", error);
			return "err";
		}
    	
		return "nowPlaying";
	}
	
	@RequestMapping(value = "/NowPlaying/{page}", method = RequestMethod.POST)
	public String postNowPlayingPage(HttpServletRequest request, @PathVariable int page, Model model) throws IOException {
		
		String button = request.getParameter("button");
		
		List<String> totalNum = nowPlaying.get(4);
		
		Integer pages = Integer.parseInt(totalNum.get(0));
		
		List<String> pageNum = new ArrayList<>();
			
		for(int i = 1; i <= pages; i++){
			pageNum.add(String.valueOf(i));
		}
		
		List<String> resultArray = nowPlaying.get(0);
		List<String> idArray = nowPlaying.get(1);
		List<String> yrArray = nowPlaying.get(2);
		List<String> picArray = nowPlaying.get(3);
		
		for(int i = 0; i < picArray.size(); i++){
			if(!picArray.get(i).contains("jpg")){
	    		picArray.set(i, "/images/b/noImage.jpg");
	    	}
		}
		
		model.addAttribute("idArray", idArray);
    	
    	for(int i = 0; i < idArray.size(); i++){
    		
	    	if(button.equals(String.valueOf(i))){
	    		
	    		String movieID = "";
	    		movieID = idArray.get(i);
	    		model.addAttribute("movieID", movieID);
	    		
	    		return "redirect:/Movie/" + movieID;
	    	}
	    	
    	}
    	
    	model.addAttribute("resultArray", resultArray);   
		model.addAttribute("idArray", idArray);
    	model.addAttribute("yrArray", yrArray);
    	model.addAttribute("picArray", picArray);
    	model.addAttribute("pageNum", pageNum);
    	model.addAttribute("totalNum", totalNum);
		
		return "nowPlaying";
	}
	
	@RequestMapping(value = "/Movie/{movieID}", method = RequestMethod.GET)
	public String getMovieByID(HttpServletRequest request, @PathVariable String movieID, Model model) throws IOException {
		
		DecimalFormat df = new DecimalFormat("#.##");
		model.addAttribute("movieID", movieID);
		
		try {
			        
	     //code below returns movieInfo - year, tagline, overview, picture
	        
			List<String> movieInfo = Movie.getMovieInfo(movieID);
			List<Review> reviewsByTitle = reviewDao.findByMovieID(movieID);
			Double ratingTotal = 0.0;
			
			for(int i = 0; i < reviewsByTitle.size(); i++){
				ratingTotal += reviewsByTitle.get(i).getRating();
			}
			String x = df.format(ratingTotal/reviewsByTitle.size());
			Double avgRating = 0.0;
		
			if(reviewsByTitle.size() != 0){
				avgRating = Double.valueOf(x);
				model.addAttribute("avgRating", avgRating);
			} else {
				String noAvg = "Film not reviewed yet";
				model.addAttribute("noAvg", noAvg);
			}
				
			String title = movieInfo.get(0);
	        String year = movieInfo.get(1);
	        String tagline = movieInfo.get(2);
	        String overview = movieInfo.get(3);
	        String picture = movieInfo.get(4);
	        String rated = Movie.getContentRating(movieID);
	        String trailer = Movie.getTrailer(movieID);
	        
        	model.addAttribute("title", title);
        	
        	if(year == null || year.equals("\"\"") || year.equals("")){
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
        	
        	if(!picture.contains("jpg")){
        		picture = "/images/b/noImage.jpg";
        	}
        	
        	model.addAttribute("overview", overview);
        	model.addAttribute("picture", picture);
        	model.addAttribute("rated", rated);
        	model.addAttribute("trailer", trailer);
	        	
		
		//code below returns cast/director names as links to their individual pages
	        	
	        List<List<String>> castInfo = Movie.getCast(movieID);
	        
	        List<String> charArray = castInfo.get(0);
	        List<String> nameArray = castInfo.get(1);
	        List<String> idArray = castInfo.get(2);
	        
	        
	        model.addAttribute("charArray", charArray);   
	    	model.addAttribute("idArray", idArray);  
			model.addAttribute("nameArray", nameArray);
			
			List<String> dirName = castInfo.get(3);
			List<String> dirID = castInfo.get(4);
			
			model.addAttribute("dirName", dirName);
			model.addAttribute("dirID", dirID);
	    	
		} catch (FileNotFoundException e) {
			String error = "There is no movie with ID#" + movieID;
			model.addAttribute("error", error);
			return "err";
		}
		
		return "movieInfo";
		
	}
	

	@RequestMapping(value = "/Movie/{movieID}", method = RequestMethod.POST)
	public String postMovieByID(HttpServletRequest request, @PathVariable String movieID, Model model) throws IOException {
		
		List<List<String>> castInfo = Movie.getCast(movieID);
		List<String> movieInfo = Movie.getMovieInfo(movieID);
        
        List<String> idArray = castInfo.get(2);
		
		String button = request.getParameter("button");
		String actorID = "";
		
		for(int i = 0; i < idArray.size(); i++){
    		
	    	if(button.equals(String.valueOf(i))){
	    		
	    		actorID = idArray.get(i);
	    		model.addAttribute("actorID", actorID);
	    		
	    		return "redirect:/SearchPerson/" + actorID ;
	    	}
    	}	
		List<String> directorName = castInfo.get(3);
		List<String> directorID = castInfo.get(4);
	
		String dirID = "";
		
		for(int i = 0; i < directorName.size(); i++){
			if(button.equals(directorName.get(i))){
				dirID = directorID.get(i);
				return"redirect:/SearchPerson/" + dirID ;
			}
		}
		
		if(button.equals("see_all")){
			
			List<Review> reviews = reviewDao.findByMovieID(movieID);
			String error = "";
			String author = movieInfo.get(0);
			
			Collections.reverse(reviews);
			
			if(reviews.isEmpty()){
				error = "This film hasn't been reviewed";
				model.addAttribute("error", error);
			}
			
			for(Review review: reviews){
				movieID = review.getMovieID();
			
				Double avgRating = review.getAvgRating(movieID, reviews);
			
				model.addAttribute("reviews", reviews);
				model.addAttribute("avgRating", avgRating);
				
			}
			
			model.addAttribute("author", author);
			
			return "seeAll";
		
		}	
		return "redirect:/newpost/{movieID}";
		
	}
	
	
	@RequestMapping(value = "/trailer/{movieID}", method = RequestMethod.GET)
	public String getTrailerLink(HttpServletRequest request, Model model, @PathVariable String movieID) throws IOException {
		
		String trailer = Movie.getTrailer(movieID);
		List<String> titleArray = Movie.getMovieInfo(movieID);
		String title = titleArray.get(0);
		
		String noTrailer = "";
		
		if(trailer.equals("https://www.youtube.com/embed/DH3ItsuvtQg")){
			noTrailer = " Not Found";
		}
		
		model.addAttribute("movieID", movieID);
		model.addAttribute("title", title);
		model.addAttribute("trailer", trailer);
		model.addAttribute("noTrailer", noTrailer);
		
		return "trailer";
	}
	

}
