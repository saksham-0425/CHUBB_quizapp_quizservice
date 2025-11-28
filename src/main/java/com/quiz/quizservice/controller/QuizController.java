package com.quiz.quizservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import com.quiz.quizservice.model.*;

import com.quiz.quizservice.service.QuizService;

@RestController
@RequestMapping("/quiz") 
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("/create")  
    public ResponseEntity<String> createQuiz(@RequestBody QuizDto quizDto) {

        return quizService.createQuiz(quizDto.getCategoryName(), quizDto.getNumQ(), quizDto.getTitle());
    }
    
    @GetMapping("/get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable String id){
    	return quizService.getQuizQuestions(id);
    }
    
    @PostMapping("/submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable String id, @RequestBody List<Response> responses){
    	return quizService.calculateResult(id, responses);
    }
}
