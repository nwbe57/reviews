package org.launchcode.reviews.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Movie{
	
	
	
	public static List<List<String>> getSearchResults(String title) throws IOException{
		
		List<List<String>> searchResults = new ArrayList<List<String>>();
	
		URL url = new URL("https://api.themoviedb.org/3/search/movie?api_key=cc10b91ab6be4842679242b80c13bb31&query=" + title);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setDoOutput(true);
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-Type", "application/json");
	
	    BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
	
	    String initResults  = br.readLine(); //the raw results of all movies containing the user provided title
	    
	    String beg = "\"title\":";
	    String end = ",\"backdrop_path\":";
	    String x = Pattern.quote(beg) + "(.*?)" + Pattern.quote(end); //returns just the titles in quotes
	                                                                  //from the raw results
	    
	    String begYR = "\"release_date\":\"";
	    String endYR = "\",\"genre_ids\":";                            //returns just the release years 
	    String xYR = Pattern.quote(begYR) + "(.*?)" + Pattern.quote(endYR);    //in quotes from the raw results
	    
	    String begID = "\"id\":";
	    String endID = ",\"original_title\":";
	    String xx = Pattern.quote(begID) + "(.*?)" + Pattern.quote(endID); //returns just the ID#s 
	                                                                  //from the raw results
	    
	    Pattern patternTitle = Pattern.compile(x);
	    Pattern patternID = Pattern.compile(xx);
	    Pattern patternYR = Pattern.compile(xYR);
	    
	    Matcher matcher = patternTitle.matcher(initResults);
	    Matcher matcherID = patternID.matcher(initResults);
	    Matcher matcherYR = patternYR.matcher(initResults);
	    
	    List<String> resultArray = new ArrayList<String>(); 
	    List<String> idArray = new ArrayList<String>(); 
	    List<String> yrArray = new ArrayList<String>();//the arrays of movie titles, years,
	                                                          //the corresponding movie ID#s have
	    while (matcher.find()) {                                  //matching indexes.
	    	  
	    	String titlesFound = matcher.group(1);
	    	resultArray.add(titlesFound);	
	    }
	    while(matcherID.find()){
	    	
	    	String IDsFound = matcherID.group(1);
	    	idArray.add(IDsFound);
	    }
	    while(matcherYR.find()){
	    	
	    	String yrsFound = matcherYR.group(1);
	    	yrArray.add(yrsFound);
	    }
	    
	    searchResults.add(resultArray);
	    searchResults.add(idArray);
	    searchResults.add(yrArray);
	    
	    return searchResults;
	}
	
	
	
	

	public static String getTitle(String movieID) throws IOException {
		
		//returns the title from the movieID#
		URL url = new URL("http://api.themoviedb.org/3/movie/" + movieID + "?api_key=cc10b91ab6be4842679242b80c13bb31");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setDoOutput(true);
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-Type", "application/json");
	   
	    BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

	    String movieInfo  = br.readLine(); //the raw results the movie with the ID# from the url
		
        String beg = "\"title\":";
        String end = ",\"video\":";
        String x = Pattern.quote(beg) + "(.*?)" + Pattern.quote(end); //returns just the title 
                                                                      //from the raw results
        Pattern pattern = Pattern.compile(x);
        
        Matcher matcher = pattern.matcher(movieInfo);
        String title = "";
        while(matcher.find()){
        	title = matcher.group(1);
        }
		
		return title;
        
	}
	
	
	
	
	
	public static List<String> getMovieInfo(String movieID) throws IOException{
		
		//returns year, tagline, overview, picture in an Arraylist
		
		URL url = new URL("http://api.themoviedb.org/3/movie/" + movieID + "?api_key=cc10b91ab6be4842679242b80c13bb31");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setDoOutput(true);
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-Type", "application/json");
	   
	    BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

	    String movieInfo  = br.readLine(); //the raw results the movie with the ID# from the url
	    
	    String begYr = "\"release_date\":\"";
        String endYr = "-";
        String xYr = Pattern.quote(begYr) + "(.*?)" + Pattern.quote(endYr); //returns just the year
        
        String begTag = "\"tagline\":";
        String endTag = ",\"title";
        String xTag = Pattern.quote(begTag) + "(.*?)" + Pattern.quote(endTag); //returns just the tagline
        
        String begOver = "\"overview\":";
        String endOver = ",\"popularity";
        String xOver = Pattern.quote(begOver) + "(.*?)" + Pattern.quote(endOver); //returns just the overview
        
        String picInfo = movieInfo.substring(movieInfo.indexOf("popularity") + 3 , movieInfo.length());
		//cuts the raw results (AKA movieInfo) after a certain point to ensure
			//the movie poster is returned for all titles selected

		String begPic = "\"poster_path\":\"/";
		String endPic = "\",\"production";
		String xPic = Pattern.quote(begPic) + "(.*?)" + Pattern.quote(endPic); //returns just the picture
		
		Pattern patternYr = Pattern.compile(xYr);
		Pattern patternTag = Pattern.compile(xTag);
		Pattern patternOver = Pattern.compile(xOver);
		Pattern patternPic = Pattern.compile(xPic);
		
		Matcher matcherYr = patternYr.matcher(movieInfo);
		Matcher matcherTag = patternTag.matcher(movieInfo);
		Matcher matcherOver = patternOver.matcher(movieInfo);
		Matcher matcherPic = patternPic.matcher(picInfo);
		
		String year = "";
		String tagline = "";
		String overview = "";
		String picture = "";
		
		while(matcherYr.find()){ 
        	year = matcherYr.group(1);
        }
        while(matcherTag.find()){ 
        	tagline = matcherTag.group(1);
        }
        while(matcherOver.find()){ 
        	String preOver = matcherOver.group(1);
        	overview = preOver.replace('\\', ' ');
        }
        while(matcherPic.find()){
        	String pic = matcherPic.group(1);
        	picture = "http://image.tmdb.org/t/p/w500//" + pic;	
        }
		
        List<String> info = new ArrayList<String>();
        info.add(year);
        info.add(tagline);
        info.add(overview);
        info.add(picture);
        
		return info;
		
	}
	
	
	
	
	
	
	public static List<String> getDirector(String movieID) throws IOException {
		
		List<String> director = new ArrayList<String>();
				
		URL url = new URL("https://api.themoviedb.org/3/movie/" + movieID + "/credits?api_key=cc10b91ab6be4842679242b80c13bb31");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setDoOutput(true);
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-Type", "application/json");
	   
	    BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

	    String directorInfo  = br.readLine(); //the raw results the cast/crew with the movieID# from the url
	    
	    String beg = ":\"Director\",\"name\":";
        String end = ",\"profile_path";
        String x = Pattern.quote(beg) + "(.*?)" + Pattern.quote(end); //returns just the director name in quotes
                                                                      //from the raw results
        String begID = "Directing\",\"id\":";
        String endID = ",\"job\":\"Director\"";
        String xID = Pattern.quote(begID) + "(.*?)" + Pattern.quote(endID); //returns just director's ID#
        
        Pattern patternName = Pattern.compile(x);
        Pattern patternID = Pattern.compile(xID);
        
        Matcher matcherName = patternName.matcher(directorInfo);
        Matcher matcherID = patternID.matcher(directorInfo);
        
        while (matcherName.find()) {                                  
        	String namesFound = matcherName.group(1);
        	director.add(namesFound);
        }
        while(matcherID.find()){
        	String IDsFound = matcherID.group(1);
        	director.add(IDsFound);
        }
		
		return director; //director.get(0) = name, director.get(1) = ID#
	}
	
	
	
	
	

	public static List<List<String>> getCast(String movieID) throws IOException{
		
		//returns the cast info - character names, actor names, actor ID#s
		
		List<List<String>> castInfo = new ArrayList<List<String>>();
		
		URL url = new URL("https://api.themoviedb.org/3/movie/" + movieID + "/credits?api_key=cc10b91ab6be4842679242b80c13bb31");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setDoOutput(true);
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-Type", "application/json");
	   
	    BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

	    String cast  = br.readLine(); //the raw results the cast with the movieID# from the url
	    
	    String beg = "\"character\":";
        String end = ",\"credit_id\":";
        String xChar = Pattern.quote(beg) + "(.*?)" + Pattern.quote(end); //returns just the character names in quotes
                                                                      //from the raw results
        String begName = "\"name\":";
        String endName = ",\"order\":";
        String xName = Pattern.quote(begName) + "(.*?)" + Pattern.quote(endName); //returns just the actor
                                                                     //names in quotes from the raw results
        
        String begID = ",\"id\":";
        String endID = ",\"name\":";
        String xID = Pattern.quote(begID) + "(.*?)" + Pattern.quote(endID); //returns just the actors ID#s 
                                                                      //from the raw results
        
        Pattern patternChar = Pattern.compile(xChar);
        Pattern patternName = Pattern.compile(xName);
        Pattern patternID = Pattern.compile(xID);
        
        Matcher matcherChar = patternChar.matcher(cast);
        Matcher matcherName = patternName.matcher(cast);
        Matcher matcherID = patternID.matcher(cast);
        
        List<String> charArray = new ArrayList<String>();
        List<String> idArray = new ArrayList<String>(); 
        List<String> nameArray = new ArrayList<String>();
        
        while (matcherChar.find()) {                                  
      	  	String charsFound = matcherChar.group(1);
        	charArray.add(charsFound);	
        }
        while (matcherName.find()) {                                  
        	String namesFound = matcherName.group(1);
        	nameArray.add(namesFound);
        }
        while(matcherID.find()){
        	String IDsFound = matcherID.group(1);
        	idArray.add(IDsFound);
        }
        
        castInfo.add(charArray);
        castInfo.add(nameArray);
        castInfo.add(idArray);
		
		return castInfo;
	}
	
	
}
