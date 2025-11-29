package com.quiz.quizservice.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quiz.quizservice.feign.QuizInterface;

import com.quiz.quizservice.model.QuestionWrapper;
import com.quiz.quizservice.model.Quiz;
import com.quiz.quizservice.model.Response;
import com.quiz.quizservice.repo.QuizRepo;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

import java.util.*;

@Service
public class QuizService {

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private QuizInterface quizInterface;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        CircuitBreaker cb = circuitBreakerFactory.create("questionServiceCB");

        List<String> questionIds = cb.run(
                () -> quizInterface.getQuestionsForQuiz(category, numQ).getBody(),
                throwable -> fallbackQuestionIds(category, numQ, throwable)
        );

        if (questionIds.isEmpty()) {
            return ResponseEntity.status(503)
                    .body("Question Service unavailable. Cannot create quiz right now.");
        }

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questionIds);
        quizRepo.save(quiz);

        return ResponseEntity.status(HttpStatus.CREATED).body("Success");
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(String id) {

        Quiz quiz = quizRepo.findById(id).orElse(null);

        if (quiz == null)
            return ResponseEntity.status(404).body(null);

        List<String> questionIds = quiz.getQuestionIds();

        CircuitBreaker cb = circuitBreakerFactory.create("questionServiceCB");

        List<QuestionWrapper> questions = cb.run(
                () -> quizInterface.getQuestionsFromId(questionIds).getBody(),
                throwable -> fallbackQuestionWrappers(questionIds, throwable)
        );

        return ResponseEntity.ok(questions);
    }

    public ResponseEntity<Integer> calculateResult(String id, List<Response> responses) {

        CircuitBreaker cb = circuitBreakerFactory.create("questionServiceCB");

        Integer score = cb.run(
                () -> quizInterface.getScore(responses).getBody(),
                throwable -> fallbackScore(throwable)
        );

        return ResponseEntity.ok(score);
    }

    public List<String> fallbackQuestionIds(String category, int numQ, Throwable t) {
        System.out.println("Fallback for getQuestionsForQuiz triggered: " + t);
        return Collections.emptyList(); 
    }

    public List<QuestionWrapper> fallbackQuestionWrappers(List<String> ids, Throwable t) {
        System.out.println("Fallback for getQuestionsFromId triggered: " + t);
        return new ArrayList<>();
    }

    public Integer fallbackScore(Throwable t) {
        System.out.println("Fallback for getScore triggered: " + t);
        return 0;
    }
}

