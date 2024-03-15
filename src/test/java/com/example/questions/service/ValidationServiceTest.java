package com.example.questions.service;

import com.example.questions.exception.BlankTextException;
import com.example.questions.validator.ValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationServiceTest {
    @Mock
    private ValidationServiceImpl validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationServiceImpl();
    }

    @Test
    void shouldThrowExceptionForNullText() {
        assertThrows(BlankTextException.class, () -> validationService.isValidText(null));
    }

    @Test
    void shouldThrowExceptionForEmptyText() {
        assertThrows(BlankTextException.class, () -> validationService.isValidText(""));
    }

    @Test
    void shouldThrowExceptionForWhitespaceText() {
        assertThrows(BlankTextException.class, () -> validationService.isValidText("   "));
    }

    @Test
    void shouldThrowExceptionForTextEqualsNullString() {
        assertThrows(BlankTextException.class, () -> validationService.isValidText("null"));
    }

    @Test
    void shouldThrowExceptionForTextEqualsNullStringCaseInsensitive() {
        assertThrows(BlankTextException.class, () -> validationService.isValidText("NuLl"));
    }

    @Test
    void shouldNotThrowExceptionForValidText() {
        assertDoesNotThrow(() -> validationService.isValidText("Valid text"));
    }
}
