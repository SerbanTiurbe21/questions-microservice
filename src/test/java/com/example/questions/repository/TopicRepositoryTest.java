package com.example.questions.repository;

import com.example.questions.model.Topic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class TopicRepositoryTest {
    @Autowired
    private TopicRepository topicRepository;
    private Topic topic1, topic2;

    @BeforeEach
    public void setUp() {
        topic1 = new Topic(null, "Java", null);
        topic2 = new Topic(null, "Spring", null);
        topicRepository.saveAll(List.of(topic1, topic2));
    }

    @AfterEach
    public void tearDown() {
        topicRepository.delete(topic1);
        topicRepository.delete(topic2);
    }

    @Test
    void whenTopicExists_thenReturnTrue() {
        boolean exists = topicRepository.existsByNameIgnoreCase("java");
        assertTrue(exists);
    }

    @Test
    void whenTopicDoesNotExist_thenReturnFalse() {
        boolean exists = topicRepository.existsByNameIgnoreCase("Python");
        assertFalse(exists);
    }
}
