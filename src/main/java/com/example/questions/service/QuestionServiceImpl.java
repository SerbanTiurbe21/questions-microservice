package com.example.questions.service;

import com.example.questions.exception.BlankTextException;
import com.example.questions.exception.ImageTypeException;
import com.example.questions.exception.InvalidInputException;
import com.example.questions.model.Question;
import com.example.questions.model.ResponseData;
import com.example.questions.model.Status;
import com.example.questions.model.Topic;
import com.example.questions.repository.QuestionRepository;
import com.example.questions.repository.TopicRepository;
import com.example.questions.validator.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;
    private final ValidationService validationService;

    @Override
    public Question createQuestion(MultipartFile imageFile, String question, String answer, List<Topic> topics) throws Exception {
        try {
            validateInputFields(question, answer, topics);

            Question questionObj = new Question(question, answer, topics, validationService.isValidFile(imageFile));

            return questionRepository.insert(loadResultForNrOfQuestionsForEachTopic(questionObj));
        } catch (ImageTypeException e) {
            throw new ImageTypeException(e.getMessage());
        }
    }


    @Override
    public List<Question> getQuestionsByTopicId(String topicId) throws Exception {
        try {
            isTopicValid(topicId);

            return loadResultForNrOfQuestionsForEachTopic(questionRepository.findQuestionsByTopic(topicId));
        } catch (InvalidInputException e) {
            throw new InvalidInputException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public int countQuestionsByTopic(String topicId) throws Exception {
        try {
            return questionRepository.getNrOfQuestionsByTopicId(topicId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ResponseData deleteQuestion(String id) {
        if (questionRepository.findById(id).isPresent()) {
            questionRepository.deleteById(id);
            return new ResponseData(Status.SUCCESS, "Question was deleted");
        }
        return new ResponseData(Status.FAILED, "Question could not be found");
    }

    @Override
    public Question updateQuestion(String id, MultipartFile imageFile, String question, String answer, List<Topic> topics) throws Exception {
        try {
            validateInputFields(question, answer, topics);
            var initialQuestion = getInitialQuestion(id);

            initialQuestion.setQuestion(question);
            initialQuestion.setImage(validationService.isValidFile(imageFile));
            initialQuestion.setTopics(topics);
            initialQuestion.setAnswer(answer);

            return questionRepository.save(loadResultForNrOfQuestionsForEachTopic(initialQuestion));
        } catch (ImageTypeException e) {
            throw new ImageTypeException(e.getMessage());
        }
    }

    @Override
    public Optional<Question> getQuestionById(String id) {
        return questionRepository.findById(id);
    }

    private Question getInitialQuestion(String id) throws Exception {
        if (questionRepository.findById(id).isPresent()) {
            return questionRepository.getQuestionById(id);
        } else {
            throw new InvalidInputException("Question could not be found");
        }
    }

    private void validateInputFields(String question, String answer, List<Topic> topics) throws Exception {
        try {
            isTopicValid(topics);
            validationService.isValidText(answer);
            validationService.isValidText(question);
        } catch (BlankTextException e) {
            throw new BlankTextException(e.getMessage());
        } catch (InvalidInputException e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

    private List<Question> loadResultForNrOfQuestionsForEachTopic(List<Question> questions) throws Exception {
        for (Question question : questions) {
            for (Topic topic : question.getTopics()) {
                topic.setNrOfQuestions(countQuestionsByTopic(topic.getId()));
            }
        }
        return questions;
    }

    private Question loadResultForNrOfQuestionsForEachTopic(Question question) throws Exception {
        for (Topic topic : question.getTopics()) {
            topic.setNrOfQuestions(countQuestionsByTopic(topic.getId()));
        }
        return question;
    }

    private void isTopicValid(List<Topic> topics) throws InvalidInputException {
        List<Topic> topicsDB = topicRepository.findAll();

        if (topics.isEmpty()) throw new InvalidInputException("Invalid topic");
        for (Topic topic : topics) {
            if (topic != null) {
                if (!topicsDB.contains(topic)) {
                    throw new InvalidInputException("Invalid topic");
                }
            } else {
                throw new InvalidInputException("Invalid topic");
            }
        }
    }

    private void isTopicValid(String topicId) throws InvalidInputException {
        if (topicId == null) throw new InvalidInputException("Invalid topic");
        Optional<Topic> topicDB = topicRepository.findById(topicId);

        if (topicDB.isEmpty()) {
            throw new InvalidInputException("Invalid topic");
        }
    }
}
