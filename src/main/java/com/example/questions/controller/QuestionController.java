package com.example.questions.controller;

import com.example.questions.model.Question;
import com.example.questions.model.ResponseData;
import com.example.questions.model.Status;
import com.example.questions.model.Topic;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Questions", description = "Operations related to questions")
@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class QuestionController {
    private final QuestionService service;

    @Operation(summary = "Delete a question by id", description = "Return a response data object with status 200 if successful, or 404 if failed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Question deleted successfully", content = @Content(schema = @Schema(implementation = ResponseData.class))),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteQuestion(
            @Parameter(description = "ID of the question to be deleted", required = true) @PathVariable("id") String id) {
        ResponseData responseData = service.deleteQuestion(id);
        HttpStatus status = Status.SUCCESS.equals(responseData.getStatus()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(responseData);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Question created successfully", content = @Content(schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @Operation(summary = "Add a question", description = "Return a question object with status 201 if successful, or 400 if failed")
    @PostMapping()
    public ResponseEntity<Question> addQuestion(
            @Parameter(description = "Image file associated with the question") @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Parameter(description = "Text of the question", required = true) @RequestParam(value = "question") String question,
            @Parameter(description = "Answer to the question", required = true) @RequestParam(value = "answer") String answer,
            @Parameter(description = "List of topics the question is associated with", required = true) @RequestParam(value = "topics") List<Topic> topics) throws Exception {
        Question createdQuestion = service.createQuestion(imageFile, question, answer, topics);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Question.class)))),
            @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @Operation(summary = "Get all questions", description = "Return a list of question objects")
    @GetMapping("/{id}")
    public ResponseEntity<List<Question>> getQuestionsByTopicId(
            @Parameter(description = "ID of the topic to retrieve questions for", required = true) @PathVariable("id") String topicId) throws Exception {
        List<Question> questions = service.getQuestionsByTopicId(topicId);
        return ResponseEntity.ok(questions);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Question updated successfully", content = @Content(schema = @Schema(implementation = Question.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @Operation(summary = "Edit a question", description = "Return a question object with status 200 if successful, or 400 if failed")
    @PutMapping("/{id}")
    public ResponseEntity<Question> editQuestion(@Parameter(description = "ID of the question to be edited", required = true) @PathVariable("id") String id,
                                                 @Parameter(description = "New image file for the question") @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                                 @Parameter(description = "New text for the question") @RequestParam(value = "question", required = false) String question,
                                                 @Parameter(description = "New answer for the question") @RequestParam(value = "answer", required = false) String answer,
                                                 @Parameter(description = "New list of topics the question is associated with") @RequestParam(value = "topics", required = false) List<Topic> topics) throws Exception {
        Question updatedQuestion = service.updateQuestion(id, imageFile, question, answer, topics);
        return ResponseEntity.ok(updatedQuestion);
    }
}
