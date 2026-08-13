package com.quizengine.service;

import com.quizengine.model.LeaderboardEntry;
import com.quizengine.model.QuizResult;
import com.quizengine.storage.LeaderboardStorage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardService {

    private final LeaderboardStorage storage;
    private final Path file;

    public LeaderboardService(
            LeaderboardStorage storage,
            Path file
    ) {
        this.storage = storage;
        this.file = file;
    }

    public void addResult(
            String player,
            QuizResult result
    ) throws IOException {

        List<LeaderboardEntry> entries =
                storage.load(file);

        double percentage =
                result.getTotalQuestions() == 0
                        ? 0.0
                        : ((double) result.getCorrect()
                        / result.getTotalQuestions()) * 100.0;

        LeaderboardEntry entry =
                new LeaderboardEntry(
                        player,
                        result.getScore(),
                        result.getCorrect(),
                        result.getWrong(),
                        result.getUnanswered(),
                        percentage
                );

        entries.add(entry);

        entries.sort(
                Comparator.comparingInt(
                        LeaderboardEntry::getScore
                ).reversed()
        );

        storage.save(file, entries);
    }

    public List<LeaderboardEntry> getTopEntries()
            throws IOException {

        List<LeaderboardEntry> entries =
                storage.load(file);

        entries.sort(
                Comparator.comparingInt(
                        LeaderboardEntry::getScore
                ).reversed()
        );

        return new ArrayList<>(entries);
    }
}