package com.example.questions.validator;

import com.example.questions.exception.BlankTextException;
import org.springframework.stereotype.Component;

@Component
public class ValidationServiceImpl implements ValidationService {
    @Override
    public void isValidText(String text) throws BlankTextException {
        if (!isNotEmptyText(text)) throw new BlankTextException("Complete required fields");
    }

    private boolean isNotEmptyText(String text) {
        if (text == null || text.trim().equalsIgnoreCase("null")) return false;
        return !text.isBlank();
    }
}
