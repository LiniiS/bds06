package com.devsuperior.movieflix.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.movieflix.DTO.GenreDTO;
import com.devsuperior.movieflix.services.GenreService;

@RestController
@RequestMapping(value = "/genres")
public class GenreResource {

	@Autowired
	private GenreService genreService;

	/**
	 * Busca simples por todos os nomes dos gêneros
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<GenreDTO>> findAllGenres() {

		List<GenreDTO> genresList = genreService.findAllGenres();
		return ResponseEntity.ok().body(genresList);
	}
}
