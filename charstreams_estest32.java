package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    /**
     * Tests that calling {@link CharStreams#skipFully(Reader, long)} with a negative
     * number of characters to skip does not read from the reader or throw an exception.
     * The reader's position should remain unchanged.
     */
    @Test
    public void skipFully_withNegativeCount_shouldDoNothing() throws IOException {
        // Arrange
        String originalContent = "This is the original content of the reader.";
        StringReader reader = new StringReader(originalContent);

        // Act
        // Attempt to skip a negative number of characters. Based on the implementation,
        // this should be a no-op.
        CharStreams.skipFully(reader, -100L);

        // Assert
        // Verify that the reader's content is still intact by reading it to a string.
        String remainingContent = CharStreams.toString(reader);
        assertEquals("Reader content should be unchanged after skipping a negative count.",
                originalContent, remainingContent);
    }
}