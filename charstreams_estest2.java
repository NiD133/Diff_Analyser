package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Writer;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    @Test
    public void toString_onConsumedReadable_returnsEmptyString() throws IOException {
        // Arrange: Create a Readable (a CharBuffer) with some initial content.
        CharBuffer readableBuffer = CharBuffer.wrap("test content");

        // Act 1: Consume the readable by copying its content to a writer.
        // This operation advances the buffer's internal position to the end,
        // leaving nothing left to be read.
        Writer discardingWriter = CharStreams.nullWriter();
        CharStreams.copy(readableBuffer, discardingWriter);

        // Act 2: Call toString() on the now-consumed readable.
        String result = CharStreams.toString(readableBuffer);

        // Assert: The result should be an empty string because the readable's
        // content has already been fully read.
        assertEquals("", result);
    }
}