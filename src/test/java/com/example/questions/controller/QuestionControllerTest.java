package com.example.questions.controller;

import com.example.questions.model.Question;
import com.example.questions.model.ResponseData;
import com.example.questions.model.Status;
import com.example.questions.model.Topic;
import com.example.questions.service.QuestionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {
    @Mock
    private QuestionService questionService;
    @InjectMocks
    private QuestionController questionController;
    private Question question;
    private ResponseData responseData;

    @BeforeEach
    public void setUp() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("1", "Java", null));
        topics.add(new Topic("2", "Spring", null));
        question = new Question("Sample question text", "Sample answer text", topics);
        responseData = new ResponseData(Status.SUCCESS, "Test message");
    }

    @AfterEach
    public void tearDown() {
        question = null;
        responseData = null;
        verifyNoMoreInteractions(questionService);
    }

    @Test
    void deleteQuestionShouldCallService() {
        final String questionId = "1";
        when(questionService.deleteQuestion(questionId)).thenReturn(responseData);
        ResponseEntity<ResponseData> result = questionController.deleteQuestion(questionId).block();
        assert result != null;
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(questionService).deleteQuestion(questionId);
    }

    @Test
    void addQuestionShouldCallService() throws Exception {
        when(questionService.createQuestion(question)).thenReturn(question);
        Question result = questionController.addQuestion(question).block().getBody();
        assertEquals(question, result);
        verify(questionService).createQuestion(question);
    }

    @Test
    void getQuestionsByTopicIdShouldCallService() throws Exception {
        final String topicId = "1";
        List<Question> questions = new ArrayList<>();
        questions.add(question);
        when(questionService.getQuestionsByTopicId(topicId)).thenReturn(questions);
        ResponseEntity<List<Question>> result = questionController.getQuestionsByTopicId(topicId).block();
        assert result != null;
        assertEquals(questions, result.getBody());
        verify(questionService).getQuestionsByTopicId(topicId);
    }

    @Test
    void editQuestionShouldCallService() throws Exception {
        final String questionId = "1";
        when(questionService.updateQuestion(questionId, question.getQuestion(), question.getAnswer(), question.getTopics())).thenReturn(question);
        ResponseEntity<Question> result = questionController.editQuestion(questionId, question).block();
        assert result != null;
        assertEquals(question, result.getBody());
        verify(questionService).updateQuestion(questionId, question.getQuestion(), question.getAnswer(), question.getTopics());
    }
}
