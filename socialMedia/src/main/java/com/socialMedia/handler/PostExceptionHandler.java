package com.socialMedia.handler;

import com.socialMedia.exception.MandatoryFieldsMissingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostExceptionHandler {
    @ExceptionHandler(MandatoryFieldsMissingException.class)
    public ResponseEntity<Object> handleMandatoryFieldsMissingException(MandatoryFieldsMissingException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
