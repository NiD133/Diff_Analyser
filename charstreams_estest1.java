package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    /**
     * Verifies that calling {@link CharStreams#skipFully(Reader, long)} with a skip count of zero
     * completes successfully without consuming any characters from the reader.
     */
    @Test
    public void skipFully_withZeroCharsToSkip_doesNothing() throws IOException {
        // Arrange: Create a reader with known content.
        String originalContent = "abcdef-123456";
        Reader reader = new StringReader(originalContent);

        // Act: Attempt to skip 0 characters.
        CharStreams.skipFully(reader, 0L);

        // Assert: Verify that the reader's content remains unchanged.
        String remainingContent = CharStreams.toString(reader);
        assertEquals(originalContent, remainingContent);
    }
}