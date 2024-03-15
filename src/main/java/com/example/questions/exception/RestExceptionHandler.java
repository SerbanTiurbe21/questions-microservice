package com.example.questions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(value = {BlankTextException.class})
    public ResponseEntity<Object> handleBlankTextException(BlankTextException exception) {
        QuestionsException questionsException = new QuestionsException(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(questionsException, questionsException.getHttpStatus());
    }

    @ExceptionHandler(value = {InvalidInputException.class})
    public ResponseEntity<Object> handleInvalidInputException(InvalidInputException exception) {
        QuestionsException questionsException = new QuestionsException(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(questionsException, questionsException.getHttpStatus());
    }
}