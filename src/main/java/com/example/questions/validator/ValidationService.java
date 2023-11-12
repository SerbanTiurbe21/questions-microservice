package com.example.questions.validator;

import com.example.questions.exception.BlankTextException;
import com.example.questions.exception.ImageTypeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ValidationService {
    byte[] isValidFile(MultipartFile file) throws IOException, ImageTypeException;

    void isValidText(String text) throws BlankTextException;
}
