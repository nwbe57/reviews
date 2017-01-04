package org.launchcode.reviews.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.reviews.models.Actor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class ActorController extends AbstractController{
		
	@RequestMapping(value = "/person", method = RequestMethod.GET)
	public String searchByPerson() {	
		return "person";
	}
	
	@RequestMapping(value = "/person", method = RequestMethod.POST)
	public String searchPerson(HttpServletRequest request, Model model) throws IOException {
		
	try {
	
		String name = request.getParameter("name");
		
		List<List<String>> nameResults = Actor.getActorID(name);
		
		List<String> nameArray = nameResults.get(0);
		List<String> actorIdArray = nameResults.get(1);
		
		String button = request.getParameter("button");
	
    	
    	if(nameArray.size() == 0){
    		String error = "No result found, must type a valid name";
    		model.addAttribute("error", error);
    		return "template";
    	}
    	
		model.addAttribute("name", name);   //displays the user provided name to be
    	model.addAttribute("nameArray", nameArray);  //searched, and provides the results 
    	model.addAttribute("actorIdArray", actorIdArray);
    	
    	for(int i = 0; i < nameArray.size(); i++){
    		
	    	if(button.equals(String.valueOf(i))){
	    		
	    		String actorID = "";
	    		actorID = actorIdArray.get(i);
	    		model.addAttribute("actorID", actorID);
	    		
	    		return "redirect:/person/" + actorID;
	    	}
    	}
    	
    } catch (IOException e) {
    	String error = "No result found, must type a valid name";
    	model.addAttribute("error", error);
    	return "template";
    }
    		
		return "person";
		
	}
	
	
	@RequestMapping(value = "/person/{actorID}", method = RequestMethod.GET)
	public String getActorInfo(HttpServletRequest request, @PathVariable String actorID, Model model) throws IOException {
		
		//code below returns actorInfo - name, birthdate, hometown, bio, picture
      try {

		List<String> actorInfo = Actor.getActorInfo(actorID);
		
		String name = actorInfo.get(0);
		String year = actorInfo.get(1);
		String bio = actorInfo.get(2);
		String hometown = actorInfo.get(3);
		String picture = actorInfo.get(4);
		
		if(year == null || year.equals("") || year.equals("null") || year.equals("\"\"")){
			year = "Not found";
		}
		if(bio == null || bio.equals("") || bio.equals("null") || bio.equals("\"\"")){
			bio = "Not found";
		}
		if(hometown == null || hometown.equals("")  || hometown.equals("null") || hometown.equals("\"\"")){
			hometown = "Not found";
		}
		if(picture == null || picture.equals("")  || picture.equals("null") || picture.equals("\"\"")){
			picture = "Not found";
		}
	
		model.addAttribute("name", name);
		model.addAttribute("year", year);
		model.addAttribute("bio", bio);
		model.addAttribute("hometown", hometown);
		model.addAttribute("picture", picture);
		
		//code below returns the actors filmography
		
		List<List<String>> castInfo = Actor.getFilmography(actorID);
		
		List<String> titles = castInfo.get(0);
		List<String> movieIDs = castInfo.get(1);
		List<String> years = castInfo.get(2);
		List<String> jobs = castInfo.get(3);
		
		model.addAttribute("titles", titles);
		model.addAttribute("movieIDs", movieIDs);
		model.addAttribute("years", years);
		model.addAttribute("jobs", jobs);
		
		
	} catch (IOException e) {
		String error = "No result found for " + actorID;
    	model.addAttribute("error", error);
    	return "template";
	}
		
		return "actorInfo";
		
	}
	
	@RequestMapping(value = "/person/{actorID}", method = RequestMethod.POST)
	public String getFilmByActor(HttpServletRequest request,  @PathVariable String actorID, Model model) throws IOException {
		
		List<List<String>> castInfo = Actor.getFilmography(actorID);
		
		List<String> movieIDs = castInfo.get(1);
		
		String button = request.getParameter("button");
		String movieID = "";
		
		for(int i = 0; i < movieIDs.size(); i++){
    		
	    	if(button.equals(String.valueOf(i))){
	    		
	    		movieID = movieIDs.get(i);
	    		model.addAttribute("movieID", movieID);
	    	}
    	}	
		
		return "redirect:/movie/" + movieID;
		
	}
}

