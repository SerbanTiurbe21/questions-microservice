package com.example.questions.service;

import com.example.questions.model.Topic;

import java.util.List;

public interface TopicService {
    Topic addTopic(Topic topic) throws Exception;

    List<Topic> findAll();
    Topic findById(String id);
}
