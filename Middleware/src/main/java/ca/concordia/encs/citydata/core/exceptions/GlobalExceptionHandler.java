package ca.concordia.encs.citydata.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/* This java class handles exceptions globally
 * Author: Sikandar Ejaz
 * Date: 2-8-2025
 */

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exceptions.InvalidProducerException.class)
	public ResponseEntity<String> handleInvalidProducerException(Exceptions.InvalidProducerException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(Exceptions.InvalidOperationException.class)
	public ResponseEntity<String> handleInvalidOperationException(Exceptions.InvalidOperationException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(Exceptions.InvalidProducerParameterException.class)
	public ResponseEntity<String> handleInvalidProducerParameterException(
			Exceptions.InvalidProducerParameterException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(Exceptions.InvalidOperationParameterException.class)
	public ResponseEntity<String> handleInvalidOperationParameterException(
			Exceptions.InvalidOperationParameterException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(Exceptions.UnsupportedParameterTypeException.class)
	public ResponseEntity<String> handleUnsupportedParameterTypeException(
			Exceptions.UnsupportedParameterTypeException ex) {
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An unexpected error occurred: " + ex.getMessage());
	}
}