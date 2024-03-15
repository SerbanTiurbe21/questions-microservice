package com.example.questions.service;

import com.example.questions.exception.BlankTextException;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;
    private final ValidationService validationService;
    private static final String INVALID_TOPIC = "Invalid topic";

    @Override
    public Question createQuestion(Question question) throws Exception {
        try {
            validateInputFields(question.getQuestion(), question.getAnswer(), question.getTopics());

            List<Topic> persistedTopics = question.getTopics().stream()
                    .map(topic -> {
                        try {
                            return topicRepository.findByName(topic.getName())
                                    .orElseThrow(() -> new InvalidInputException(INVALID_TOPIC));
                        } catch (InvalidInputException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            Question questionObj = new Question(question.getQuestion(), question.getAnswer(), persistedTopics);

            return questionRepository.insert(loadResultForNrOfQuestionsForEachTopic(questionObj));
        } catch (BlankTextException | InvalidInputException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
    public int countQuestionsByTopic(String topicId) {
        return questionRepository.getNrOfQuestionsByTopicId(topicId);
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
    public Question updateQuestion(String id, String questionText, String answer, List<Topic> topics) throws Exception {
        try {
            validateInputFields(questionText, answer, topics);

            Question initialQuestion = getInitialQuestion(id);
            List<Topic> persistedTopics = topics.stream()
                    .map(topic -> {
                        try {
                            return topicRepository.findByName(topic.getName())
                                    .orElseThrow(() -> new InvalidInputException(INVALID_TOPIC));
                        } catch (InvalidInputException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            initialQuestion.setQuestion(questionText);
            initialQuestion.setAnswer(answer);
            initialQuestion.setTopics(persistedTopics);

            return questionRepository.save(loadResultForNrOfQuestionsForEachTopic(initialQuestion));
        } catch (InvalidInputException e) {
            throw new InvalidInputException(e.getMessage());
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

    private List<Question> loadResultForNrOfQuestionsForEachTopic(List<Question> questions) {
        for (Question question : questions) {
            for (Topic topic : question.getTopics()) {
                topic.setNrOfQuestions(countQuestionsByTopic(topic.getId()));
            }
        }
        return questions;
    }

    private Question loadResultForNrOfQuestionsForEachTopic(Question question) {
        for (Topic topic : question.getTopics()) {
            topic.setNrOfQuestions(countQuestionsByTopic(topic.getId()));
        }
        return question;
    }

    private void isTopicValid(List<Topic> topics) throws InvalidInputException {
        List<Topic> topicsDB = topicRepository.findAll();

        if (topics.isEmpty()) throw new InvalidInputException(INVALID_TOPIC);

        for (Topic topic : topics) {
            if (topic != null) {
                boolean topicExists = topicsDB.stream()
                        .anyMatch(dbTopic -> dbTopic.getName().equals(topic.getName()));
                if (!topicExists) {
                    throw new InvalidInputException(INVALID_TOPIC);
                }
            } else {
                throw new InvalidInputException(INVALID_TOPIC);
            }
        }
    }

    private void isTopicValid(String topicId) throws InvalidInputException {
        if (topicId == null) throw new InvalidInputException(INVALID_TOPIC);
        Optional<Topic> topicDB = topicRepository.findById(topicId);

        if (topicDB.isEmpty()) {
            throw new InvalidInputException(INVALID_TOPIC);
        }
    }
}
