package com.example.questions.service;

import com.example.questions.exception.InvalidInputException;
import com.example.questions.model.Topic;
import com.example.questions.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final QuestionService questionService;

    @Override
    public Topic addTopic(Topic topic) throws Exception {
        try {
            verifyTopic(topic);
            topic.setNrOfQuestions(0);
            return topicRepository.insert(topic);
        } catch (InvalidInputException e) {
            throw new InvalidInputException(e.getMessage());
        } catch (Exception e) {
            throw new ConnectException("Failed to create the topic");
        }
    }

    @Override
    public List<Topic> findAll() {
        List<Topic> topics = topicRepository.findAll();

        topics.forEach(topic -> {
            try {
                topic.setNrOfQuestions(questionService.countQuestionsByTopic(topic.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return topics;
    }

    private void verifyTopic(Topic topic) throws InvalidInputException {
        if (topic == null) {
            throw new InvalidInputException("Please provide a topic");
        }

        if (topic.getName() == null || topic.getName().isBlank()) {
            throw new InvalidInputException("Please provide a name for the topic");
        }

        if (topic.getNrOfQuestions() != null) {
            throw new InvalidInputException("Setting the number of questions while creating the topic is not allowed");
        }

        if (topicRepository.existsByNameIgnoreCase(topic.getName())) {
            throw new InvalidInputException("A topic with that name already exists");
        }
    }
}
