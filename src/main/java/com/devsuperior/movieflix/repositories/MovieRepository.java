package com.devsuperior.movieflix.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

	@Query("SELECT movie FROM Movie movie "
			+ "WHERE :genre IS NULL OR movie.genre = :genre "
			+ "ORDER BY movie.title ASC")
	Page<Movie> findByGenre(Genre genre, Pageable pageable);

	//semelhante aos produtos e categorias: retorna como content os filmes 
	//daquela categoria olhando pra lista de filmes
	@Query("SELECT movie FROM Movie movie "
			+ "JOIN FETCH movie.genre "
			+ "WHERE movie IN :movies")	
	List<Movie> findMoviesWithGenres(List<Movie> movies);

	


}
