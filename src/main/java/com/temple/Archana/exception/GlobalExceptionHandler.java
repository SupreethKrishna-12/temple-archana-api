package com.temple.Archana.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(DevoteeNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleDevoteeNotFound(DevoteeNotFoundException exception) {
		
		ErrorResponse errorresponse = new ErrorResponse(404, exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorresponse);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleRequestValidationExcetion(MethodArgumentNotValidException exception){
		
		Map<String, String> errors = new LinkedHashMap<>();
		
		 exception.getBindingResult().getFieldErrors().forEach(error -> {
												errors.put(
														error.getField(),
														error.getDefaultMessage()
														);	
												      }
				 								    );
	
		
				
		ErrorResponse errorResponse = new ErrorResponse(400,"Validation Failed",errors);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		

		System.out.println(exception.getCause());

		ErrorResponse errorresponse = new ErrorResponse(400, "Please provide correct request with proper datatype");
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorresponse);
		
	}
		
}
