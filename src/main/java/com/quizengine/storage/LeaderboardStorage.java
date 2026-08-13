package com.quizengine.storage;

import com.quizengine.model.LeaderboardEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardStorage {

    private static final String HEADER =
            "player,score,correct,wrong,unanswered,percentage";

    public List<LeaderboardEntry> load(Path file)
            throws IOException {

        List<LeaderboardEntry> entries =
                new ArrayList<>();

        if (!Files.exists(file)) {
            return entries;
        }

        List<String> lines =
                Files.readAllLines(file);

        if (lines.isEmpty()) {
            return entries;
        }

        for (int i = 1; i < lines.size(); i++) {

            String line = lines.get(i);

            if (line.isBlank()) {
                continue;
            }

            String[] values =
                    line.split(",", -1);

            if (values.length != 6) {

                throw new IllegalArgumentException(
                        "Malformed leaderboard row: " + line
                );
            }

            try {

                entries.add(
                        new LeaderboardEntry(
                                values[0],
                                Integer.parseInt(values[1]),
                                Integer.parseInt(values[2]),
                                Integer.parseInt(values[3]),
                                Integer.parseInt(values[4]),
                                Double.parseDouble(values[5])
                        )
                );

            } catch (NumberFormatException e) {

                throw new IllegalArgumentException(
                        "Invalid number in leaderboard row: "
                                + line,
                        e
                );
            }
        }

        return entries;
    }

    public void save(
            Path file,
            List<LeaderboardEntry> entries
    ) throws IOException {

        Path parent = file.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        List<String> lines =
                new ArrayList<>();

        lines.add(HEADER);

        for (LeaderboardEntry entry : entries) {

            lines.add(
                    String.format(
                            "%s,%d,%d,%d,%d,%.2f",
                            entry.getPlayer(),
                            entry.getScore(),
                            entry.getCorrect(),
                            entry.getWrong(),
                            entry.getUnanswered(),
                            entry.getPercentage()
                    )
            );
        }

        Files.write(
                file,
                lines,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}