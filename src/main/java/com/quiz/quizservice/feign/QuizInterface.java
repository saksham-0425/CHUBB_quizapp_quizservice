package com.quiz.quizservice.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.quiz.quizservice.model.QuestionWrapper;
import com.quiz.quizservice.model.Response;

@FeignClient(name = "QUESTION-SERVICE")
public interface QuizInterface {

    @GetMapping("/Question/generate")
    ResponseEntity<List<String>> getQuestionsForQuiz(
            @RequestParam String categoryName,
            @RequestParam Integer numQuestions);

    @PostMapping("/Question/getQuestions")
    ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(
            @RequestBody List<String> questionIds);

    @PostMapping("/Question/getScore")
    ResponseEntity<Integer> getScore(@RequestBody List<Response> responses);
}
