package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import org.junit.Test;

/**
 * Tests for the {@link CharStreams} utility class.
 */
public class CharStreamsTest {

    /**
     * Verifies that copying from a Reader to a Writer:
     * 1. Copies all characters and returns the correct count on the first attempt.
     * 2. Copies zero characters on a subsequent attempt after the reader is exhausted.
     */
    @Test
    public void copyFromReaderToWriter_copiesAllContentOnFirstCallAndNothingOnSecond() throws IOException {
        // Arrange: Create a reader with known content and a writer that discards output.
        String content = "Funnels.unencodedCharsFunnel()";
        Reader reader = new StringReader(content);
        Writer writer = CharStreams.nullWriter();

        // Act & Assert: First copy
        // The reader is new, so all content should be copied.
        long charsCopied = CharStreams.copyReaderToWriter(reader, writer);
        assertEquals(
                "The number of copied characters should match the input string length.",
                (long) content.length(),
                charsCopied);

        // Act & Assert: Second copy
        // The reader has been exhausted by the first copy, so no more content should be read.
        long additionalCharsCopied = CharStreams.copyReaderToWriter(reader, writer);
        assertEquals(
                "Copying from an exhausted reader should result in zero characters copied.",
                0L,
                additionalCharsCopied);
    }
}