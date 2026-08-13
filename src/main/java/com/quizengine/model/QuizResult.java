package com.quizengine.model;

import com.quizengine.model.AnswerReview;
import com.quizengine.model.Question;
import com.quizengine.model.QuizResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuizResult {

    private final int correct;
    private final int wrong;
    private final int unanswered;
    private final int score;
    private final int totalQuestions;
    private final long elapsedSeconds;
    private final List<AnswerReview> wrongAnswers;

    public QuizResult(
            int correct,
            int wrong,
            int unanswered,
            int score,
            int totalQuestions,
            long elapsedSeconds,
            List<AnswerReview> wrongAnswers
    ) {
        this.correct = correct;
        this.wrong = wrong;
        this.unanswered = unanswered;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.elapsedSeconds = elapsedSeconds;
        this.wrongAnswers = wrongAnswers;
    }

    public int getCorrect() {
        return correct;
    }

    public int getWrong() {
        return wrong;
    }

    public int getUnanswered() {
        return unanswered;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public List<AnswerReview> getWrongAnswers() {
        return wrongAnswers;
    }
}