package com.trip.global;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	 @ExceptionHandler(Exception.class)
	    public ResponseEntity<ErrorRes> handleTodoNotFoundException(NotFoundException ex) {

	        return ResponseEntity.status(404)
	                .body(new ErrorRes(ex.getMessage()));
	    }
}
