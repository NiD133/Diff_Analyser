package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.time.Duration;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedReader} focusing on its interaction with other Reader implementations.
 */
class BoundedReaderTest {

    private static final Duration HANG_TIMEOUT = Duration.ofSeconds(5);

    /**
     * This test verifies that a {@link BoundedReader} wrapped by a {@link LineNumberReader}
     * does not cause an infinite loop in {@link LineNumberReader#readLine()}.
     * <p>
     * This issue can occur if the underlying stream ends without a newline character (EOL).
     * The {@link BoundedReader} must correctly propagate the end-of-file (EOF) signal to prevent
     * the {@link LineNumberReader} from hanging while waiting for a line terminator that will never arrive.
     * </p>
     */
    @Test
    void readLineWithLineNumberReaderShouldNotHangOnStreamWithoutTrailingEol() {
        // Arrange: A multi-line string that does not end with a newline.
        final String input = "Line 1\nLine 2\nLine 3";
        final Reader sourceReader = new StringReader(input);

        // The bound is intentionally set larger than the input string length to ensure
        // that termination is governed by the underlying reader's EOF, not the bound.
        final int bound = input.length() + 1;
        final BoundedReader boundedReader = new BoundedReader(sourceReader, bound);

        // Act & Assert: The entire operation should complete without a timeout.
        assertTimeoutPreemptively(HANG_TIMEOUT, () -> {
            try (final LineNumberReader lineNumberReader = new LineNumberReader(boundedReader)) {
                // Read all lines to consume the reader.
                while (lineNumberReader.readLine() != null) {
                    // Loop until EOF is reached.
                }
                // Verify that the loop terminated after reading the correct number of lines.
                assertEquals(3, lineNumberReader.getLineNumber(), "Should have read 3 lines before terminating.");
            }
        });
    }
}