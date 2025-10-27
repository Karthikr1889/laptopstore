package com.example.laptopstore;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class ExceptionController {
	
   @ExceptionHandler(ResourceNotFoundException.class)
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public ErrorResponse handlerResourceNotFoundException(ResourceNotFoundException ex) {
	   return new ErrorResponse(HttpStatus.NOT_FOUND.value(),ex.getMessage());
   }
   
   //Other exception handler 
   @ExceptionHandler(InvalidDataException.class)
   public ResponseEntity<ErrorResponse> handler(InvalidDataException ex){
	   ErrorResponse exception = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
	   ResponseEntity<ErrorResponse> response = new ResponseEntity<ErrorResponse>(exception, HttpStatus.BAD_REQUEST);
	   return response;
   }
   
   public static class ErrorResponse {
	  
	   private int status;
	   private String message;
	   
	   public ErrorResponse(int status, String message) {
		   this.status=status;
		   this.message = message;
		   
	   }
	   public int getStatus() {
		   return status;
	   }
	   
	   public String getMessage() {
		   return message;
	   }
   }
   
   
}
