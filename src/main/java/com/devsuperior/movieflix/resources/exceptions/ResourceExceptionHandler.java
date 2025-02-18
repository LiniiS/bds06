package com.devsuperior.movieflix.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.movieflix.services.exceptions.DataBaseException;
import com.devsuperior.movieflix.services.exceptions.ForbiddenException;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import com.devsuperior.movieflix.services.exceptions.UnauthorizedException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		StandardError standardError = new StandardError();
		standardError.setTimestamp(Instant.now());
		standardError.setStatus(status.value());
		standardError.setError("Resource not found");
		standardError.setMessage(exception.getMessage());
		standardError.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(standardError);

	}

	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandardError> dataBaseIntegrityViolation(DataBaseException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		StandardError standardError = new StandardError();
		standardError.setTimestamp(Instant.now());
		standardError.setStatus(status.value());
		standardError.setError("Data Base exception");
		standardError.setMessage(exception.getMessage());
		standardError.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(standardError);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validationException(MethodArgumentNotValidException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; // 422

		ValidationError validationError = new ValidationError();
		validationError.setTimestamp(Instant.now());
		validationError.setStatus(status.value());
		validationError.setError("Validation exception");
		validationError.setMessage(exception.getMessage());
		validationError.setPath(request.getRequestURI());

		// acessa a lista de errors interna do beans valitaion
		for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
			validationError.addError(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return ResponseEntity.status(status).body(validationError);

	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<OAuthCustomError> forbiddenResourceAccess(ForbiddenException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.FORBIDDEN;

		OAuthCustomError oAuthCustomError = new OAuthCustomError("Forbidden", exception.getMessage());

		return ResponseEntity.status(status).body(oAuthCustomError);

	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<OAuthCustomError> forbiddenResourceAccess(UnauthorizedException exception,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		OAuthCustomError oAuthCustomError = new OAuthCustomError("Unauthorized", exception.getMessage());

		return ResponseEntity.status(status).body(oAuthCustomError);

	}

}