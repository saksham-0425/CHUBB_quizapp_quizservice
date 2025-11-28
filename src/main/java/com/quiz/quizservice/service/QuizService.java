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
import java.util.*;

@Service
public class QuizService {
	
	@Autowired
    QuizRepo quizRepo;
	
	@Autowired
	QuizInterface quizInterface;

	public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<String> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
        
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizRepo.save(quiz);
        
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

	public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(String id) {
		
		Quiz quiz = quizRepo.findById(id).get();
		List<String> questionIds = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions= quizInterface.getQuestionsFromId(questionIds);
	    return questions;
	}

	public ResponseEntity<Integer> calculateResult(String id, List<Response> responses) {
		ResponseEntity<Integer> score = quizInterface.getScore(responses);
		return score;
	}
}
