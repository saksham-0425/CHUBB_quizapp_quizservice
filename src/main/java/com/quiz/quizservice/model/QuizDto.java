package com.quiz.quizservice.model;

import lombok.Data;

@Data
public class QuizDto {
    String categoryName;
    String title;
    Integer numQ;
}
