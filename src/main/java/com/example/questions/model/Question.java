package com.example.questions.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "questions")
public class Question {
    @Id
    private String id;
    @Field(name = "question")
    private String question;
    @Field(name = "answer")
    private String answer;
    @DBRef
    private List<Topic> topics;
    private byte[] image;
    public Question(String question, String answer, List<Topic> topics, byte[] image) {
        this.question = question;
        this.answer = answer;
        this.topics = topics;
        this.image = image;
    }
}
