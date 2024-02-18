package com.example.questions.validator;

import com.example.questions.exception.BlankTextException;
import com.example.questions.exception.ImageTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
public class ValidationServiceImpl implements ValidationService {
    @Override
    public byte[] isValidFile(MultipartFile file) throws ImageTypeException, IOException {
        if (file == null) {
            return null;
        } else {
            String imageType = file.getContentType();
            if (imageType == null) throw new ImageTypeException("Image file is empty");
            if (!isSupportedContentType(imageType)) throw new ImageTypeException("Invalid file type");
            if (!isNotCorruptContent(file)) throw new ImageTypeException("Image file is corrupted");
            if (isNotCorruptContent(file)) isSupportedContentType(imageType);

            return file.getBytes();
        }
    }

    @Override
    public void isValidText(String text) throws BlankTextException {
        if (!isNotEmptyText(text)) throw new BlankTextException("Complete required fields");
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equalsIgnoreCase(MediaType.IMAGE_PNG.toString())
                || contentType.equalsIgnoreCase(MediaType.IMAGE_JPEG.toString())
                || contentType.equalsIgnoreCase(MediaType.IMAGE_GIF.toString());
    }

    private boolean isNotCorruptContent(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            return image != null && image.getWidth() > 0 && image.getHeight() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isNotEmptyText(String text) {
        if (text == null || text.trim().equalsIgnoreCase("null")) return false;
        return !text.isBlank();
    }
}
