package com.quiz.quizservice.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quiz.quizservice.model.Quiz;

public interface QuizRepo extends MongoRepository<Quiz, String> {
    
}
