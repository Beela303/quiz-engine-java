package com.quizengine;

import com.quizengine.model.Question;
import com.quizengine.storage.QuestionBank;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionBankTest {

    @Test
    void shouldLoadQuestionsFromJsonFile() throws Exception {

        Path file = Files.createTempFile(
                "questions",
                ".json"
        );

        String json = """
                [
                    {
                        "category": "Java",
                        "question": "What is Java?",
                        "options": [
                            "A programming language",
                            "A database",
                            "An operating system",
                            "A web browser"
                        ],
                        "correctAnswerIndex": 0
                    }
                ]
                """;

        Files.writeString(file, json);

        QuestionBank questionBank = new QuestionBank();

        List<Question> questions =
                questionBank.loadQuestions(file);

        assertNotNull(questions);
        assertEquals(1, questions.size());

        Question question = questions.get(0);

        assertEquals("Java", question.getCategory());
        assertEquals("What is Java?", question.getQuestion());
        assertEquals(4, question.getOptions().size());
        assertEquals(0, question.getCorrectAnswerIndex());

        Files.deleteIfExists(file);
    }


    @Test
    void shouldThrowExceptionWhenQuestionBankDoesNotExist()
            throws Exception {

        Path file = Path.of(
                "does-not-exist-quiz-bank.json"
        );

        QuestionBank questionBank = new QuestionBank();

        assertThrows(
                java.io.IOException.class,
                () -> questionBank.loadQuestions(file)
        );
    }


    @Test
    void shouldRejectEmptyQuestionBank()
            throws Exception {

        Path file = Files.createTempFile(
                "empty-questions",
                ".json"
        );

        Files.writeString(file, "");

        QuestionBank questionBank = new QuestionBank();

        assertThrows(
                IllegalArgumentException.class,
                () -> questionBank.loadQuestions(file)
        );

        Files.deleteIfExists(file);
    }


    @Test
    void shouldRejectMalformedJson()
            throws Exception {

        Path file = Files.createTempFile(
                "malformed-questions",
                ".json"
        );

        String malformedJson = """
                [
                    {
                        "category": "Java",
                        "question": "What is Java?",
                        "options": [
                            "A programming language",
                            "A database"
                        ],
                """;

        Files.writeString(file, malformedJson);

        QuestionBank questionBank = new QuestionBank();

        assertThrows(
                IllegalArgumentException.class,
                () -> questionBank.loadQuestions(file)
        );

        Files.deleteIfExists(file);
    }


    @Test
    void shouldRejectQuestionWithWrongNumberOfOptions()
            throws Exception {

        Path file = Files.createTempFile(
                "invalid-options",
                ".json"
        );

        String json = """
                [
                    {
                        "category": "Java",
                        "question": "What is Java?",
                        "options": [
                            "A programming language",
                            "A database",
                            "An operating system"
                        ],
                        "correctAnswerIndex": 0
                    }
                ]
                """;

        Files.writeString(file, json);

        QuestionBank questionBank = new QuestionBank();

        assertThrows(
                IllegalArgumentException.class,
                () -> questionBank.loadQuestions(file)
        );

        Files.deleteIfExists(file);
    }


    @Test
    void shouldRejectInvalidCorrectAnswerIndex()
            throws Exception {

        Path file = Files.createTempFile(
                "invalid-answer",
                ".json"
        );

        String json = """
                [
                    {
                        "category": "Java",
                        "question": "What is Java?",
                        "options": [
                            "A programming language",
                            "A database",
                            "An operating system",
                            "A web browser"
                        ],
                        "correctAnswerIndex": 5
                    }
                ]
                """;

        Files.writeString(file, json);

        QuestionBank questionBank = new QuestionBank();

        assertThrows(
                IllegalArgumentException.class,
                () -> questionBank.loadQuestions(file)
        );

        Files.deleteIfExists(file);
    }
}