package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Contains tests for the {@link ProxyReader} class, focusing on the skip() method.
 */
public class ProxyReaderTest {

    /**
     * Tests that calling skip() with a number larger than the remaining characters
     * in the stream skips only up to the end of the stream and returns the
     * number of characters actually skipped.
     */
    @Test
    public void skipShouldReturnActualCharactersSkippedWhenAttemptingToSkipPastEndOfStream() throws IOException {
        // Arrange
        final String inputData = "1234567890"; // A simple string with a clear length of 10.
        final long inputLength = inputData.length();
        final long charactersToSkip = inputLength + 5; // A number explicitly larger than the input.

        // The class under test, ProxyReader, is abstract.
        // We use a concrete subclass, TaggedReader, for instantiation.
        final ProxyReader proxyReader = new TaggedReader(new StringReader(inputData));

        // Act
        final long actualSkipped = proxyReader.skip(charactersToSkip);

        // Assert
        assertEquals("The number of skipped characters should equal the total length of the input string.",
                inputLength, actualSkipped);
    }
}