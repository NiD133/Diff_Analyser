package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.time.Duration;
import org.junit.jupiter.api.Test;

/**
 * Tests the interaction between {@link BoundedReader} and {@link LineNumberReader}.
 *
 * This class focuses on ensuring that decorators like {@link LineNumberReader}
 * behave correctly when wrapping a {@link BoundedReader}.
 */
public class BoundedReaderWithLineNumberReaderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    /**
     * Tests that a LineNumberReader reading from a BoundedReader terminates correctly
     * when the underlying stream does not end with a newline character.
     * <p>
     * This is a regression test for a bug that could cause an infinite loop. The
     * {@code assertTimeout} ensures the test fails if the read operation hangs.
     * </p>
     */
    @Test
    void shouldTerminateWhenReadingLinesFromStreamWithoutTrailingNewline() {
        assertTimeout(TIMEOUT, () -> {
            // Arrange: A multi-line string that does not end with a newline.
            final String input = "Line 1\nLine 2\nLine 3";

            // The bound is intentionally large so it doesn't affect the read operation.
            final int largeBound = 100;
            final Reader boundedReader = new BoundedReader(new StringReader(input), largeBound);

            // Act & Assert
            try (final LineNumberReader lineNumberReader = new LineNumberReader(boundedReader)) {
                // Read all lines to ensure we reach the end of the stream.
                while (lineNumberReader.readLine() != null) {
                    // Consuming the stream
                }

                // The primary goal is to ensure the loop terminates (covered by assertTimeout).
                // We also assert the final line number for correctness.
                assertEquals(3, lineNumberReader.getLineNumber(), "Should have read 3 lines.");
            }
        });
    }
}