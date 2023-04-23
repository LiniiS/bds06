package com.devsuperior.movieflix.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.DTO.UserDTO;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {

	// logger
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username);
		if (user == null) {
			logger.error("Ops! User with username: " + username + " not found!");
			throw new UsernameNotFoundException("User not found!");
		}

		logger.info("User with username: " + username + " found!");
		return user;
	}

	@Transactional(readOnly = true)
	public UserDTO getCurrentUser() {

		User currentUser = authService.authenticatedUser();

		logger.info("Current user " + currentUser);

		return new UserDTO(currentUser);
	}

	@Transactional(readOnly = true)
	public UserDTO findUserById(Long userId) {
		authService.validateMemberOrVisitor(userId);

		Optional<User> optUser = userRepository.findById(userId);
		User userEntity = optUser.orElseThrow(() -> new ResourceNotFoundException("Entity User not found!"));
		return new UserDTO(userEntity);
	}

	@PreAuthorize("hasAnyRole('MEMBER', 'VISITOR')")
	@Transactional(readOnly = true)
	public UserDTO getProfileForCurrentUser() {
		logger.info("Auth Service verification started, calling method: authenticatedUser()");

		User user = authService.authenticatedUser();

		logger.info("Auth Service verification finshed, returned user: " + user.getId());

		return new UserDTO(user);
	}

}
