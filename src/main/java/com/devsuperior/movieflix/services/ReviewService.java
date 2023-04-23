package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.DTO.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private AuthService authService;

	
	
	@Transactional
	public @Valid ReviewDTO insert(@Valid ReviewDTO reviewDTO) {
		Review newReview = new Review();
		getReviewFromDTO(reviewDTO, newReview);
		newReview = reviewRepository.save(newReview);
		return new ReviewDTO(newReview);

	}

	/**
	 * Método auxiliar pra converter em entidade os dados no DTO
	 * 
	 * @param reviewDTO
	 * @param review
	 */
	private void getReviewFromDTO(@Valid ReviewDTO reviewDTO, Review review) {
		review.setText(reviewDTO.getText());
		review.setMovie(movieRepository.getOne(reviewDTO.getMovieId()));
		review.setUser(authService.authenticatedUser());

	}
	
	public List<ReviewDTO> findMoviesWithReviews(Long id) throws ResourceNotFoundException{
		Optional<List<Review>> optionalReview = Optional.ofNullable(reviewRepository.getReviewsFromMovieId(id));
		List<Review> reviews = optionalReview.orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
		return (List<ReviewDTO>) reviews.stream().map(review -> new ReviewDTO(review, review.getUser())).collect(Collectors.toList()); 
	}

}
