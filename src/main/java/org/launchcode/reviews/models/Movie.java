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
	
	public Movie(){}
	
	
	
	public static List<List<String>> getSearchResults(String title, String page) throws IOException{
		
		List<List<String>> searchResults = new ArrayList<List<String>>();
	
		//URL url = new URL("https://api.themoviedb.org/3/search/movie?api_key=cc10b91ab6be4842679242b80c13bb31&query=" + title);
		URL url = new URL("https://api.themoviedb.org/3/search/movie?api_key=cc10b91ab6be4842679242b80c13bb31&language=en-US&query=" + title + "&page=" + page + "&include_adult=false");
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
	    String begPic = "{\"poster_path\":";
	    String endPic = ",\"adult\":";
	    String xPic = Pattern.quote(begPic) + "(.*?)" + Pattern.quote(endPic); //returns the picture
	    
	    String begTotal = "\"total_pages\":";
	    String endTotal = "}";
	    String xTotal = Pattern.quote(begTotal) + "(.*?)" + Pattern.quote(endTotal); //returns the total # results
	    
	    Pattern patternTitle = Pattern.compile(x);
	    Pattern patternID = Pattern.compile(xx);
	    Pattern patternYR = Pattern.compile(xYR);
	    Pattern patternPic = Pattern.compile(xPic);
	    Pattern patternTotal = Pattern.compile(xTotal);
	    
	    Matcher matcher = patternTitle.matcher(initResults);
	    Matcher matcherID = patternID.matcher(initResults);
	    Matcher matcherYR = patternYR.matcher(initResults);
	    Matcher matcherPic = patternPic.matcher(initResults);
	    Matcher matcherTotal = patternTotal.matcher(initResults);
	    
	    List<String> resultArray = new ArrayList<String>(); 
	    List<String> idArray = new ArrayList<String>(); 
	    List<String> yrArray = new ArrayList<String>();  //the arrays of movie titles, years,
	    List<String> picArray = new ArrayList<String>();    //the corresponding movie ID#s have  
	    List<String> totalNum = new ArrayList<String>();         //matching indexes. 
	   
	    while (matcher.find()) {                                 
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
	    while(matcherPic.find()){
	    	String picsFound = matcherPic.group(1);
	    	picArray.add(picsFound);
	    }
	    while(matcherTotal.find()){
	    	String totalFound = matcherTotal.group(1);
	    	totalNum.add(totalFound);
	    }
	    
	    for(int i = 0; i < picArray.size(); i++){
			if(!picArray.get(i).contains(".jpg")){
				String noPic = "No Pic Found";
				picArray.set(i, noPic);
			} else {
				String p1 = picArray.get(i).replace("\"", "");
				String p2 = p1.replace("\\", "");
				String p3 = p2.replace("/", "");
				String picUrl = "http://image.tmdb.org/t/p/w154//" + p3;
				picArray.set(i, picUrl);
			}
	    }
    
	    searchResults.add(resultArray);
	    searchResults.add(idArray);
	    searchResults.add(yrArray);
	    searchResults.add(picArray);
	    searchResults.add(totalNum);
	    
	    return searchResults;
	}
	
	
	
	
	
	public static List<List<String>> getNowPlaying(int page) throws IOException{
		
		List<List<String>> nowPlaying = new ArrayList<List<String>>();
		
		URL url = new URL("https://api.themoviedb.org/3/movie/now_playing?api_key=cc10b91ab6be4842679242b80c13bb31&language=en-US&page=" + page);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setDoOutput(true);
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-Type", "application/json");
	
	    BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
	
	    String initResults  = br.readLine(); 
	    
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
	    String begPic = "{\"poster_path\":";
	    String endPic = ",\"adult\":";
	    String xPic = Pattern.quote(begPic) + "(.*?)" + Pattern.quote(endPic); //returns the picture
	    
	    String begTotal = "\"total_pages\":";
	    String endTotal = ",";
	    String xTotal = Pattern.quote(begTotal) + "(.*?)" + Pattern.quote(endTotal); //returns the total # results
	    
	    Pattern patternTitle = Pattern.compile(x);
	    Pattern patternID = Pattern.compile(xx);
	    Pattern patternYR = Pattern.compile(xYR);
	    Pattern patternPic = Pattern.compile(xPic);
	    Pattern patternTotal = Pattern.compile(xTotal);
	    
	    Matcher matcher = patternTitle.matcher(initResults);
	    Matcher matcherID = patternID.matcher(initResults);
	    Matcher matcherYR = patternYR.matcher(initResults);
	    Matcher matcherPic = patternPic.matcher(initResults);
	    Matcher matcherTotal = patternTotal.matcher(initResults);
	    
	    List<String> resultArray = new ArrayList<String>(); 
	    List<String> idArray = new ArrayList<String>(); 
	    List<String> yrArray = new ArrayList<String>();  //the arrays of movie titles, years,
	    List<String> picArray = new ArrayList<String>();    //the corresponding movie ID#s have                                       
	    List<String> totalNum = new ArrayList<String>();           //matching indexes.
	    
	    while (matcher.find()) {                                 
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
	    while(matcherPic.find()){
	    	String picsFound = matcherPic.group(1);
	    	picArray.add(picsFound);
	    }
	    while(matcherTotal.find()){
	    	String totalFound = matcherTotal.group(1);
	    	totalNum.add(totalFound);
	    }
	    
	    for(int i = 0; i < picArray.size(); i++){
			if(!picArray.get(i).contains(".jpg")){
				String noPic = "No Pic Found";
				picArray.set(i, noPic);
			} else {
				String p1 = picArray.get(i).replace("\"", "");
				String p2 = p1.replace("\\", "");
				String p3 = p2.replace("/", "");
				String picUrl = "http://image.tmdb.org/t/p/w154//" + p3;
				picArray.set(i, picUrl);
			}
	    }
    
	    nowPlaying.add(resultArray);
	    nowPlaying.add(idArray);
	    nowPlaying.add(yrArray);
	    nowPlaying.add(picArray);
	    nowPlaying.add(totalNum);
	    
	    return nowPlaying;
		
		
	}
	
	
	
	
	
	
	public static String getTrailer(String movieID) throws IOException{
		
		URL url = new URL("http://api.themoviedb.org/3/movie/" + movieID + "/videos?api_key=cc10b91ab6be4842679242b80c13bb31");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setDoOutput(true);
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-Type", "application/json");
	   
	    BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

	    String trailer  = br.readLine(); 
	    
        String beg = "US\",\"key\":\"";
        String end = "\",\"name";
        String x = Pattern.quote(beg) + "(.*?)" + Pattern.quote(end); //returns just the youtube key 
                                                                        //for this trailer
        Pattern pattern = Pattern.compile(x);
        Matcher matcher = pattern.matcher(trailer);
        
        String trailerURL = "";
        
        while(matcher.find()){
	        trailerURL = matcher.group(1);
	        trailerURL = "https://www.youtube.com/embed/" + trailerURL;
	    }
        
        if(trailerURL.isEmpty()){
        	trailerURL = "https://www.youtube.com/embed/DH3ItsuvtQg"; //video with not found message
        }
        
		return trailerURL;
		
	}
	
	
	
	
	
	
	public static String getContentRating(String movieID) throws IOException{
		
		URL url = new URL("https://api.themoviedb.org/3/movie/"  
				+ movieID + "?api_key=cc10b91ab6be4842679242b80c13bb31&append_to_response=releases");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setDoOutput(true);
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-Type", "application/json");
	   
	    BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

	    String contentRating  = br.readLine(); 
	    
        String beg = "certification\":\"";
        String end = "\",\"iso_3166_1\":\"US\"";
        String x = Pattern.quote(beg) + "(.*?)" + Pattern.quote(end); //returns just the content rating(G,PG,PG13,R,NR,UR) 
           
        Pattern pattern = Pattern.compile(x);
        Matcher matcher = pattern.matcher(contentRating);
        
        String rated = "";
        
        while(matcher.find()){
	        rated = matcher.group(1);
	    }
        rated = new StringBuilder(rated).reverse().toString();
        rated = rated.split("\"")[0];
        rated = new StringBuilder(rated).reverse().toString();
        
        if(rated.equals("") || rated == null){
        	rated = "Not Found";
        }
        
        
		return rated;
	}
		
	
	
	
	
	
	public static List<String> getMovieInfo(String movieID) throws IOException{
		
		//returns title, year, tagline, overview, picture in an Arraylist
		
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
		
	    Pattern pattern = Pattern.compile(x);
		Pattern patternYr = Pattern.compile(xYr);
		Pattern patternTag = Pattern.compile(xTag);
		Pattern patternOver = Pattern.compile(xOver);
		Pattern patternPic = Pattern.compile(xPic);
		
		Matcher matcher = pattern.matcher(movieInfo);
		Matcher matcherYr = patternYr.matcher(movieInfo);
		Matcher matcherTag = patternTag.matcher(movieInfo);
		Matcher matcherOver = patternOver.matcher(movieInfo);
		Matcher matcherPic = patternPic.matcher(picInfo);
		
		String title = "";
		String year = "";
		String tagline = "";
		String overview = "";
		String picture = "";
		
	    while(matcher.find()){
	        title = matcher.group(1);
	    }
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
        info.add(title);
        info.add(year);
        info.add(tagline);
        info.add(overview);
        info.add(picture);
        
		return info;
		
	}
	
	
		

	public static List<List<String>> getCast(String movieID) throws IOException{
		
		//returns the cast info - character names, actor names, actor ID#s, director name, director ID#
		
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
	    String begDir = ":\"Director\",\"name\":";
        String endDir = ",\"profile_path";
        String xDir = Pattern.quote(begDir) + "(.*?)" + Pattern.quote(endDir); //returns just the director name in quotes
                                                                      //from the raw results
        String begDirID = "Directing\",\"id\":";
        String endDirID = ",\"job\":\"Director\"";
        String xDirID = Pattern.quote(begDirID) + "(.*?)" + Pattern.quote(endDirID); //returns just director's ID#
        
        Pattern patternChar = Pattern.compile(xChar);
        Pattern patternName = Pattern.compile(xName);
        Pattern patternID = Pattern.compile(xID);
        Pattern patternDir = Pattern.compile(xDir);
        Pattern patternDirID = Pattern.compile(xDirID);
        
        Matcher matcherChar = patternChar.matcher(cast);
        Matcher matcherName = patternName.matcher(cast);
        Matcher matcherID = patternID.matcher(cast);
        Matcher matcherDir = patternDir.matcher(cast);
        Matcher matcherDirID = patternDirID.matcher(cast);
        
        List<String> charArray = new ArrayList<String>();
        List<String> idArray = new ArrayList<String>(); 
        List<String> nameArray = new ArrayList<String>();
        List<String> directorName = new ArrayList<String>();
        List<String> directorID = new ArrayList<String>();
        
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
        while (matcherDir.find()) {                                  
        	String dirFound = matcherDir.group(1);
        	directorName.add(dirFound);
        }
        while(matcherDirID.find()){
        	String dirIdFound = matcherDirID.group(1);
        	directorID.add(dirIdFound);
        }
        
        for(int i = 0; i < charArray.size(); i++){
			if(charArray.get(i).length() > 40){
				String q = charArray.get(i).substring(0, 30); 
				charArray.set(i, q);
			}
		}
        
        castInfo.add(charArray);
        castInfo.add(nameArray);
        castInfo.add(idArray);
        castInfo.add(directorName); //director.get(0) = name, director.get(1) = ID#
        castInfo.add(directorID);
        
		return castInfo;
	}
	
	
}
