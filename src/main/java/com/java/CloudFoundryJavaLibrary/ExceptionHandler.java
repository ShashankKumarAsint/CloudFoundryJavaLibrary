package com.java.CloudFoundryJavaLibrary;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandler {
	
	
	 	@org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
	 	@ResponseStatus(HttpStatus.BAD_REQUEST)
	    public ErrorDetails runtimeExceptionHandler(RuntimeException exc, WebRequest req){

	        ErrorDetails err = new ErrorDetails();
	        err.setTimeStamp(LocalDateTime.now());
	        err.setMessage(exc.getMessage());
	        err.setDescription(req.getDescription(false));

	        return err;

	    }

}
