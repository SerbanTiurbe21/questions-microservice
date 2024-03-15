package com.example.questions.validator;

import com.example.questions.exception.BlankTextException;

public interface ValidationService {
    void isValidText(String text) throws BlankTextException;
}
