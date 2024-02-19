package com.example.questions.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleException() {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage("Connection failed to the database, try again later");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ImageTypeException.class)
    public ResponseEntity<ErrorResponse> handleException(ImageTypeException exc) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BlankTextException.class)
    public ResponseEntity<ErrorResponse> handleException(BlankTextException exc) {

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidInputException exc) {

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponse> handleException(ConnectException exc) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}