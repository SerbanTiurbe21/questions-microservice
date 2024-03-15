package com.example.questions.service;

import com.example.questions.model.Question;
import com.example.questions.model.ResponseData;
import com.example.questions.model.Topic;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    Question createQuestion(Question question) throws Exception;

    List<Question> getQuestionsByTopicId(String topicId) throws Exception;

    int countQuestionsByTopic(String topicId) throws Exception;

    ResponseData deleteQuestion(String id);

    Optional<Question> getQuestionById(String id);

    Question updateQuestion(String id, String question, String answer, List<Topic> topics) throws Exception;
}
