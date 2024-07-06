package com.cs.orderservice.util.exception;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
    public @ResponseBody ResponseEntity<String> handleException(Exception e) {
		log.error("An unhandled error occured: ", e);
        return ResponseEntity.internalServerError().body("An unhandled error occured");
    }

	@ExceptionHandler(value = FeignException.class)
    public @ResponseBody ResponseEntity<String> handleException(FeignException e) {
		String message = StandardCharsets.UTF_8.decode(e.responseBody().orElse(ByteBuffer.allocate(0))).toString();
        return ResponseEntity.internalServerError().body(message);
    }

    @ExceptionHandler(value = OrderServiceException.class)
    public @ResponseBody ResponseEntity<String> handleException(OrderServiceException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}