package com.devsuperior.movieflix.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.ForbiddenException;
import com.devsuperior.movieflix.services.exceptions.UnauthorizedException;

@Service
public class AuthService {

	private static Logger logger = LoggerFactory.getLogger(AuthService.class);
	private static final String ADMIN = "ROLE_ADMIN";
	private static final String MEMBER = "ROLE_MEMBER";
	@Autowired
	private UserRepository userRepository;

	/**
	 * Verifica qual o usuário dentro do contexto da aplicação e:
	 * <ul>
	 * <li>Se existir um usuário logado, verifica o email e retorna o username</li>
	 * </ul>
	 * <li>Se não existir um usuário logado com o email passado, retorna "usuário
	 * inválido"</li>
	 * 
	 * @return username
	 * @exception UnauthorizedException
	 */
	@Transactional(readOnly = true) // operação somente de leitura, não trava o banco
	public User authenticatedUser() {
		logger.info("Getting username from ContextHolder...");
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			logger.info("Authenticating user: " + username);

			return userRepository.findByEmail(username);
		} catch (Exception e) {
			throw new UnauthorizedException("Invalid User");
		}

	}

	/**
	 * Verifica se o usuário que está logado é o mesmo que está sendo consultado e
	 * qual é o Role se:
	 * <ul>
	 * <li>id do usuário logado é o mesmo do usuário consultado e ele não é
	 * <i>ADMIN</i>:<br>
	 * - deve visualizar apenas o seu perfil de usuário <br>
	 * - HttpStatus: 200</li>
	 * <li>id do usuário logado é o mesmo do usuário consultado e ele é
	 * <i>ADMIN</i>:<br>
	 * - deve permitir a visualizaçaõ de todos os perfis de usuários <br>
	 * - HttpStatus: 200</li>
	 * <li>id do usuário logado não é o mesmo do usuário consultado e ele não é
	 * <i>ADMIN</i>:<br>
	 * - não deve permitir a visualização do perfil do usuário <br>
	 * - HttpStatus: 403</li>
	 * </ul>
	 * 
	 * @param userId
	 * @return Acesso negado || Usuário
	 */
	public void validateSelfOrAdmin(Long userId) {
		User user = authenticatedUser();

		if (!user.getId().equals(userId) && !user.hasRole(ADMIN)) {
			throw new ForbiddenException("Access denied");
		}
	}

	public void validateMemberOrVisitor(Long userId) {
		User user = authenticatedUser();

		if (!user.getId().equals(userId) && !user.hasRole(MEMBER)) {
			throw new ForbiddenException("Access denied");
		}
	}
}