package org.launchcode.reviews.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.reviews.models.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class PersonController extends AbstractController{
		
	@RequestMapping(value = "/SearchPerson", method = RequestMethod.GET)
	public String searchByPerson() {	
		return "person";
	}
	
	@RequestMapping(value = "/SearchPerson", method = RequestMethod.POST)
	public String searchPerson(HttpServletRequest request, Model model){
	
		String name = request.getParameter("name");

		return "redirect:/SearchPerson/name=" + name;
		
	}
	
	
	
	@RequestMapping(value = "/SearchPerson/name={name}", method = RequestMethod.GET)
	public String getPersonByName(HttpServletRequest request, @PathVariable String name, Model model) {
		
		try {
			
			String actorID = "";
			String pic = "";
			
			List<List<String>> nameResults = Person.getPersonID(name);
			
			List<String> nameArray = nameResults.get(0);
			List<String> actorIdArray = nameResults.get(1);
			List<String> picResults = new ArrayList<String>();
			List<String> picArray = new ArrayList<String>();
			
	    	if(nameArray.size() == 0){
	    		String error = "No result found, must type a valid name";
	    		model.addAttribute("error", error);
	    		return "err";
	    	}
	    	
			model.addAttribute("name", name);   //displays the user provided name to be
	    	model.addAttribute("nameArray", nameArray);  //searched, and provides the results 
	    	model.addAttribute("actorIdArray", actorIdArray);
	    	
	    	for(int i = 0; i < nameArray.size(); i++){
	    		
	    		actorID = actorIdArray.get(i);
	    		picResults = Person.getPersonInfo(actorID);
	    		pic = picResults.get(4);
	    	    picArray.add(pic);
	    		
		    	if(!picArray.get(i).contains("jpg")){
	        		picArray.set(i, "/images/b/noImage.jpg");
	        	}
	    	}
	    	
	    	model.addAttribute("actorID", actorID);
			model.addAttribute("picArray", picArray);
	    	
	    } catch (IOException e) {
	    	String error = "No result found, must type a valid name";
	    	model.addAttribute("error", error);
	    	return "err";
	    }
		
		return "person";
	}
	
	@RequestMapping(value = "/SearchPerson/name={name}", method = RequestMethod.POST)
	public String postPersonByName(HttpServletRequest request, @PathVariable String name,  Model model) throws IOException {
		
		String button = request.getParameter("button");
		
		String actorID = "";
		String pic = "";
		
		List<List<String>> nameResults = Person.getPersonID(name);
		
		List<String> nameArray = nameResults.get(0);
		List<String> actorIdArray = nameResults.get(1);
		List<String> picResults = new ArrayList<String>();
		List<String> picArray = new ArrayList<String>();
		
    	if(nameArray.size() == 0){
    		String error = "No result found, must type a valid name";
    		model.addAttribute("error", error);
    		return "err";
    	}
    	
		model.addAttribute("name", name);   //displays the user provided name to be
    	model.addAttribute("nameArray", nameArray);  //searched, and provides the results 
    	model.addAttribute("actorIdArray", actorIdArray);
    	
    	for(int i = 0; i < nameArray.size(); i++){
    		
    		actorID = actorIdArray.get(i);
    		picResults = Person.getPersonInfo(actorID);
    		pic = picResults.get(4);
    	    picArray.add(pic);
    		
	    	if(button.equals(String.valueOf(i))){
	    		
	    		return "redirect:/SearchPerson/" + actorID;
	    	}
	    	if(!picArray.get(i).contains("jpg")){
        		picArray.set(i, "/images/b/noImage.jpg");
        	}
    	}
    	if(button.equals("search")){
    		return "redirect:/SearchPerson/name=" + request.getParameter("name");
    	}
    	
    	model.addAttribute("actorID", actorID);
		model.addAttribute("picArray", picArray);
		
		return "person";
	}
	
	
	@RequestMapping(value = "/SearchPerson/{actorID}", method = RequestMethod.GET)
	public String getActorInfo(HttpServletRequest request, @PathVariable String actorID, Model model) throws IOException {
		
		//code below returns actorInfo - name, birthdate, hometown, bio, picture
      try {

		List<String> actorInfo = Person.getPersonInfo(actorID);
		
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
		if(!picture.contains("jpg")){
    		picture = "/images/b/noImage.jpg";
    	}
	
		model.addAttribute("name", name);
		model.addAttribute("year", year);
		model.addAttribute("bio", bio);
		model.addAttribute("hometown", hometown);
		model.addAttribute("picture", picture);
		
		//code below returns the actors filmography
		
		List<List<String>> castInfo = Person.getFilmography(actorID);
		
		List<String> titles = castInfo.get(0);
		List<String> movieIDs = castInfo.get(1);
		List<String> years = castInfo.get(2);
		List<String> jobs = castInfo.get(3);
		
		for(int i = 0; i < years.size(); i++){
			if(years.get(i) == null || years.get(i).equals("") || 
					years.get(i).equals("null") || years.get(i).equals("\"\"")){
				
				String yr = "Yr Not Found";
				years.set(i, yr);
			}
		}	
		
		model.addAttribute("titles", titles);
		model.addAttribute("movieIDs", movieIDs);
		model.addAttribute("years", years);
		model.addAttribute("jobs", jobs);
		
		
	} catch (IOException e) {
		String error = "No result found for " + actorID;
    	model.addAttribute("error", error);
    	return "err";
	}
		
      return "actorInfo";
		
	}
	
	@RequestMapping(value = "/SearchPerson/{actorID}", method = RequestMethod.POST)
	public String getFilmByActor(HttpServletRequest request,  @PathVariable String actorID, Model model) throws IOException {
		
		List<List<String>> castInfo = Person.getFilmography(actorID);
		
		List<String> movieIDs = castInfo.get(1);
		
		String button = request.getParameter("button");
		String movieID = "";
		
		for(int i = 0; i < movieIDs.size(); i++){
    		
	    	if(button.equals(String.valueOf(i))){
	    		
	    		movieID = movieIDs.get(i);
	    		model.addAttribute("movieID", movieID);
	    	}
    	}	
		
		return "redirect:/Movie/" + movieID;
		
	}
}

