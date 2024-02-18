package com.example.questions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "topics")
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    @Transient
    private Integer nrOfQuestions;
}
