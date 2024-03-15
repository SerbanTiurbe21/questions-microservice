package com.example.questions.repository;

import com.example.questions.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends MongoRepository<Topic, String> {
    boolean existsByNameIgnoreCase(String title);
    Optional<Topic> findByName(String name);
}