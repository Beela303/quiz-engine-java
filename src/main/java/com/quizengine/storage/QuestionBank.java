package com.quizengine.storage;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.quizengine.model.Question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class QuestionBank {

    private final Gson gson = new Gson();

    public List<Question> loadQuestions(Path file) throws IOException {

        if (!Files.exists(file)) {
            throw new IOException(
                    "Question bank not found: " + file
            );
        }

        String json = Files.readString(file);

        if (json.isBlank()) {
            throw new IllegalArgumentException(
                    "Question bank is empty."
            );
        }

        try {
            Question[] questions = gson.fromJson(
                    json,
                    Question[].class
            );

            if (questions == null || questions.length == 0) {
                throw new IllegalArgumentException(
                        "Question bank contains no questions."
                );
            }

            validateQuestions(questions);

            return Arrays.asList(questions);

        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(
                    "Malformed question bank JSON.",
                    e
            );
        }
    }

    private void validateQuestions(Question[] questions) {

        for (Question question : questions) {

            if (question.getQuestion() == null ||
                    question.getQuestion().isBlank()) {

                throw new IllegalArgumentException(
                        "A question has missing question text."
                );
            }

            if (question.getCategory() == null ||
                    question.getCategory().isBlank()) {

                throw new IllegalArgumentException(
                        "A question has a missing category."
                );
            }

            if (question.getOptions() == null ||
                    question.getOptions().size() != 4) {

                throw new IllegalArgumentException(
                        "Every question must have exactly 4 options."
                );
            }

            if (question.getCorrectAnswerIndex() < 0 ||
                    question.getCorrectAnswerIndex() >=
                            question.getOptions().size()) {

                throw new IllegalArgumentException(
                        "Invalid correct answer index."
                );
            }
        }
    }
}