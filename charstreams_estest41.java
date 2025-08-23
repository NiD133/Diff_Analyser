package com.google.common.io;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Writer;
import org.junit.Test;

/**
 * Contains tests for the {@link CharStreams} utility class.
 */
public class CharStreamsTest {

    /**
     * Verifies that the Writer from {@link CharStreams#nullWriter()} accepts and
     * discards all forms of input without throwing an exception. This ensures
     * it behaves like a "/dev/null" for character streams.
     */
    @Test
    public void nullWriter_shouldAcceptAndDiscardAllInputWithoutError() {
        // Arrange: Obtain the writer that should discard all output.
        Writer nullWriter = CharStreams.nullWriter();
        assertNotNull("CharStreams.nullWriter() should not return null.", nullWriter);

        try {
            // Act: Perform a series of write, append, flush, and close operations.
            nullWriter.write('a');
            nullWriter.write("a test string");
            nullWriter.write("another string".toCharArray(), 2, 5); // writes "other"
            nullWriter.append("a char sequence");
            nullWriter.append('z');
            nullWriter.flush();
            nullWriter.close();

            // Assert: The test succeeds if no exceptions were thrown.
            // The try-catch block explicitly enforces this assertion.

        } catch (IOException e) {
            // The nullWriter should never throw an IOException.
            fail("CharStreams.nullWriter() should not have thrown an IOException, but it did: " + e.getMessage());
        }
    }
}