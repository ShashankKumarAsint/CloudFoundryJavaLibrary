package com.java.CloudFoundryJavaLibrary.ExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionHandler {
	
	 	@org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<ErrorDetails> runtimeExceptionHandler(RuntimeException exc, WebRequest req){

	        ErrorDetails err = new ErrorDetails();
	        err.setTimeStamp(LocalDateTime.now());
	        err.setMessage(exc.getMessage());
	        err.setDescription(req.getDescription(false));
	        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	    }
	 	
	 	@org.springframework.web.bind.annotation.ExceptionHandler(HttpClientErrorException.class)
	    public ResponseEntity<ErrorDetails> httpClientExceptionHandler(HttpClientErrorException exc, WebRequest req){

	        ErrorDetails err = new ErrorDetails();
	        err.setTimeStamp(LocalDateTime.now());
	        err.setMessage(exc.getMessage());
	        err.setDescription(req.getDescription(false));
	        return new ResponseEntity<>(err, exc.getStatusCode());
	    }
	 	
	 	@org.springframework.web.bind.annotation.ExceptionHandler(HttpServerErrorException.class)
	    public ResponseEntity<ErrorDetails> httpServerExceptionHandler(HttpServerErrorException exc, WebRequest req){

	        ErrorDetails err = new ErrorDetails();
	        err.setTimeStamp(LocalDateTime.now());
	        err.setMessage(exc.getMessage());
	        err.setDescription(req.getDescription(false));
	        return new ResponseEntity<>(err, exc.getStatusCode());
	    }
	 	
	 	@org.springframework.web.bind.annotation.ExceptionHandler(IOException.class)
	    public ResponseEntity<ErrorDetails> ioExceptionHandler(IOException exc, WebRequest req){

	        ErrorDetails err = new ErrorDetails();
	        err.setTimeStamp(LocalDateTime.now());
	        err.setMessage(exc.getMessage());
	        err.setDescription(req.getDescription(false));
	        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	    }
	 	
	 	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	    public ResponseEntity<ErrorDetails> generixExceptionHandler(Exception exc, WebRequest req){

	        ErrorDetails err = new ErrorDetails();
	        err.setTimeStamp(LocalDateTime.now());
	        err.setMessage(exc.getMessage());
	        err.setDescription(req.getDescription(false));
	        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	    }
	 	
	 	@org.springframework.web.bind.annotation.ExceptionHandler(NoHandlerFoundException.class)
	    public ResponseEntity<ErrorDetails> noHandlerFoundExceptionHandler(NoHandlerFoundException me, WebRequest req)  {

	        ErrorDetails err=new ErrorDetails();
	        err.setTimeStamp(LocalDateTime.now());
	        err.setDescription(req.getDescription(false));
	        err.setMessage(me.getMessage());
	        return new ResponseEntity<>(err,HttpStatus.BAD_GATEWAY);
	    }
	 	
}
