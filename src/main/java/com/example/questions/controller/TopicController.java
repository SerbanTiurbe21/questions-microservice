package com.example.questions.controller;

import com.example.questions.model.Topic;
import com.example.questions.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Topics", description = "Operations related to topics")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/topics")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class TopicController {
    private final TopicService topicService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Topic added successfully", content = @Content(schema = @Schema(implementation = Topic.class))),
            @ApiResponse(responseCode = "400", description = "Failed to add topic")
    })
    @Operation(summary = "Add a topic", description = "Return a topic object with status 201 if successful, or 400 if failed")
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer')")
    @PostMapping()
    public Mono<ResponseEntity<Topic>> addTopic(@Parameter(description = "Topic object to be added to the database") @RequestBody Topic topic) throws Exception {
        Topic createdTopic = topicService.addTopic(topic);
        return Mono.just(new ResponseEntity<>(createdTopic, HttpStatus.CREATED));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Topic retrieved successfully", content = @Content(schema = @Schema(implementation = Topic.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Operation(summary = "Get all topics", description = "Return a list of topic objects")
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer')")
    @GetMapping()
    public Mono<ResponseEntity<List<Topic>>> displayTopics() {
        List<Topic> topics = topicService.findAll();
        return Mono.just(new ResponseEntity<>(topics, HttpStatus.OK));
    }
}
