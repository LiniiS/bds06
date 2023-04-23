package com.devsuperior.movieflix.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	Page<Review> findByUser(User user, Pageable pageable);
	
	@Query("SELECT review FROM Review review WHERE movie_Id = :movieId")
	List<Review> getReviewsFromMovieId(Long movieId);




}
