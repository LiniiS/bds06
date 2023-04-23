package com.devsuperior.movieflix.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.movieflix.DTO.UserDTO;
import com.devsuperior.movieflix.services.UserService;

@RestController
@RequestMapping("/users")
public class UserResource {

	private static Logger logger = LoggerFactory.getLogger(UserResource.class);

	@Autowired
	private UserService userService;

	@GetMapping("/profile")
	public ResponseEntity<UserDTO> getProfileForCurrentUser() {

		logger.info("Looking for current user...");
		UserDTO currentUser = userService.getProfileForCurrentUser();
		logger.info("Current user found: " + currentUser.getId());
		return ResponseEntity.ok().body(currentUser);
	}

}