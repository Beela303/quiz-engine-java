package com.quizengine.model;

public class LeaderboardEntry {

    private final String player;
    private final int score;
    private final int correct;
    private final int wrong;
    private final int unanswered;
    private final double percentage;

    public LeaderboardEntry(
            String player,
            int score,
            int correct,
            int wrong,
            int unanswered,
            double percentage
    ) {
        this.player = player;
        this.score = score;
        this.correct = correct;
        this.wrong = wrong;
        this.unanswered = unanswered;
        this.percentage = percentage;
    }

    public String getPlayer() {
        return player;
    }

    public int getScore() {
        return score;
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

    public double getPercentage() {
        return percentage;
    }
}