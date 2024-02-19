package com.example.questions.controller;

import com.example.questions.model.Question;
import com.example.questions.model.ResponseData;
import com.example.questions.model.Status;
import com.example.questions.model.Topic;
import com.example.questions.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class QuestionController {
    private final QuestionService service;

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteQuestion(@PathVariable("id") String id) {
        ResponseData responseData = service.deleteQuestion(id);
        HttpStatus status = Status.SUCCESS.equals(responseData.getStatus()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(responseData);
    }

    @PostMapping()
    public ResponseEntity<Question> addQuestion(@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                                @RequestParam(value = "question") String question,
                                                @RequestParam(value = "answer") String answer,
                                                @RequestParam(value = "topics") List<Topic> topics) throws Exception {
        Question createdQuestion = service.createQuestion(imageFile, question, answer, topics);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Question>> getQuestionsByTopicId(@PathVariable("id") String topicId) throws Exception {
        List<Question> questions = service.getQuestionsByTopicId(topicId);
        return ResponseEntity.ok(questions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> editQuestion(@PathVariable("id") String id,
                                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                                 @RequestParam(value = "question", required = false) String question,
                                                 @RequestParam(value = "answer", required = false) String answer,
                                                 @RequestParam(value = "topics", required = false) List<Topic> topics) throws Exception {
        Question updatedQuestion = service.updateQuestion(id, imageFile, question, answer, topics);
        return ResponseEntity.ok(updatedQuestion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Question>> getQuestionsById(@PathVariable("id") String topicId) throws Exception {
        List<Question> questions = service.getQuestionsByTopicId(topicId);
        return ResponseEntity.ok(questions);
    }
}
