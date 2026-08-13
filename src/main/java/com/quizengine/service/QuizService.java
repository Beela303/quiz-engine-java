package com.quizengine.service;

import com.quizengine.model.AnswerReview;
import com.quizengine.model.Question;
import com.quizengine.model.QuizResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuizService {

    private final ScoreService scoreService;

    public QuizService(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    public QuizResult calculateResult(
            List<Question> questions,
            List<Integer> answers,
            long elapsedSeconds
    ) {

        int correct = 0;
        int wrong = 0;
        int unanswered = 0;

        List<AnswerReview> wrongAnswers =
                new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {

            Question question = questions.get(i);
            Integer answer = answers.get(i);

            // No answer was provided
            if (answer == null) {

                unanswered++;
                continue;
            }

            // Correct answer
            if (answer == question.getCorrectAnswerIndex()) {

                correct++;

            } else {

                // Wrong answer
                wrong++;

                wrongAnswers.add(
                        new AnswerReview(
                                question,
                                answer,
                                false
                        )
                );
            }
        }

        int score =
                scoreService.calculateScore(
                        correct,
                        wrong
                );

        return new QuizResult(
                correct,
                wrong,
                unanswered,
                score,
                questions.size(),
                elapsedSeconds,
                wrongAnswers
        );
    }

    public List<Question> prepareQuestions(
            List<Question> questions,
            String category
    ) {

        List<Question> filtered;

        if (category.equalsIgnoreCase("All")) {

            filtered =
                    new ArrayList<>(questions);

        } else {

            // Create a final copy so the lambda
            // can safely use the category value.
            final String selectedCategory =
                    category;

            filtered =
                    questions.stream()
                            .filter(q ->
                                    q.getCategory()
                                            .equalsIgnoreCase(
                                                    selectedCategory
                                            )
                            )
                            .collect(Collectors.toList());
        }

        // Randomise the questions
        Collections.shuffle(filtered);

        return filtered;
    }
}