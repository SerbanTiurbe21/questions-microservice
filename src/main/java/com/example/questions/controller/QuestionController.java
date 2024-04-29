package com.example.questions.controller;

import com.example.questions.model.Question;
import com.example.questions.model.ResponseData;
import com.example.questions.model.Status;
import com.example.questions.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

@Tag(name = "Questions", description = "Operations related to questions")
@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService service;

    @Operation(summary = "Delete a question by id", description = "Return a response data object with status 200 if successful, or 404 if failed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Question deleted successfully", content = @Content(schema = @Schema(implementation = ResponseData.class))),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer') or hasRole('ROLE_client-admin')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ResponseData>> deleteQuestion(
            @Parameter(description = "ID of the question to be deleted", required = true) @PathVariable("id") String id) {
        ResponseData responseData = service.deleteQuestion(id);
        HttpStatus status = Status.SUCCESS.equals(responseData.getStatus()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return Mono.just(ResponseEntity.status(status).body(responseData));
    }

    @Operation(summary = "Add a question", description = "Return a question object with status 201 if successful, or 400 if failed")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Question created successfully", content = @Content(schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer') or hasRole('ROLE_client-admin')")
    @PostMapping()
    public Mono<ResponseEntity<Question>> addQuestion(@RequestBody Question question) throws Exception {
        Question createdQuestion = service.createQuestion(question);
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Question.class)))),
            @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @Operation(summary = "Get all questions", description = "Return a list of question objects")
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer') or hasRole('ROLE_client-admin')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<List<Question>>> getQuestionsByTopicId(
            @Parameter(description = "ID of the topic to retrieve questions for", required = true) @PathVariable("id") String topicId) throws Exception {
        List<Question> questions = service.getQuestionsByTopicId(topicId);
        return Mono.just(ResponseEntity.ok(questions));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Question updated successfully", content = @Content(schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @Operation(summary = "Edit a question", description = "Return a question object with status 200 if successful, or 400 if failed")
    @PreAuthorize("hasRole('ROLE_client-hr') or hasRole('ROLE_client-developer') or hasRole('ROLE_client-admin')")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Question>> editQuestion(@Parameter(description = "ID of the question to be edited", required = true) @PathVariable("id") String id,
                                                       @RequestBody Question question) throws Exception {
        Question updatedQuestion = service.updateQuestion(id, question.getQuestion(), question.getAnswer(), question.getTopics());
        return Mono.just(ResponseEntity.ok(updatedQuestion));
    }
}
