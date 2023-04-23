package com.devsuperior.movieflix.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.movieflix.DTO.MovieDTO;
import com.devsuperior.movieflix.DTO.ReviewDTO;
import com.devsuperior.movieflix.services.MovieService;
import com.devsuperior.movieflix.services.ReviewService;

@RestController
@RequestMapping(value = "/movies")
public class MovieResource {

	@Autowired
	private MovieService movieService;

	@Autowired
	private ReviewService reviewService;

	/**
	 * Movies by Id (ao selecionar um filme o sistema mostra titulo, subtitulo, img,
	 * sinopse, ano)
	 * 
	 * @param id
	 * @return
	 */

	@GetMapping(value = "/{id}")
	public ResponseEntity<MovieDTO> findById(@PathVariable Long id) {
		MovieDTO movieDTO = movieService.findMovieById(id);
		return ResponseEntity.ok(movieDTO);
	}

	/**
	 * Movies by Genre (paginada e ordenada alfabeticamente por titulo) Retorna
	 * todos os filmes ou por gênero, se o gênero for passado
	 * 
	 * @param genreId
	 * @param pageable
	 * @return moviesList
	 */
	@GetMapping
	public ResponseEntity<Page<MovieDTO>> findMovies(
			@RequestParam(value = "genreId", defaultValue = "0") Long genreId, Pageable pageable) {

		Page<MovieDTO> moviesWithGenreList = movieService.findMoviesWithGenre(genreId, pageable);

		return ResponseEntity.ok(moviesWithGenreList);
	}

	/**
	 * Movies with Reviews (retorna os reviews de um dado movie)
	 * 
	 * @param id
	 * @return reviewList
	 */
	@GetMapping(value = "/{id}/reviews")
	public ResponseEntity<List<ReviewDTO>> findMovieReviews(@PathVariable Long id) {
		List<ReviewDTO> reviews = reviewService.findMoviesWithReviews(id);

		return ResponseEntity.ok().body(reviews);

	}

}