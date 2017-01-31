package org.launchcode;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.launchcode.reviews.models.Movie;
import org.launchcode.reviews.models.Person;
import org.launchcode.reviews.models.Review;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class ReviewsApplicationTests {
	

	@Test
	@Repeat(10)
	public void testReviewConstructors() {
		
		Review review = new Review();
		
		Double random = Math.random();
		
		String expectedString = String.valueOf(random);
		Double expectedDouble = random;
		
		review.setTitle(expectedString);
		review.setBody(expectedString);
		review.setMovieID(expectedString);
		review.setRating(random);
		
		String actualTitle = review.getTitle();
		String actualBody = review.getBody();
		String actualMovieID = review.getMovieID();
		Double actualRating = review.getRating();
		
		String expectedTitle = expectedString;
		String expectedBody = expectedString;
		String expectedMovieID = expectedString;
		Double expectedRating = expectedDouble;
		
		assertEquals(actualTitle, expectedTitle);
		assertEquals(actualBody, expectedBody);
		assertEquals(actualMovieID, expectedMovieID);
		assertEquals(actualRating, expectedRating);
		assertEquals(review.getCreated(), review.getModified());
		
	}
	
	@Test
	@Repeat(10)
	public void testAvgRating(){
		DecimalFormat df = new DecimalFormat("#.##");
		
		List<Review> reviewsByTitle = new ArrayList<>();
		
		Double random = (double)Math.round(Math.random()*10.0);
		if(random.equals(0.0)){        //random whole # for size of array
			random = random + 1;
		}
		
		Double totalRating = 0.0;
				
			for(int i = 0; i < random; i++){ 
				
				Review review = new Review();
				review.setRating(Math.round(Math.random()*100.0)/10.0); 
				                       //random # 0.0-10.0 with 1 digit after decimal point
				totalRating += review.getRating();
				reviewsByTitle.add(review);
				
			}
			
			Review testReview = new Review();
			
		Double actualAvgRating = testReview.getAvgRating("123", reviewsByTitle);
		
		String x = df.format(totalRating/random);
		Double expectedAvgRating = Double.valueOf(x);
		
		assertEquals(actualAvgRating, expectedAvgRating);
		
	}
	
	
	
	@Test
	@Repeat(10)
	public void testMovieSearchResults() throws IOException{
		
		List<List<String>> bigResults = new ArrayList<>();
		
		try {
			try {
				
				bigResults = Movie.getSearchResults("big", "1");
				
				List<String> titles = bigResults.get(0);
				List<String> ids = bigResults.get(1);
				List<String> yrs = bigResults.get(2);
				List<String> pics = bigResults.get(3);
				
				assertEquals(titles.size(), ids.size());
				assertEquals(titles.size(), yrs.size());
				assertEquals(titles.size(), pics.size());
				
			} catch (FileNotFoundException e) {
			}
		} catch (IOException e) {	
		}
		
		
	}
	
	
	@Rule
	public ExpectedException thrown= ExpectedException.none();
	@Test
	public void testMovieSearchException() throws IOException{
		
		String title = "";
		for(int i = 1; i <= 4; i++){
	
			if(i == 1){
				title = ""; //empty string
			}
			if(i == 2){
				title = "          "; //string of empty spaces
			}
			if(i == 3){
				title = "null"; // null string
			}
			if(i == 4){
				title = null; // null 
			}
		
			@SuppressWarnings("unused")
			List<List<String>> results = new ArrayList<>();
			
			thrown.expect(IOException.class);
			results = Movie.getSearchResults(title, "1");
			
			
		}
		
	}
	
	
	@Test
	public void testNowPlayingResults(){
		
		List<List<String>> nowPlayingResults = new ArrayList<>();
		
		try {
		
			nowPlayingResults = Movie.getNowPlaying(1);
		
		} catch (IOException e) {
			
		}
		
		List<String> titles = nowPlayingResults.get(0);
		List<String> ids = nowPlayingResults.get(1);
		List<String> yrs = nowPlayingResults.get(2);
		List<String> pics = nowPlayingResults.get(3);
		
		assertEquals(titles.size(), ids.size());
		assertEquals(titles.size(), yrs.size());
		assertEquals(titles.size(), pics.size());
		
	}
	
	
	@Test
	@Repeat(10)
	public void testTrailerResults() throws IOException{
		
		String randomID = "";
		
		for(int i = 0; i < 6; i++){
			if(i == 0){
				randomID = String.valueOf(Math.round(Math.random()*10.0));
			}
			if(i == 1){
				randomID = String.valueOf(Math.round(Math.random()*100.0));
			}
			if(i == 2){
				randomID = String.valueOf(Math.round(Math.random()*1000.0));
			}
			if(i == 3){
				randomID = String.valueOf(Math.round(Math.random()*10000.0));
			}
			if(i == 4){
				randomID = String.valueOf(Math.round(Math.random()*100000.0));
			}
			if(i == 5){
				randomID = String.valueOf(Math.round(Math.random()*1000000.0));
			}
			
			String trailer = "";
			try {
				try {
					trailer = Movie.getTrailer(randomID);
				} catch (FileNotFoundException e) {
				}
			} catch (IOException e) {	
			}
			
			assertNotNull(trailer);
		}
		
	}
	
	
	@Test
	@Repeat(10)
	public void testContentRatingResults() throws IOException{
		
		String randomID = "";
		
		for(int i = 0; i < 6; i++){
			if(i == 0){
				randomID = String.valueOf(Math.round(Math.random()*10.0));
			}
			if(i == 1){
				randomID = String.valueOf(Math.round(Math.random()*100.0));
			}
			if(i == 2){
				randomID = String.valueOf(Math.round(Math.random()*1000.0));
			}
			if(i == 3){
				randomID = String.valueOf(Math.round(Math.random()*10000.0));
			}
			if(i == 4){
				randomID = String.valueOf(Math.round(Math.random()*100000.0));
			}
			if(i == 5){
				randomID = String.valueOf(Math.round(Math.random()*1000000.0));
			}
			
			String rated = "";
			try {
				try {
					rated = Movie.getContentRating(randomID);
				} catch (FileNotFoundException e) {
				}
			} catch (IOException e) {	
			}
			
			assertNotNull(rated);
		}
		
	}
	
	
	
	@Test
	@Repeat(10)
	public void testMovieInfoResults() throws IOException{
		
		String randomID = "";
		
		for(int i = 0; i < 6; i++){
			if(i == 0){
				randomID = String.valueOf(Math.round(Math.random()*10.0));
			}
			if(i == 1){
				randomID = String.valueOf(Math.round(Math.random()*100.0));
			}
			if(i == 2){
				randomID = String.valueOf(Math.round(Math.random()*1000.0));
			}
			if(i == 3){
				randomID = String.valueOf(Math.round(Math.random()*10000.0));
			}
			if(i == 4){
				randomID = String.valueOf(Math.round(Math.random()*100000.0));
			}
			if(i == 5){
				randomID = String.valueOf(Math.round(Math.random()*1000000.0));
			}
			
			List<String> info = new ArrayList<String>();
			try {
				try {
					info = Movie.getMovieInfo(randomID);
			
					String title = info.get(0);
					String yr = info.get(1);
					String tag = info.get(2);
					String over = info.get(3);
					String pic = info.get(4);
					
					assertNotNull(title);
					assertNotNull(yr);
					assertNotNull(tag);
					assertNotNull(over);
					assertNotNull(pic);
					
				} catch (FileNotFoundException e) {	
				}
			} catch (IOException e) {	
			}
		}
		
	}
	
	
	
	@Test
	@Repeat(10)
	public void testCastResults() throws IOException{
		
		String randomID = "";
		
		for(int i = 0; i < 6; i++){
			if(i == 0){
				randomID = String.valueOf(Math.round(Math.random()*10.0));
			}
			if(i == 1){
				randomID = String.valueOf(Math.round(Math.random()*100.0));
			}
			if(i == 2){
				randomID = String.valueOf(Math.round(Math.random()*1000.0));
			}
			if(i == 3){
				randomID = String.valueOf(Math.round(Math.random()*10000.0));
			}
			if(i == 4){
				randomID = String.valueOf(Math.round(Math.random()*100000.0));
			}
			if(i == 5){
				randomID = String.valueOf(Math.round(Math.random()*1000000.0));
			}
			
			List<List<String>> cast = new ArrayList<>();
			try {
				try {
					cast = Movie.getCast(randomID);
			
					List<String> chars = cast.get(0);
					List<String> names = cast.get(1);
					List<String> ids = cast.get(2);
					List<String> dirNames = cast.get(3);
					List<String> dirIDs = cast.get(4);
					
					for(int j = 0; j < ids.size(); j++){
						assertNotNull(ids.get(j));
					}
				
					assertEquals(chars.size(), names.size());
					assertEquals(dirNames.size(), dirIDs.size());
					
				} catch (FileNotFoundException e) {	
				}
			} catch (IOException e) {	
			}
			
		}
		
	}
	
	
	@Test
	@Repeat(10)
	public void testPersonInfoResults() throws IOException{
		
		String randomID = "";
		
		for(int i = 0; i < 6; i++){
			if(i == 0){
				randomID = String.valueOf(Math.round(Math.random()*10.0));
			}
			if(i == 1){
				randomID = String.valueOf(Math.round(Math.random()*100.0));
			}
			if(i == 2){
				randomID = String.valueOf(Math.round(Math.random()*1000.0));
			}
			if(i == 3){
				randomID = String.valueOf(Math.round(Math.random()*10000.0));
			}
			if(i == 4){
				randomID = String.valueOf(Math.round(Math.random()*100000.0));
			}
			if(i == 5){
				randomID = String.valueOf(Math.round(Math.random()*1000000.0));
			}
			
			List<String> person = new ArrayList<>();
			try {
				try {
					person = Person.getPersonInfo(randomID);
			
					String name = person.get(0);
					String yr = person.get(1);
					String bio = person.get(2);
					String hometown = person.get(3);
					String pic = person.get(4);
				
					assertNotNull(name);
					assertNotNull(yr);
					assertNotNull(bio);
					assertNotNull(hometown);
					assertNotNull(pic);
					
				} catch (FileNotFoundException e) {	
				}
			} catch (IOException e) {	
			}
			
		}
		
	}
	
	
	
	@Test
	@Repeat(10)
	public void testPersonIdResults() throws IOException{
		
		String randomID = "";
		
		for(int i = 0; i < 6; i++){
			if(i == 0){
				randomID = String.valueOf(Math.round(Math.random()*10.0));
			}
			if(i == 1){
				randomID = String.valueOf(Math.round(Math.random()*100.0));
			}
			if(i == 2){
				randomID = String.valueOf(Math.round(Math.random()*1000.0));
			}
			if(i == 3){
				randomID = String.valueOf(Math.round(Math.random()*10000.0));
			}
			if(i == 4){
				randomID = String.valueOf(Math.round(Math.random()*100000.0));
			}
			if(i == 5){
				randomID = String.valueOf(Math.round(Math.random()*1000000.0));
			}
			
			List<List<String>> nameResults = new ArrayList<>();
			try {
				try {
					nameResults = Movie.getCast(randomID);
			
					List<String> names = nameResults.get(0);
					List<String> ids = nameResults.get(1);
		
					assertEquals(names.size(), ids.size());
					
					
				} catch (FileNotFoundException e) {	
				}
			} catch (IOException e) {	
			}
			
		}
		
	}
	
	
	
	@Test
	@Repeat(10)
	public void testFilmographyResults() throws IOException{
		
		String randomID = "";
		
		for(int i = 0; i < 6; i++){
			if(i == 0){
				randomID = String.valueOf(Math.round(Math.random()*10.0));
			}
			if(i == 1){
				randomID = String.valueOf(Math.round(Math.random()*100.0));
			}
			if(i == 2){
				randomID = String.valueOf(Math.round(Math.random()*1000.0));
			}
			if(i == 3){
				randomID = String.valueOf(Math.round(Math.random()*10000.0));
			}
			if(i == 4){
				randomID = String.valueOf(Math.round(Math.random()*100000.0));
			}
			if(i == 5){
				randomID = String.valueOf(Math.round(Math.random()*1000000.0));
			}
			
			List<List<String>> filmography = new ArrayList<>();
			try {
				try {
					filmography = Person.getFilmography(randomID);
			
					List<String> films = filmography.get(0);
					List<String> ids = filmography.get(1);
					List<String> yrs = filmography.get(2);
					List<String> chars = filmography.get(3);
					
					assertEquals(films.size(), ids.size());
					assertEquals(films.size(), yrs.size());
					assertEquals(films.size(), chars.size());
					
					
				} catch (FileNotFoundException e) {	
				}
			} catch (IOException e) {	
			}
			
		}
		
	}
	

//	@Test
//	public void contextLoads() {
//	}

}
