package com.example.questions.repository;

import com.example.questions.model.Question;
import com.example.questions.model.Topic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    private Question question1;
    private Question question2;

    @BeforeEach
    public void setUp() {
        Topic topic = new Topic("1", "Java", null);
        question1 = new Question("How does Java work?", "Answer 1", List.of(topic));
        question2 = new Question("What is Spring?", "Answer 2", List.of(topic));
        questionRepository.saveAll(List.of(question1, question2));
    }

    @AfterEach
    public void tearDown() {
        questionRepository.delete(question1);
        questionRepository.delete(question2);
    }

    @Test
    void shouldFindQuestionsByTopic() {
        final String topicId = "1";
        List<Question> questions = questionRepository.findQuestionsByTopic(topicId);

        assertEquals(2, questions.size());
        assertTrue(questions.stream().anyMatch(q -> q.getQuestion().equals(question1.getQuestion())));
        assertTrue(questions.stream().anyMatch(q -> q.getQuestion().equals(question2.getQuestion())));
    }

    @Test
    void shouldGetNrOfQuestionsByTopicId() {
        final String topicId = "1";
        int nrOfQuestions = questionRepository.getNrOfQuestionsByTopicId(topicId);

        assertEquals(2, nrOfQuestions);
    }

    @Test
    void shouldGetQuestionById() {
        Question question = questionRepository.getQuestionById(question1.getId());

        assertNotNull(question);
        assertEquals(question1.getQuestion(), question.getQuestion());
    }
}
