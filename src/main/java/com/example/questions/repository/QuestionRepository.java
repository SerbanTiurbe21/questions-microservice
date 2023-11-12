package com.example.questions.repository;

import com.example.questions.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
    @Query(value = "{topics : ?0}", count = true)
    int getNrOfQuestionsByTopicId(String topicId);

    @Query("{topics: ?0}")
    List<Question> findQuestionsByTopic(String topicId);

    Question getQuestionById(String id);
}
