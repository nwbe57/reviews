package org.launchcode;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.launchcode.reviews.models.Review;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ReviewsApplicationTests {
	

	@Test
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
		
	}
	
	@Test
	public void testSetAvgRating(){
		
		Review review = new Review();
		
		Double random = Math.random();
        Double random2 = Math.random();
		Double random3 = Math.random();
		Double random4 = Math.random();
		Double random5 = Math.random();
		
		Review review1 = new Review();   review1.setRating(random);
		Review review2 = new Review();   review2.setRating(random2);
		Review review3 = new Review();   review3.setRating(random3);
		Review review4 = new Review();   review4.setRating(random4);
		Review review5 = new Review();   review5.setRating(random5);
		
		List<Review> reviewsByTitle = new ArrayList<>();
		reviewsByTitle.add(review1);
		reviewsByTitle.add(review2);
		reviewsByTitle.add(review3);
		reviewsByTitle.add(review4);
		reviewsByTitle.add(review5);
		
		review.setAvgRating((review1.getRating() + review2.getRating() +
                             review3.getRating() + review4.getRating() + 
                             review5.getRating())/reviewsByTitle.size());
		
		Double actualAvgRating = review.getAvgRating("12345", reviewsByTitle);
		Double expectedAvgRating = Math.round(((random + random2 +
                                    random3 + random4 + 
                                    random5)/5.0)*100.0)/100.0;
		
		assertEquals(actualAvgRating, expectedAvgRating);
	}
	
	
	

	@Test
	public void contextLoads() {
	}

}
