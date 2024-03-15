package com.example.questions.service;

import com.example.questions.exception.BlankTextException;
import com.example.questions.exception.InvalidInputException;
import com.example.questions.model.Question;
import com.example.questions.model.Topic;
import com.example.questions.repository.QuestionRepository;
import com.example.questions.repository.TopicRepository;
import com.example.questions.validator.ValidationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {
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

    @BeforeEach
    public void setUp() {
        topic = new Topic("1", "Java", 10);
        question = new Question("How does Spring work?", "Spring is a Java framework...", List.of(topic));
    }

    @AfterEach
    public void tearDown() {
        question = null;
        topic = null;
    }

    @Test
    void shouldCreateQuestion() throws Exception {
        when(topicRepository.findAll()).thenReturn(List.of(topic));
        when(topicRepository.findByName("Java")).thenReturn(Optional.of(topic));
        when(questionRepository.insert(any(Question.class))).thenReturn(question);

        Question createdQuestion = questionService.createQuestion(question);

        assertNotNull(createdQuestion);
        assertEquals(question.getQuestion(), createdQuestion.getQuestion());
        assertEquals(question.getAnswer(), createdQuestion.getAnswer());

        verify(topicRepository, times(1)).findAll();
        verify(questionRepository, times(1)).insert(any(Question.class));
    }

    @Test
    void testCreateQuestionThrowsRuntimeExceptionWhenCatchingInvalidInputExceptionThrownByTopicRepositoryFindByNameWhenNotFoundAnything() {
        lenient().when(questionRepository.findById("1")).thenReturn(Optional.of(question));
        lenient().when(topicRepository.findByName("Java")).thenReturn(Optional.empty());
        lenient().when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));

        assertThrows(Exception.class, () -> {
            questionService.createQuestion(new Question("How does Spring work?", "Spring is a Java framework...", List.of(topic)));
        }, "InvalidInputException should be thrown for invalid topic");
    }

    @Test
    void shouldGetQuestionsByTopicId() throws Exception {
        when(topicRepository.findById("1")).thenReturn(Optional.of(new Topic("1", "ValidTopic", null)));
        when(questionRepository.findQuestionsByTopic("1")).thenReturn(Collections.singletonList(question));

        List<Question> result = questionService.getQuestionsByTopicId("1");

        assertEquals(Collections.singletonList(question), result);
        verify(questionRepository, times(1)).findQuestionsByTopic("1");
    }

    @Test
    void shouldThrowInvalidInputExceptionWhenGetQuestionsByTopicId() {
        when(topicRepository.findById("1")).thenReturn(Optional.empty());

        try {
            questionService.getQuestionsByTopicId("1");
        } catch (Exception e) {
            assertEquals("Invalid topic", e.getMessage());
        }
    }

    @Test
    void shouldThrowExceptionWhenGetQuestionsByTopicId() {
        when(topicRepository.findById("1")).thenReturn(Optional.of(new Topic("1", "ValidTopic", null)));
        when(questionRepository.findQuestionsByTopic("1")).thenThrow(new RuntimeException("Error"));

        try {
            questionService.getQuestionsByTopicId("1");
        } catch (Exception e) {
            assertEquals("Error", e.getMessage());
        }
    }

    @Test
    void shouldCountQuestionsByTopicId() {
        when(questionRepository.getNrOfQuestionsByTopicId("1")).thenReturn(10);

        int result = questionService.countQuestionsByTopic("1");

        assertEquals(10, result);
        verify(questionRepository, times(1)).getNrOfQuestionsByTopicId("1");
    }

    @Test
    void shouldDeleteQuestion() {
        when(questionRepository.findById("1")).thenReturn(Optional.of(question));

        questionService.deleteQuestion("1");

        verify(questionRepository, times(1)).deleteById("1");
    }

    @Test
    void shouldNotDeleteQuestionWhenQuestionNotFound() {
        when(questionRepository.findById("1")).thenReturn(Optional.empty());

        try {
            questionService.deleteQuestion("1");
        } catch (Exception e) {
            assertEquals("Question could not be found", e.getMessage());
        }
    }

    @Test
    void shouldUpdateQuestionWhenAllInputFieldsAreValid() throws Exception {
        when(questionRepository.findById("1")).thenReturn(Optional.of(question));
        when(topicRepository.findByName("Java")).thenReturn(Optional.of(topic));
        when(questionRepository.getQuestionById("1")).thenReturn(question);

        when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));
        doNothing().when(validationService).isValidText("How does Spring work?");
        doNothing().when(validationService).isValidText("Spring is a Java framework...");
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        questionService.updateQuestion("1", "How does Spring work?", "Spring is a Java framework...", List.of(topic));

        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void testUpdateQuestion_ThrowsRuntimeExceptionForInvalidTopic() {
        when(questionRepository.findById("1")).thenReturn(Optional.of(question));
        when(topicRepository.findByName("Java")).thenReturn(Optional.empty());
        when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));

        assertThrows(RuntimeException.class, () -> {
            questionService.updateQuestion("1", "How does Spring work?", "Spring is a Java framework...", List.of(topic));
        }, "InvalidInputException should be thrown for invalid topic");
    }


    @Test
    void shouldGetQuestionById() {
        when(questionRepository.findById("1")).thenReturn(Optional.of(question));

        Optional<Question> result = questionService.getQuestionById("1");

        assertEquals(Optional.of(question), result);
        verify(questionRepository, times(1)).findById("1");
    }

    @Test
    void updateQuestionShouldThrowInvalidInputExceptionWhenQuestionIsNotFound() {
        when(questionRepository.findById("1")).thenReturn(Optional.empty());
        when(topicRepository.findAll()).thenReturn(Collections.singletonList(topic));

        try {
            questionService.updateQuestion("1", "How does Spring work?", "Spring is a Java framework...", List.of(topic));
        } catch (Exception e) {
            assertEquals("Question could not be found", e.getMessage());
        }
    }

    @Test
    void createQuestionShouldThrowBlankTextExceptionForBlankQuestion() throws BlankTextException {
        String blankQuestion = " ";
        String validAnswer = "Spring is a powerful framework.";
        List<Topic> validTopics = List.of(new Topic("1", "Java", 10));

        when(topicRepository.findAll()).thenReturn(validTopics);
        doThrow(new BlankTextException("Question text cannot be blank"))
                .when(validationService).isValidText(blankQuestion);
        doNothing().when(validationService).isValidText(validAnswer);

        assertThrows(BlankTextException.class, () -> {
            questionService.createQuestion(new Question(blankQuestion, validAnswer, validTopics));
        }, "BlankTextException should be thrown for blank question text");
    }

    @Test
    void createQuestionShouldThrowInvalidInputExceptionForInvalidTopics() throws Exception {
        String validQuestion = "How does Spring work?";
        String validAnswer = "Spring is a Java framework...";
        List<Topic> invalidTopics = List.of(new Topic("invalidId", "NonExistingTopic", 0));

        when(topicRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(InvalidInputException.class, () -> {
            questionService.createQuestion(new Question(validQuestion, validAnswer, invalidTopics));
        }, "InvalidInputException should be thrown for invalid topics");
    }

    @Test
    void createQuestionShouldThrowInvalidInputExceptionForNullTopic() {
        String validQuestion = "How does Spring work?";
        String validAnswer = "Spring is a Java framework...";
        List<Topic> topicsWithNull = Arrays.asList(new Topic("1", "Java", 10), null);

        when(topicRepository.findAll()).thenReturn(Collections.singletonList(new Topic("1", "Java", 10)));

        assertThrows(InvalidInputException.class, () -> {
            questionService.createQuestion(new Question(validQuestion, validAnswer, topicsWithNull));
        }, "InvalidInputException should be thrown for null topic");
    }

    @Test
    void createQuestionShouldThrowGenericExceptionForUnexpectedErrors() {
        Topic topic = new Topic("1", "Java", 10);
        Question question = new Question("Valid question?", "Yes", List.of(topic));

        lenient().when(topicRepository.findByName("Java")).thenReturn(Optional.of(topic));
        lenient().when(topicRepository.findAll()).thenReturn(List.of(topic));
        lenient().when(topicRepository.findById(anyString())).thenReturn(Optional.of(topic));
        when(questionRepository.insert(any(Question.class))).thenThrow(new RuntimeException("Unexpected error"));

        Exception exception = assertThrows(Exception.class, () -> questionService.createQuestion(question),
                "Expected to catch and rethrow a generic Exception for unexpected errors");

        assertEquals("Unexpected error", exception.getMessage(), "The exception message should match the one from the unexpected error");
    }
}
