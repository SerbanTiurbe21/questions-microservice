package com.example.questions.controller;

import com.example.questions.model.Topic;
import com.example.questions.service.TopicServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {

    private final TopicServiceImpl topicServiceImpl;

    TopicController(TopicServiceImpl topicServiceImpl) {
        this.topicServiceImpl = topicServiceImpl;
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public Topic addTopic(@RequestBody Topic topic) throws Exception {
        return topicServiceImpl.addTopic(topic);
    }

    @GetMapping()
    public List<Topic> displayTopics() {
        return topicServiceImpl.findAll();
    }

}
