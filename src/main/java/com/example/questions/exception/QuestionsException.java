package com.example.questions.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class QuestionsException {
    private final String message;
    private final HttpStatus httpStatus;
}
