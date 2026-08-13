package com.quizengine.model;

import java.util.List;

public class Question {

    private int id;
    private String category;
    private String question;
    private List<String> options;
    private int correctAnswerIndex;

    public Question(
            int id,
            String category,
            String question,
            List<String> options,
            int correctAnswerIndex
    ) {
        this.id = id;
        this.category = category;
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}