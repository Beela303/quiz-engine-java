package com.quizengine.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class TimedInput {

    private final BufferedReader reader;
    private final ExecutorService executor;

    public TimedInput() {

        reader = new BufferedReader(
                new InputStreamReader(System.in)
        );

        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Reads a line from the console with a time limit.
     *
     * @param timeoutMillis maximum time to wait
     * @return the user's input, or null if the time expires
     */
    public String readLine(long timeoutMillis)
            throws InterruptedException {

        if (timeoutMillis <= 0) {
            return null;
        }

        Future<String> future =
                executor.submit(() -> {

                    try {
                        return reader.readLine();

                    } catch (IOException e) {
                        throw new RuntimeException(
                                "Could not read input.",
                                e
                        );
                    }
                });

        try {

            return future.get(
                    timeoutMillis,
                    TimeUnit.MILLISECONDS
            );

        } catch (TimeoutException e) {

            future.cancel(true);

            return null;

        } catch (ExecutionException e) {

            throw new RuntimeException(
                    "Error while reading input.",
                    e.getCause()
            );
        }
    }

    /**
     * Attempts to clear any input that is
     * already waiting in the console.
     */
    public void clearPendingInput() {

        try {

            while (reader.ready()) {
                reader.readLine();
            }

        } catch (IOException e) {

            System.out.println(
                    "Warning: Could not clear pending input."
            );
        }
    }

    /**
     * Shuts down the input thread.
     */
    public void shutdown() {

        executor.shutdownNow();
    }
}
