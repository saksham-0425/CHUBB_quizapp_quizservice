package com.quiz.quizservice.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionWrapper {
    private String id;
    private String title;
    private Options options;
}

