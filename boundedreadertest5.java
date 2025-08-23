package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.time.Duration;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedReader} focusing on its interaction with other Readers.
 */
// Renamed class from BoundedReaderTestTest5 for clarity and correctness.
public class BoundedReaderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    /**
     * This test verifies that a {@link LineNumberReader} wrapping a {@link BoundedReader}
     * correctly terminates when reading a stream that ends with a newline character.
     * <p>
     * This test was added to prevent a regression of a bug where this specific
     * combination of readers could lead to an infinite loop. The test passes if it
     * completes within the specified timeout.
     * </p>
     */
    @Test
    void lineReaderOnBoundedReaderShouldTerminateForStreamEndingWithNewline() {
        // Arrange
        final String inputEndingWithNewline = "0\n1\n2\n";
        // A bound large enough that it won't be reached. This ensures the test focuses
        // on the interaction between the readers, not the bounding behavior itself.
        final int largeBound = 10_000_000;
        final Reader boundedReader = new BoundedReader(new StringReader(inputEndingWithNewline), largeBound);
        final LineNumberReader lineReader = new LineNumberReader(boundedReader);

        // Act & Assert
        // The test's purpose is to ensure the read loop terminates.
        // assertTimeout will fail the test if the loop hangs, thus confirming the fix.
        assertTimeout(TIMEOUT, () -> {
            try (lineReader) {
                while (lineReader.readLine() != null) {
                    // Keep reading until the end of the stream is reached.
                }
            }
        });
    }
}