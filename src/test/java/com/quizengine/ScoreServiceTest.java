package com.quizengine;

import com.quizengine.service.ScoreService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreServiceTest {

    private final ScoreService scoreService =
            new ScoreService(10, 5);

    @Test
    void shouldCalculateScoreForCorrectAnswers() {
        int score = scoreService.calculateScore(5, 0);

        assertEquals(50, score);
    }

    @Test
    void shouldApplyNegativeMarkingForWrongAnswers() {
        int score = scoreService.calculateScore(0, 2);

        assertEquals(-10, score);
    }

    @Test
    void shouldCalculateMixedAnswers() {
        int score = scoreService.calculateScore(3, 2);

        // 3 correct × 10 = 30
        // 2 wrong × 5 penalty = -10
        // Total = 20
        assertEquals(20, score);
    }

    @Test
    void shouldHandleNoAnswers() {
        int score = scoreService.calculateScore(0, 0);

        assertEquals(0, score);
    }

    @Test
    void shouldHandleOnlyCorrectAnswers() {
        int score = scoreService.calculateScore(10, 0);

        assertEquals(100, score);
    }

    @Test
    void shouldHandleOnlyWrongAnswers() {
        int score = scoreService.calculateScore(0, 2);

        assertEquals(-10, score);
    }

    @Test
    void shouldHandleEqualCorrectAndWrongAnswers() {
        int score = scoreService.calculateScore(2, 2);

        // 2 × 10 = 20
        // 2 × 5 = -10
        // Total = 10
        assertEquals(10, score);
    }

    @Test
    void shouldHandleLargeNumberOfAnswers() {
        int score = scoreService.calculateScore(100, 20);

        // 100 × 10 = 1000
        // 20 × 5 = -100
        // Total = 900
        assertEquals(900, score);
    }
}