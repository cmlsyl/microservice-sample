package com.cs.productservice.util.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
    public @ResponseBody ResponseEntity<String> handleException(Exception e) {
		log.error("An unhandled error occured: ", e);
        return ResponseEntity.internalServerError().body("An unhandled error occured");
    }

    @ExceptionHandler(value = ProductServiceException.class)
    public @ResponseBody ResponseEntity<String> handleException(ProductServiceException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}