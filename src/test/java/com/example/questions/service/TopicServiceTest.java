package com.example.questions.service;

import com.example.questions.exception.InvalidInputException;
import com.example.questions.model.Topic;
import com.example.questions.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.ConnectException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {
    @Mock
    private TopicRepository topicRepository;
    @InjectMocks
    private TopicServiceImpl topicService;
    @Mock
    private QuestionService questionService;
    private Topic topic;

    @BeforeEach
    void setUp() {
        topic = new Topic(null, "Java", null);
    }

    @Test
    public void shouldSuccessfullyAddTopic() throws Exception {
        Topic savedTopic = new Topic("1", "Java", 0);

        when(topicRepository.existsByNameIgnoreCase("Java")).thenReturn(false);
        when(topicRepository.insert(any(Topic.class))).thenReturn(savedTopic);

        Topic result = topicService.addTopic(topic);

        assertNotNull(result.getId());
        assertEquals("Java", result.getName());
        assertEquals(0, result.getNrOfQuestions());
        verify(topicRepository).insert(topic);
    }

    @Test
    public void addTopicShouldThrowExceptionForNullTopic() {
        assertThrows(InvalidInputException.class, () -> {
            topicService.addTopic(null);
        }, "Please provide a topic");
    }

    @Test
    public void addTopicShouldThrowExceptionForBlankTopicName() {
        Topic blankNameTopic = new Topic(null, " ", null);

        assertThrows(InvalidInputException.class, () -> {
            topicService.addTopic(blankNameTopic);
        }, "Please provide a name for the topic");
    }

    @Test
    public void addTopicShouldThrowExceptionForExistingTopicName() {
        when(topicRepository.existsByNameIgnoreCase("Java")).thenReturn(true);

        assertThrows(InvalidInputException.class, () -> {
            topicService.addTopic(topic);
        }, "A topic with that name already exists");
    }

    @Test
    public void addTopicShouldThrowExceptionForSettingNrOfQuestions() {
        Topic topicWithNrOfQuestions = new Topic(null, "Java", 10);

        assertThrows(InvalidInputException.class, () -> {
            topicService.addTopic(topicWithNrOfQuestions);
        }, "Setting the number of questions while creating the topic is not allowed");
    }

    @Test
    public void findAllShouldPopulateNrOfQuestions() throws Exception {
        List<Topic> topics = List.of(
                new Topic("1", "Spring Boot", null),
                new Topic("2", "Java", null)
        );

        when(topicRepository.findAll()).thenReturn(topics);
        when(questionService.countQuestionsByTopic("1")).thenReturn(5);
        when(questionService.countQuestionsByTopic("2")).thenReturn(10);

        List<Topic> result = topicService.findAll();

        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getNrOfQuestions());
        assertEquals(10, result.get(1).getNrOfQuestions());
        verify(questionService, times(1)).countQuestionsByTopic("1");
        verify(questionService, times(1)).countQuestionsByTopic("2");
    }

    @Test
    public void addTopicShouldThrowConnectExceptionOnGenericException() throws Exception {
        Topic validTopic = new Topic(null, "Java", null);

        doThrow(RuntimeException.class).when(topicRepository).insert(validTopic);

        assertThrows(ConnectException.class, () -> {
            topicService.addTopic(validTopic);
        }, "Failed to create the topic");
    }

    @Test
    public void findAll_ShouldThrowRuntimeException_OnException() throws Exception {
        List<Topic> topics = List.of(
                new Topic("1", "Spring Boot", null),
                new Topic("2", "Java", null)
        );

        when(topicRepository.findAll()).thenReturn(topics);
        when(questionService.countQuestionsByTopic("1")).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            topicService.findAll();
        });
    }

}