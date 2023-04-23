package com.devsuperior.movieflix.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.movieflix.DTO.ReviewDTO;
import com.devsuperior.movieflix.services.ReviewService;

@RestController
@RequestMapping(value="/reviews")
public class ReviewResource {
	
	@Autowired
	private ReviewService reviewService;
	
	
	//insert -> only Members
	@PreAuthorize("hasAnyRole('MEMBER')")
	@PostMapping
	public ResponseEntity<ReviewDTO> addNewReview(@Valid @RequestBody ReviewDTO reviewDTO){
				
		reviewDTO = reviewService.insert(reviewDTO);
		URI newResourceUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(reviewDTO.getId()).toUri();
		return ResponseEntity.created(newResourceUri).body(reviewDTO);
		
	}
	
}
