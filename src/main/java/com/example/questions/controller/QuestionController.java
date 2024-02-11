package com.example.questions.controller;

import com.example.questions.model.Question;
import com.example.questions.model.ResponseData;
import com.example.questions.model.Status;
import com.example.questions.model.Topic;
import com.example.questions.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class QuestionController {

    private final QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteQuestion(@PathVariable("id") String id)  {
        var responseData = service.deleteQuestion(id);
        return new ResponseEntity<>(responseData,
                Status.SUCCESS.equals(responseData.getStatus()) ?
                        HttpStatus.OK: HttpStatus.BAD_REQUEST);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public Question addQuestion(@RequestParam(value = "imageFile",required = false) MultipartFile imageFile,
                                @RequestParam(value = "question") String question,
                                @RequestParam(value = "answer") String answer,
                                @RequestParam(value = "topics") List<Topic> topics) throws Exception {
        return service.createQuestion(imageFile,question,answer,topics);
    }

    @GetMapping("/{id}")
    public List<Question> getQuestionsById(@PathVariable("id") String topicId) throws Exception {
        return service.getQuestionsByTopicId(topicId);
    }

    @PutMapping("/{id}")
    public Question editQuestion (@PathVariable("id") String id,
                                  @RequestParam(value = "imageFile",required = false) MultipartFile imageFile,
                                  @RequestParam(value = "question", required = false) String question,
                                  @RequestParam(value = "answer", required = false) String answer,
                                  @RequestParam(value = "topics", required = false)List<Topic> topics) throws  Exception {
        return service.updateQuestion(id, imageFile, question, answer, topics);
    }

}
