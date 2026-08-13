package com.quizengine.service;

public class ScoreService {

    private final int correctPoints;
    private final int wrongPenalty;

    public ScoreService(
            int correctPoints,
            int wrongPenalty
    ) {
        if (correctPoints < 0) {
            throw new IllegalArgumentException(
                    "Correct points cannot be negative."
            );
        }

        if (wrongPenalty < 0) {
            throw new IllegalArgumentException(
                    "Wrong penalty cannot be negative."
            );
        }

        this.correctPoints = correctPoints;
        this.wrongPenalty = wrongPenalty;
    }

    public int calculateScore(
            int correct,
            int wrong
    ) {

        if (correct < 0 || wrong < 0) {
            throw new IllegalArgumentException(
                    "Correct and wrong answers cannot be negative."
            );
        }

        return (correct * correctPoints)
                - (wrong * wrongPenalty);
    }
}