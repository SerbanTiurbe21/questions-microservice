package com.example.questions.service;

import com.example.questions.exception.BlankTextException;
import com.example.questions.exception.ImageTypeException;
import com.example.questions.exception.InvalidInputException;
import com.example.questions.model.Question;
import com.example.questions.model.Topic;
import com.example.questions.repository.QuestionRepository;
import com.example.questions.repository.TopicRepository;
import com.example.questions.validator.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private ValidationService validationService;
    @InjectMocks
    private QuestionServiceImpl questionService;
    private Question question;
    private Topic topic;
    private MockMultipartFile imageFile;

    @BeforeEach
    public void setUp() {
        topic = new Topic("1", "Java", 10);
        question = new Question("How does Spring work?", "Spring is a Java framework...", List.of(topic), new byte[0]);
        imageFile = new MockMultipartFile("image", "test.png", "image/png", new byte[0]);
    }

    @Test
    public void shouldCreateQuestion() throws Exception {
        when(validationService.isValidFile(imageFile)).thenReturn(new byte[0]);
        when(questionRepository.insert(question)).thenReturn(question);
        when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));

        Question result = questionService.createQuestion(imageFile, "How does Spring work?", "Spring is a Java framework...", List.of(topic));

        assertEquals(question, result);
        verify(validationService, times(1)).isValidFile(imageFile);
        verify(questionRepository, times(1)).insert(question);
        verify(topicRepository, times(1)).findAll();
    }

    @Test
    public void shouldThrowImageTypeExceptionWhenCreateQuestion() throws Exception {
        when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));
        when(validationService.isValidFile(imageFile)).thenThrow(new ImageTypeException("Invalid file type"));

        try {
            questionService.createQuestion(imageFile, "How does Spring work?", "Spring is a Java framework...", List.of(topic));
        } catch (ImageTypeException e) {
            assertEquals("Invalid file type", e.getMessage());
        }
    }

    @Test
    public void shouldGetQuestionsByTopicId() throws Exception {
        when(topicRepository.findById("1")).thenReturn(Optional.of(new Topic("1", "ValidTopic", null)));
        when(questionRepository.findQuestionsByTopic("1")).thenReturn(Collections.singletonList(question));

        List<Question> result = questionService.getQuestionsByTopicId("1");

        assertEquals(Collections.singletonList(question), result);
        verify(questionRepository, times(1)).findQuestionsByTopic("1");
    }

    @Test
    public void shouldThrowInvalidInputExceptionWhenGetQuestionsByTopicId() {
        when(topicRepository.findById("1")).thenReturn(Optional.empty());

        try {
            questionService.getQuestionsByTopicId("1");
        } catch (Exception e) {
            assertEquals("Invalid topic", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWhenGetQuestionsByTopicId() {
        when(topicRepository.findById("1")).thenReturn(Optional.of(new Topic("1", "ValidTopic", null)));
        when(questionRepository.findQuestionsByTopic("1")).thenThrow(new RuntimeException("Error"));

        try {
            questionService.getQuestionsByTopicId("1");
        } catch (Exception e) {
            assertEquals("Error", e.getMessage());
        }
    }

    @Test
    public void shouldCountQuestionsByTopicId() throws Exception {
        when(questionRepository.getNrOfQuestionsByTopicId("1")).thenReturn(10);

        int result = questionService.countQuestionsByTopic("1");

        assertEquals(10, result);
        verify(questionRepository, times(1)).getNrOfQuestionsByTopicId("1");
    }

    @Test
    public void shouldThrowExceptionWhenCountQuestionsByTopicId() {
        when(questionRepository.getNrOfQuestionsByTopicId("1")).thenThrow(new RuntimeException("Error"));

        try {
            questionService.countQuestionsByTopic("1");
        } catch (Exception e) {
            assertEquals("Error", e.getMessage());
        }
    }

    @Test
    public void shouldDeleteQuestion() throws Exception {
        when(questionRepository.findById("1")).thenReturn(Optional.of(question));

        questionService.deleteQuestion("1");

        verify(questionRepository, times(1)).deleteById("1");
    }

    @Test
    public void shouldNotDeleteQuestionWhenQuestionNotFound() {
        when(questionRepository.findById("1")).thenReturn(Optional.empty());

        try {
            questionService.deleteQuestion("1");
        } catch (Exception e) {
            assertEquals("Question could not be found", e.getMessage());
        }
    }

    @Test
    public void shouldUpdateQuestionWhenAllInputFieldsAreValid() throws Exception {
        when(questionRepository.findById("1")).thenReturn(Optional.of(question));
        when(questionRepository.getQuestionById("1")).thenReturn(question);

        when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));
        when(validationService.isValidFile(imageFile)).thenReturn(new byte[0]);
        doNothing().when(validationService).isValidText("How does Spring work?");
        doNothing().when(validationService).isValidText("Spring is a Java framework...");
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        questionService.updateQuestion("1", imageFile, "How does Spring work?", "Spring is a Java framework...", List.of(topic));

        verify(questionRepository, times(1)).save(question);
    }

    @Test
    public void shouldThrowImageTypeExceptionWhenUpdateQuestion() throws Exception {
        when(questionRepository.findById("1")).thenReturn(Optional.of(question));
        when(questionRepository.getQuestionById("1")).thenReturn(question);
        when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));
        when(validationService.isValidFile(imageFile)).thenThrow(new ImageTypeException("Invalid file type"));

        try {
            questionService.updateQuestion("1", imageFile, "How does Spring work?", "Spring is a Java framework...", List.of(topic));
        } catch (ImageTypeException e) {
            assertEquals("Invalid file type", e.getMessage());
        }
    }

    @Test
    public void shouldGetQuestionById() {
        when(questionRepository.findById("1")).thenReturn(Optional.of(question));

        Optional<Question> result = questionService.getQuestionById("1");

        assertEquals(Optional.of(question), result);
        verify(questionRepository, times(1)).findById("1");
    }

    @Test
    public void updateQuestionShouldThrowInvalidInputExceptionWhenQuestionIsNotFound() {
        when(questionRepository.findById("1")).thenReturn(Optional.empty());
        when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));

        try {
            questionService.updateQuestion("1", imageFile, "How does Spring work?", "Spring is a Java framework...", List.of(topic));
        } catch (Exception e) {
            assertEquals("Question could not be found", e.getMessage());
        }
    }

    @Test
    public void createQuestionShouldThrowBlankTextExceptionForBlankQuestion() throws BlankTextException {
        String blankQuestion = " ";
        String validAnswer = "Spring is a powerful framework.";
        List<Topic> validTopics = List.of(new Topic("1", "Java", 10));
        MultipartFile validImageFile = new MockMultipartFile("image", "test.png", "image/png", new byte[0]);

        when(topicRepository.findAll()).thenReturn(validTopics);
        doThrow(new BlankTextException("Question text cannot be blank"))
                .when(validationService).isValidText(blankQuestion);
        doNothing().when(validationService).isValidText(validAnswer);

        assertThrows(BlankTextException.class, () -> {
            questionService.createQuestion(validImageFile, blankQuestion, validAnswer, validTopics);
        }, "BlankTextException should be thrown for blank question text");
    }

    @Test
    public void createQuestionShouldThrowInvalidInputExceptionForInvalidTopics() throws Exception {
        String validQuestion = "How does Spring work?";
        String validAnswer = "Spring is a Java framework...";
        List<Topic> invalidTopics = List.of(new Topic("invalidId", "NonExistingTopic", 0));
        MultipartFile validImageFile = new MockMultipartFile("image", "test.png", "image/png", new byte[0]);

        when(topicRepository.findAll()).thenReturn(Collections.emptyList());
        lenient().when(validationService.isValidFile(validImageFile)).thenReturn(new byte[0]);

        assertThrows(InvalidInputException.class, () -> {
            questionService.createQuestion(validImageFile, validQuestion, validAnswer, invalidTopics);
        }, "InvalidInputException should be thrown for invalid topics");
    }

    @Test
    public void createQuestionShouldThrowInvalidInputExceptionForNullTopic() {
        String validQuestion = "How does Spring work?";
        String validAnswer = "Spring is a Java framework...";
        List<Topic> topicsWithNull = Arrays.asList(new Topic("1", "Java", 10), null);
        MultipartFile validImageFile = new MockMultipartFile("image", "test.png", "image/png", new byte[0]);

        when(topicRepository.findAll()).thenReturn(Collections.singletonList(new Topic("1", "Java", 10)));

        assertThrows(InvalidInputException.class, () -> {
            questionService.createQuestion(validImageFile, validQuestion, validAnswer, topicsWithNull);
        }, "InvalidInputException should be thrown for null topic");
    }

}
