package com.example.questions.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseData {
    Status status;
    String message;
}
