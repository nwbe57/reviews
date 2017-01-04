package org.launchcode.reviews.models.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.launchcode.reviews.models.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface ReviewDao extends CrudRepository<Review, Integer> {
    
    List<Review> findByAuthor(int authorId);
    
    List<Review> findByMovieID(String movieID);
    
    Review findByUid(int uid);
    
    List<Review> findAll();
    
}