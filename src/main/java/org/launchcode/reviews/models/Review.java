package org.launchcode.reviews.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.launchcode.reviews.models.dao.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;



@Entity
@Table(name = "review")
public class Review extends AbstractEntity{
	
	@Autowired
	ReviewDao reviewDao;
	
	private String title;
	private String body;
	private String movieID;
	private double rating;
	private double avgRating;
	private User author;
	private Date created;
	private Date modified;
	
	public Review() {}
	
	public Review(String title, String MovieID, double rating, String body, User author) {
		
		super();
		
		this.title = title;
		this.body = body;
		this.movieID = MovieID;
		this.rating = rating;
		this.author = author;
		this.created = new Date();
		this.modified = this.created;
		
		
		author.addReview(this);
	}
	
	
	@NotNull
    @Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotNull
    @Column(name = "body")
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	@NotNull
	@Column(name = "movieID")
	public String getMovieID() {
		return movieID;
	}
	
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	@NotNull
	@Column(name = "rating")
	public double getRating() {
		return rating;
	}
	
	public double getAvgRating(String movieID) {
		
		List<Review> reviewsByTitle = reviewDao.findByMovieID(movieID);
		Double ratingTotal = 0.0;
		
		for(Review review: reviewsByTitle){
			ratingTotal += review.getRating();
		}
		avgRating = ratingTotal/reviewsByTitle.size();
		
		return avgRating;
	}
	
	public void setMovieID(String movieID) {
		this.movieID = movieID;
	}
	
	@ManyToOne
	public User getAuthor() {
		return author;
	}
	
	@SuppressWarnings("unused")
	private void setAuthor(User author) {
		this.author = author;
	}
	
	@NotNull
	@OrderColumn
	@Column(name = "created")
	public Date getCreated() {
		return created;
	}
	
	@SuppressWarnings("unused")
	private void setCreated(Date created) {
		this.created = created;
	}
	
	@NotNull
	@Column(name = "modified")
	public Date getModified() {
		return modified;
	}
	
	public void setModified(Date modified) {
		this.modified = modified;
	}

}