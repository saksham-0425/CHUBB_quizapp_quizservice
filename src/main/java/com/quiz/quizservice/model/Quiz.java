package com.quiz.quizservice.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "quiz")
public class Quiz {

    @Id
    private String id;

    private String title;
    
    private List<String> questionIds;
}
