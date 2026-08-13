package com.quizengine.model;

public class AnswerReview {

    private final Question question;
    private final int selectedAnswer;
    private final boolean correct;

    public AnswerReview(
            Question question,
            int selectedAnswer,
            boolean correct
    ) {
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.correct = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }
}