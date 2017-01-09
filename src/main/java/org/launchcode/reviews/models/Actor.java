package org.launchcode.reviews.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Actor {
	

	public static List<String> getActorInfo(String actorID) throws IOException{
		
		//returns the personal info for each member of the cast listed
		
		URL url = new URL("https://api.themoviedb.org/3/person/" + actorID + "?api_key=cc10b91ab6be4842679242b80c13bb31&language=en-US");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setDoOutput(true);
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-Type", "application/json");
	   
	    BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

	    String actorData  = br.readLine(); //the raw results for actor with the ID# from the url
	    
	    String beg = "\"name\":";
        String end = ",\"place_of_birth";
        String x = Pattern.quote(beg) + "(.*?)" + Pattern.quote(end); //returns just the name
	    
	    String begYr = "\"birthday\":";
        String endYr = ",\"deathday";
        String xYr = Pattern.quote(begYr) + "(.*?)" + Pattern.quote(endYr); //returns just the birthday
        
        String begBio = "\"biography\":";
        String endBio = ",\"birthday";
        String xBio = Pattern.quote(begBio) + "(.*?)" + Pattern.quote(endBio); //returns just the Bio
        
        String begTown = "\"place_of_birth\":";
        String endTown = ",\"popularity";
        String xTown = Pattern.quote(begTown) + "(.*?)" + Pattern.quote(endTown); //returns just the hometown
		
		String begPic = "\"profile_path\":\"/";
		String endPic = "\"}";
		String xPic = Pattern.quote(begPic) + "(.*?)" + Pattern.quote(endPic); //returns just the picture
		
		Pattern pattern = Pattern.compile(x);
		Pattern patternYr = Pattern.compile(xYr);
		Pattern patternBio = Pattern.compile(xBio);
		Pattern patternTown = Pattern.compile(xTown);
		Pattern patternPic = Pattern.compile(xPic);
		
		Matcher matcher = pattern.matcher(actorData);
		Matcher matcherYr = patternYr.matcher(actorData);
		Matcher matcherBio = patternBio.matcher(actorData);
		Matcher matcherTown = patternTown.matcher(actorData);
		Matcher matcherPic = patternPic.matcher(actorData);
		
		String name = "";
		String year = "";
		String bio = "";
		String hometown = "";
		String picture = "";
		
		while(matcher.find()){
			name = matcher.group(1);
		}
		while(matcherYr.find()){ 
        	year = matcherYr.group(1);
        }
        while(matcherBio.find()){ 
        	String preBio1 = matcherBio.group(1);
        	String preBio2 = preBio1.replace("\\n\\n", " ");
        	String preBio3 = preBio2.replace("&amp;", "&");
        	bio = preBio3.replace('\\', ' ');
        }
        while(matcherTown.find()){ 
        	hometown = matcherTown.group(1);
        	
        }
        while(matcherPic.find()){
        	String pic = matcherPic.group(1);
        	picture = "http://image.tmdb.org/t/p/w500//" + pic;	
        }
        
		List<String> actorInfo = new ArrayList<String>();
        actorInfo.add(name);
		actorInfo.add(year);
        actorInfo.add(bio);
        actorInfo.add(hometown);
        actorInfo.add(picture);
        
		return actorInfo;
				
	}
	
	
	
	
	public static List<List<String>> getActorID (String preName) throws IOException{
		
		List<List<String>> resultArray = new ArrayList<List<String>>();
		
		String name = preName.replace(' ', '+'); // ex. "george lucas" changes to "george+lucas"
		
		URL url = new URL("https://api.themoviedb.org/3/search/person?"
		+ "api_key=cc10b91ab6be4842679242b80c13bb31&language=en-US&query=" + name + "&page=1&include_adult=false");
		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

        String nameResults  = br.readLine(); //the raw results of all names found for that search
        
        String beg = "],\"name\":";
        String end = ",\"popularity";
        String x = Pattern.quote(beg) + "(.*?)" + Pattern.quote(end); //returns just the names in quotes
                                                                        //from the raw results
        String begID = "false,\"id\":";
        String endID = ",\"known_for\"";
        String xID = Pattern.quote(begID) + "(.*?)" + Pattern.quote(endID);//returns just the ID#s
        
        Pattern pattern = Pattern.compile(x);
        Pattern patternID = Pattern.compile(xID);
        
        Matcher matcher = pattern.matcher(nameResults);
        Matcher matcherID = patternID.matcher(nameResults);
        
        List<String> nameArray = new ArrayList<String>();
        List<String> idArray = new ArrayList<String>(); 
        
        while (matcher.find()) {                                 	  
        	String namesFound = matcher.group(1);
        	nameArray.add(namesFound);
        }
        while(matcherID.find()){	
        	String IDsFound = matcherID.group(1);
        	idArray.add(IDsFound);
        }
     
          
        resultArray.add(nameArray);
        resultArray.add(idArray);
		
		return resultArray;
	}
	
	

	
	
	public static List<List<String>> getFilmography(String actorID) throws IOException{
		
		//returns actors filmography from their actorID#
		
		List<List<String>> filmography = new ArrayList<List<String>>();
		
		URL url = new URL("https://api.themoviedb.org/3/person/" +  actorID + "/movie_credits?api_key=cc10b91ab6be4842679242b80c13bb31&language=en-US");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

        String filmResults  = br.readLine(); //the raw results of all movies listed for that actor
        
        String beg = "\"title\":";
        String end = "}";
        String x = Pattern.quote(beg) + "(.*?)" + Pattern.quote(end); //returns just the titles in quotes
                                                                        //from the raw results
        String begChar = ",\"character\":";
        String endChar = ",\"credit_id";
        String xChar = Pattern.quote(begChar) + "(.*?)" + Pattern.quote(endChar);//returns character played
        
        String begJob = ",\"job\":";
        String endJob = ",\"original_title";
        String xJob = Pattern.quote(begJob) + "(.*?)" + Pattern.quote(endJob);//returns job title on film
        
        String begYR = "release_date\":";
        String endYR = ",\"title";
        String xYR = Pattern.quote(begYR) + "(.*?)" + Pattern.quote(endYR);//returns just the release yrs
                                                                             //from the raw results
        String begID = "\"id\":";
        String endID = ",";//\"original_title\":";
        String xID = Pattern.quote(begID) + "(.*?)" + Pattern.quote(endID); //returns just the movie ID#s 
                                                                      //from the raw results
        
        Pattern patternTitle = Pattern.compile(x);
        Pattern patternID = Pattern.compile(xID);
        Pattern patternYR = Pattern.compile(xYR);
        Pattern patternChar = Pattern.compile(xChar);
        Pattern patternJob = Pattern.compile(xJob);
        
        Matcher matcher = patternTitle.matcher(filmResults);
        Matcher matcherID = patternID.matcher(filmResults);
        Matcher matcherYR = patternYR.matcher(filmResults);
        Matcher matcherChar = patternChar.matcher(filmResults);
        Matcher matcherJob = patternJob.matcher(filmResults);
        
        List<String> filmArray = new ArrayList<String>();
        List<String> idArray = new ArrayList<String>();   
        List<String> yrArray = new ArrayList<String>();     
        List<String> charArray = new ArrayList<String>(); 
       
        
        while (matcher.find()) {                                 	  
        	String titlesFound = matcher.group(1);
        	filmArray.add(titlesFound);
        }
        while(matcherID.find()){	
        	String IDsFound = matcherID.group(1);
        	idArray.add(IDsFound);
        }
        while(matcherYR.find()){
        	String yrsFound = matcherYR.group(1);
        	yrArray.add(yrsFound);	
        }
        while(matcherChar.find()){                    
        	String charsFound = matcherChar.group(1);     //character names and job titles
        	String chars2 = charsFound.replace("\\", "");  //both get added to charArray
        	charArray.add(chars2);	                        //because the raw results always
        }                                                    //print out crew credits after all
        while(matcherJob.find()){                             // the acting credits
        	String jobsFound = matcherJob.group(1);
        	charArray.add(jobsFound);	
        }
        
		for(int i = 0; i < filmArray.size(); i++){
			if(filmArray.get(i).length() > 40){
				String q = filmArray.get(i).substring(0, 30); 
				filmArray.set(i, q);
			}
			if(charArray.get(i).length() > 40){
				String z = charArray.get(i).substring(0, 30); 
				charArray.set(i, z);
			}
		}
        
        String chars = "";
        String yrss = "";
        String film = "";
        String ids = "";
        for(int i = 0; i < filmArray.size(); i++){

        	String yrs = yrArray.get(i).replace("\"", "0");
        	String yrs2 = yrs.replace("-", "0");

        	if(yrArray.get(i).equals("null") || yrArray.get(i) == null){
        		yrss = yrArray.get(i) + ";000000000000";
        		film = filmArray.get(i) + ";000000000000";
        		chars = charArray.get(i) + ";000000000000";
        		ids = idArray.get(i) + ";000000000000";
        	} else {
        		yrss = yrArray.get(i) + ";" + yrs2;
        		film = filmArray.get(i) + ";" + yrs2;
        		chars = charArray.get(i) + ";" + yrs2;
        		ids = idArray.get(i) + ";" + yrs2;
        	}
             	
        	filmArray.set(i, film);
        	charArray.set(i, chars);
        	yrArray.set(i, yrss);
        	idArray.set(i, ids);
        }
        
        Collections.sort(filmArray, Comparator.comparing(s -> s.split(";")[1]));
        Collections.sort(charArray, Comparator.comparing(s -> s.split(";")[1]));
        Collections.sort(yrArray, Comparator.comparing(s -> s.split(";")[1]));
        Collections.sort(idArray, Comparator.comparing(s -> s.split(";")[1]));
        
        Collections.reverse(filmArray);
        Collections.reverse(charArray);
        Collections.reverse(yrArray);
        Collections.reverse(idArray);
        
		for(int j = 0; j < filmArray.size(); j ++){
			String xx = filmArray.get(j);
			filmArray.set(j, xx.substring( 0, xx.length() - 13));
			String y = yrArray.get(j);
			yrArray.set(j, y.substring( 0, y.length() - 13));
			String z = charArray.get(j);
			charArray.set(j, z.substring( 0, z.length() - 13));
			String zz = idArray.get(j);
			idArray.set(j, zz.substring( 0, zz.length() - 13));
		}	
        
        filmography.add(filmArray);
        filmography.add(idArray);
        filmography.add(yrArray);
        filmography.add(charArray);
        
        return filmography;
		
	}
	

}
