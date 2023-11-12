package com.example.questions.service;

import com.example.questions.model.Question;
import com.example.questions.model.ResponseData;
import com.example.questions.model.Topic;
import com.example.questions.repository.QuestionRepository;
import com.example.questions.repository.TopicRepository;
import com.example.questions.validator.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;
    private final ValidationService validationService;

    public QuestionServiceImpl(QuestionRepository questionRepository, TopicRepository topicRepository, ValidationService validationService) {
        this.questionRepository = questionRepository;
        this.topicRepository = topicRepository;
        this.validationService = validationService;
    }

    @Override
    public Question createQuestion(MultipartFile imageFile, String question, String answer, List<Topic> topics) throws Exception {
        return null;
    }

    @Override
    public List<Question> getQuestionsByTopicId(String topicId) throws Exception {
        return null;
    }

    @Override
    public int countQuestionsByTopic(String topicId) throws Exception {
        return 0;
    }

    @Override
    public ResponseData deleteQuestion(String id) {
        return null;
    }

    @Override
    public Optional<Question> getQuestionById(String id) {
        return Optional.empty();
    }

    @Override
    public Question updateQuestion(String id, MultipartFile imageFile, String question, String answer, List<Topic> topics) throws Exception {
        return null;
    }
}
