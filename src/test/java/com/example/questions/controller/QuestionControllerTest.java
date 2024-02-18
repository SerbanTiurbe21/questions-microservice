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
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {
    @Mock
    private QuestionService questionService;
    @InjectMocks
    private QuestionController questionController;
    private Question question;
    private MockMultipartFile file;
    private ResponseData responseData;

    @BeforeEach
    public void setUp() {
        byte[] imageBytes = "dummy image data".getBytes();
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("1", "Java", null));
        topics.add(new Topic("2", "Spring", null));
        question = new Question("Sample question text", "Sample answer text", topics, imageBytes);
        responseData = new ResponseData(Status.SUCCESS, "Test message");
        file = new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());
    }

    @AfterEach
    public void tearDown() {
        question = null;
        file = null;
        responseData = null;
        verifyNoMoreInteractions(questionService);
    }

    @Test
    public void deleteQuestionShouldCallService() {
        final String questionId = "1";
        when(questionService.deleteQuestion(questionId)).thenReturn(responseData);
        ResponseEntity<ResponseData> result = questionController.deleteQuestion(questionId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(questionService).deleteQuestion(questionId);
    }

    @Test
    public void addQuestionShouldCallService() throws Exception {
        when(questionService.createQuestion(file, question.getQuestion(), question.getAnswer(), question.getTopics())).thenReturn(question);
        Question result = questionController.addQuestion(file, question.getQuestion(), question.getAnswer(), question.getTopics()).getBody();
        assertEquals(question, result);
        verify(questionService).createQuestion(file, question.getQuestion(), question.getAnswer(), question.getTopics());
    }

    @Test
    public void getQuestionByIdShouldCallService() throws Exception {
        final String topicId = "1";
        List<Question> questions = new ArrayList<>();
        questions.add(question);
        when(questionService.getQuestionsByTopicId(topicId)).thenReturn(questions);
        List<Question> result = questionController.getQuestionsById(topicId).getBody();
        assertEquals(questions, result);
        verify(questionService).getQuestionsByTopicId(topicId);
    }

    @Test
    public void editQuestionShouldCallService() throws Exception {
        final String questionId = "1";
        when(questionService.updateQuestion(questionId, file, question.getQuestion(), question.getAnswer(), question.getTopics())).thenReturn(question);
        Question result = questionController.editQuestion(questionId, file, question.getQuestion(), question.getAnswer(), question.getTopics()).getBody();
        assertEquals(question, result);
        verify(questionService).updateQuestion(questionId, file, question.getQuestion(), question.getAnswer(), question.getTopics());
    }
}
