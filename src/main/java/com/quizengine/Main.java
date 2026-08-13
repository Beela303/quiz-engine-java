package com.quizengine;

import com.quizengine.model.AnswerReview;
import com.quizengine.model.LeaderboardEntry;
import com.quizengine.model.Question;
import com.quizengine.model.QuizResult;
import com.quizengine.service.QuizService;
import com.quizengine.service.ScoreService;
import com.quizengine.storage.LeaderboardStorage;
import com.quizengine.storage.QuestionBank;
import com.quizengine.util.TimedInput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final int QUESTIONS_PER_QUIZ = 5;
    private static final long TIME_PER_QUESTION_SECONDS = 30;

    private static final int CORRECT_POINTS = 10;
    private static final int WRONG_PENALTY = 5;

    private static final Path QUESTION_FILE =
            Path.of("src/main/resources/questions.json");

    private static final Path LEADERBOARD_FILE =
            Path.of("data/leaderboard.csv");

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        QuestionBank questionBank = new QuestionBank();

        ScoreService scoreService =
                new ScoreService(
                        CORRECT_POINTS,
                        WRONG_PENALTY
                );

        QuizService quizService =
                new QuizService(scoreService);

        LeaderboardStorage leaderboardStorage =
                new LeaderboardStorage();

        TimedInput timedInput =
                new TimedInput();

        try {

            List<Question> allQuestions =
                    questionBank.loadQuestions(
                            QUESTION_FILE
                    );

            System.out.println();
            System.out.println("=================================");
            System.out.println("        JAVA QUIZ ENGINE");
            System.out.println("=================================");
            System.out.println();

            boolean playAgain = true;

            while (playAgain) {

                playQuiz(
                        scanner,
                        quizService,
                        leaderboardStorage,
                        timedInput,
                        allQuestions
                );

                System.out.println();
                System.out.println("Would you like to play again?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.print("Choose: ");

                String choice = scanner.nextLine();

                playAgain =
                        choice.equals("1") ||
                        choice.equalsIgnoreCase("yes");
            }

            System.out.println();
            System.out.println("=================================");
            System.out.println("      Thanks for playing!");
            System.out.println("=================================");

        } catch (Exception e) {

            System.out.println();
            System.out.println(
                    "ERROR: " + e.getMessage()
            );

        } finally {

            timedInput.shutdown();
            scanner.close();
        }
    }

    private static void playQuiz(
            Scanner scanner,
            QuizService quizService,
            LeaderboardStorage leaderboardStorage,
            TimedInput timedInput,
            List<Question> allQuestions
    ) throws Exception {

        System.out.print("Enter your name: ");

        String playerName =
                scanner.nextLine().trim();

        if (playerName.isBlank()) {
            playerName = "Anonymous";
        }

        System.out.println();
        System.out.println("Choose a category:");
        System.out.println("1. Java Basics");
        System.out.println("2. OOP");
        System.out.println("3. Collections");
        System.out.println("4. All");
        System.out.print("Choose: ");

        String categoryChoice =
                scanner.nextLine().trim();

        String category;

        switch (categoryChoice) {

            case "1":
                category = "Java Basics";
                break;

            case "2":
                category = "OOP";
                break;

            case "3":
                category = "Collections";
                break;

            case "4":
                category = "All";
                break;

            default:
                System.out.println(
                        "Invalid category. Using All."
                );

                category = "All";
        }

        List<Question> questions =
                quizService.prepareQuestions(
                        allQuestions,
                        category
                );

        if (questions.isEmpty()) {

            System.out.println();
            System.out.println(
                    "No questions found for this category."
            );

            return;
        }

        if (questions.size() > QUESTIONS_PER_QUIZ) {

            questions =
                    new ArrayList<>(
                            questions.subList(
                                    0,
                                    QUESTIONS_PER_QUIZ
                            )
                    );
        }

        System.out.println();
        System.out.println("=================================");
        System.out.println("          QUIZ STARTING");
        System.out.println("=================================");
        System.out.println(
                "Category: " + category
        );
        System.out.println(
                "Questions: " + questions.size()
        );
        System.out.println(
                "Time per question: "
                        + TIME_PER_QUESTION_SECONDS
                        + " seconds"
        );
        System.out.println();

        System.out.println(
                "Press Enter to begin..."
        );

        scanner.nextLine();

        List<Integer> answers =
                new ArrayList<>();

        long startTime =
                System.nanoTime();

        for (int i = 0; i < questions.size(); i++) {

            Question question =
                    questions.get(i);

            System.out.println();
            System.out.println("---------------------------------");

            System.out.println(
                    "Question "
                            + (i + 1)
                            + "/"
                            + questions.size()
            );

            System.out.println();

            System.out.println(
                    question.getQuestion()
            );

            System.out.println();

            List<String> options =
                    question.getOptions();

            for (int j = 0; j < options.size(); j++) {

                System.out.println(
                        (j + 1)
                                + ". "
                                + options.get(j)
                );
            }

            System.out.println();

            System.out.println(
                    "You have "
                            + TIME_PER_QUESTION_SECONDS
                            + " seconds."
            );

            System.out.print(
                    "Your answer (1-"
                            + options.size()
                            + "): "
            );

            timedInput.clearPendingInput();

            String input =
                    timedInput.readLine(
                            TIME_PER_QUESTION_SECONDS
                                    * 1000
                    );

            if (input == null) {

                System.out.println();
                System.out.println(
                        "Time's up! "
                                + "Question marked unanswered."
                );

                answers.add(null);

                continue;
            }

            input = input.trim();

            try {

                int answer =
                        Integer.parseInt(input);

                if (
                        answer < 1 ||
                        answer > options.size()
                ) {

                    System.out.println(
                            "Invalid answer. "
                                    + "Question marked unanswered."
                    );

                    answers.add(null);

                } else {

                    answers.add(answer - 1);
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. "
                                + "Question marked unanswered."
                );

                answers.add(null);
            }
        }

        long endTime =
                System.nanoTime();

        long elapsedSeconds =
                (endTime - startTime)
                        / 1_000_000_000;

        QuizResult result =
                quizService.calculateResult(
                        questions,
                        answers,
                        elapsedSeconds
                );

        displayResults(
                playerName,
                result
        );

        saveLeaderboardEntry(
                playerName,
                result,
                leaderboardStorage
        );

        displayLeaderboard(
                leaderboardStorage
        );
    }

    private static void displayResults(
            String playerName,
            QuizResult result
    ) {

        int total =
                result.getCorrect()
                        + result.getWrong()
                        + result.getUnanswered();

        double percentage = 0;

        if (total > 0) {

            percentage =
                    ((double) result.getCorrect()
                            / total)
                            * 100;
        }

        System.out.println();
        System.out.println("=================================");
        System.out.println("          QUIZ RESULTS");
        System.out.println("=================================");

        System.out.println(
                "Player:      " + playerName
        );

        System.out.println(
                "Correct:     " + result.getCorrect()
        );

        System.out.println(
                "Wrong:       " + result.getWrong()
        );

        System.out.println(
                "Unanswered:  " + result.getUnanswered()
        );

        System.out.println(
                "Score:       " + result.getScore()
        );

        System.out.printf(
                "Percentage:  %.2f%%%n",
                percentage
        );

        System.out.println(
                "Time:        "
                        + result.getElapsedSeconds()
                        + " seconds"
        );

        System.out.println(
                "================================="
        );

        displayWrongAnswers(result);
    }

    private static void displayWrongAnswers(
            QuizResult result
    ) {

        List<AnswerReview> wrongAnswers =
                result.getWrongAnswers();

        if (wrongAnswers == null ||
                wrongAnswers.isEmpty()) {

            System.out.println();
            System.out.println(
                    "Excellent! You had no wrong answers."
            );

            return;
        }

        System.out.println();
        System.out.println("=================================");
        System.out.println("         WRONG ANSWERS");
        System.out.println("=================================");

        for (AnswerReview review : wrongAnswers) {

            Question question =
                    review.getQuestion();

            int selected =
                    review.getSelectedAnswer();

            int correct =
                    question.getCorrectAnswerIndex();

            System.out.println();
            System.out.println(
                    "Question: "
                            + question.getQuestion()
            );

            System.out.println(
                    "Your answer: "
                            + question.getOptions()
                            .get(selected)
            );

            System.out.println(
                    "Correct answer: "
                            + question.getOptions()
                            .get(correct)
            );

            System.out.println("---------------------------------");
        }
    }

    private static void saveLeaderboardEntry(
            String playerName,
            QuizResult result,
            LeaderboardStorage storage
    ) throws Exception {

        List<LeaderboardEntry> entries =
                storage.load(LEADERBOARD_FILE);

        int total =
                result.getCorrect()
                        + result.getWrong()
                        + result.getUnanswered();

        double percentage = 0;

        if (total > 0) {

            percentage =
                    ((double) result.getCorrect()
                            / total)
                            * 100;
        }

        LeaderboardEntry entry =
                new LeaderboardEntry(
                        playerName,
                        result.getScore(),
                        result.getCorrect(),
                        result.getWrong(),
                        result.getUnanswered(),
                        percentage
                );

        entries.add(entry);

        entries.sort(
                (a, b) ->
                        Integer.compare(
                                b.getScore(),
                                a.getScore()
                        )
        );

        if (entries.size() > 10) {

            entries =
                    new ArrayList<>(
                            entries.subList(
                                    0,
                                    10
                            )
                    );
        }

        storage.save(
                LEADERBOARD_FILE,
                entries
        );
    }

    private static void displayLeaderboard(
            LeaderboardStorage storage
    ) throws Exception {

        List<LeaderboardEntry> entries =
                storage.load(
                        LEADERBOARD_FILE
                );

        System.out.println();
        System.out.println("=================================");
        System.out.println("          LEADERBOARD");
        System.out.println("=================================");

        if (entries.isEmpty()) {

            System.out.println(
                    "No scores yet."
            );

            return;
        }

        System.out.printf(
                "%-6s %-15s %-8s%n",
                "Rank",
                "Player",
                "Score"
        );

        System.out.println("---------------------------------");

        for (int i = 0; i < entries.size(); i++) {

            LeaderboardEntry entry =
                    entries.get(i);

            System.out.printf(
                    "%-6d %-15s %-8d%n",
                    i + 1,
                    entry.getPlayer(),
                    entry.getScore()
            );
        }

        System.out.println(
                "================================="
        );
    }
}