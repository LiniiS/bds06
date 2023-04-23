package com.devsuperior.movieflix.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.DTO.MovieDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private GenreRepository genreRepository;

	@Transactional
	public MovieDTO findMovieById(Long movieId) {
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);

		// tenta se existir o optionalMovie com o id recuperado pelo repository, passa
		// pro movie
		// pra ser convertido, se não lança a exceção
		Movie movie = optionalMovie
				.orElseThrow(() -> new ResourceNotFoundException("Movie with id : " + movieId + "not found!"));

		return new MovieDTO(movie);
	}

	@Transactional
	public Page<MovieDTO> findMoviesWithGenre(Long genreId, Pageable pageable) {
		@SuppressWarnings("deprecation")
		Genre genre = (genreId == 0) ? null : genreRepository.getOne(genreId);

		Page<Movie> moviesPage = movieRepository.findByGenre(genre, pageable);
		movieRepository.findMoviesWithGenres(moviesPage.getContent());

		return moviesPage.map(pageItem -> new MovieDTO(pageItem));

	}

}
