package com.example.questions.service;

import com.example.questions.exception.BlankTextException;
import com.example.questions.exception.ImageTypeException;
import com.example.questions.validator.ValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidationServiceTest {
    @Mock
    private ValidationServiceImpl validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationServiceImpl();
    }

    @ParameterizedTest
    @ValueSource(strings = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE})
    void shouldBeAValidFileForSupportedContentTypes(String contentType) throws IOException, ImageTypeException {
        MultipartFile file = new MockMultipartFile("image", "image." + contentType.substring(contentType.lastIndexOf("/") + 1), contentType, new byte[]{1, 2, 3, 4});

        BufferedImage bufferedImageMock = mock(BufferedImage.class);
        when(bufferedImageMock.getWidth()).thenReturn(100);
        when(bufferedImageMock.getHeight()).thenReturn(100);

        try (MockedStatic<ImageIO> mockedImageIO = Mockito.mockStatic(ImageIO.class)) {
            mockedImageIO.when(() -> ImageIO.read(any(InputStream.class))).thenReturn(bufferedImageMock);

            byte[] imageBytes = validationService.isValidFile(file);
            assertNotNull(imageBytes);
        }
    }

    @Test
    void shouldThrowExceptionForCorruptedFileDueToException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getInputStream()).thenThrow(new IOException("Simulated IO Exception"));

        assertThrows(ImageTypeException.class, () -> validationService.isValidFile(file), "Image file is corrupted");
    }

    @Test
    void shouldReturnNullWhenFileIsNull() throws Exception {
        byte[] result = validationService.isValidFile(null);
        assertNull(result, "Method should return null when input file is null");
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
