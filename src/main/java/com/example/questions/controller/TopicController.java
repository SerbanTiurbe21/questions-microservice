package com.example.questions.controller;

import com.example.questions.model.Topic;
import com.example.questions.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/topics")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class TopicController {
    private final TopicService topicService;

    @PostMapping()
    public ResponseEntity<Topic> addTopic(@RequestBody Topic topic) throws Exception {
        Topic createdTopic = topicService.addTopic(topic);
        return new ResponseEntity<>(createdTopic, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Topic>> displayTopics() {
        return new ResponseEntity<>(topicService.findAll(), HttpStatus.OK);
    }
}
