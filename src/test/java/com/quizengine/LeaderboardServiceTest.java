package com.quizengine;

import com.quizengine.model.LeaderboardEntry;
import com.quizengine.model.QuizResult;
import com.quizengine.service.LeaderboardService;
import com.quizengine.storage.LeaderboardStorage;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardServiceTest {

    @Test
    void shouldAddResultToLeaderboard() throws Exception {

        Path file = Files.createTempFile(
                "leaderboard",
                ".csv"
        );

        LeaderboardStorage storage =
                new LeaderboardStorage();

        LeaderboardService service =
                new LeaderboardService(
                        storage,
                        file
                );

        QuizResult result =
                new QuizResult(
                        8,
                        2,
                        0,
                        8,
                        10,
                        60,
                        List.of()
                );

        service.addResult(
                "Nabila",
                result
        );

        /*
         * Instead of calling service.getLeaderboard(),
         * we load the saved leaderboard directly.
         */
        List<LeaderboardEntry> entries =
                storage.load(file);

        assertEquals(1, entries.size());

        LeaderboardEntry entry =
                entries.get(0);

        assertEquals(
                "Nabila",
                entry.getPlayer()
        );

        assertEquals(
                8,
                entry.getScore()
        );

        assertEquals(
                8,
                entry.getCorrect()
        );

        assertEquals(
                2,
                entry.getWrong()
        );

        assertEquals(
                0,
                entry.getUnanswered()
        );

        Files.deleteIfExists(file);
    }


    @Test
    void shouldStoreMultipleResults() throws Exception {

        Path file = Files.createTempFile(
                "leaderboard",
                ".csv"
        );

        LeaderboardStorage storage =
                new LeaderboardStorage();

        LeaderboardService service =
                new LeaderboardService(
                        storage,
                        file
                );

        QuizResult firstResult =
                new QuizResult(
                        5,
                        5,
                        0,
                        5,
                        10,
                        60,
                        List.of()
                );

        QuizResult secondResult =
                new QuizResult(
                        9,
                        1,
                        0,
                        9,
                        10,
                        50,
                        List.of()
                );

        service.addResult(
                "Player One",
                firstResult
        );

        service.addResult(
                "Player Two",
                secondResult
        );

        List<LeaderboardEntry> entries =
                storage.load(file);

        assertEquals(2, entries.size());

        /*
         * LeaderboardService should sort by score
         * from highest to lowest.
         */
        assertEquals(
                "Player Two",
                entries.get(0).getPlayer()
        );

        assertEquals(
                9,
                entries.get(0).getScore()
        );

        assertEquals(
                "Player One",
                entries.get(1).getPlayer()
        );

        assertEquals(
                5,
                entries.get(1).getScore()
        );

        Files.deleteIfExists(file);
    }


    @Test
    void shouldCreateLeaderboardFileWhenAddingResult()
            throws Exception {

        Path directory =
                Files.createTempDirectory(
                        "quiz-engine-test"
                );

        Path file =
                directory.resolve(
                        "leaderboard.csv"
                );

        LeaderboardStorage storage =
                new LeaderboardStorage();

        LeaderboardService service =
                new LeaderboardService(
                        storage,
                        file
                );

        QuizResult result =
                new QuizResult(
                        7,
                        3,
                        0,
                        7,
                        10,
                        60,
                        List.of()
                );

        service.addResult(
                "Nabila",
                result
        );

        assertTrue(
                Files.exists(file)
        );

        List<LeaderboardEntry> entries =
                storage.load(file);

        assertEquals(
                1,
                entries.size()
        );

        assertEquals(
                "Nabila",
                entries.get(0).getPlayer()
        );

        Files.deleteIfExists(file);
        Files.deleteIfExists(directory);
    }
}