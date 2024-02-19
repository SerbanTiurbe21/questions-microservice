package com.example.questions.controller;

import com.example.questions.model.Topic;
import com.example.questions.service.TopicService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class TopicControllerTest {
    @Mock
    private TopicService topicService;
    @InjectMocks
    private TopicController topicController;
    private Topic topic;

    @BeforeEach
    public void setUp() {
        topic = new Topic("1", "Java", null);
    }

    @AfterEach
    public void tearDown() {
        topic = null;
        verifyNoMoreInteractions(topicService);
    }

    @Test
    public void addTopicShouldCallService() throws Exception {
        topicController.addTopic(topic);
        verify(topicService).addTopic(topic);
    }

    @Test
    public void displayTopicsShouldCallService() {
        topicController.displayTopics();
        verify(topicService).findAll();
    }
}
