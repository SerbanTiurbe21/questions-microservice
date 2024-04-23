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
class TopicControllerTest {
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
    void addTopicShouldCallService() throws Exception {
        topicController.addTopic(topic).block();
        verify(topicService).addTopic(topic);
    }

    @Test
    void displayTopicsShouldCallService() {
        topicController.displayTopics().block();
        verify(topicService).findAll();
    }

    @Test
    void getTopicByIdShouldCallService() {
        topicController.getTopicById("1").block();
        verify(topicService).findById("1");
    }
}
