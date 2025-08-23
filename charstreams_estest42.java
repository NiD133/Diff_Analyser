package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import org.junit.Test;

public class CharStreams_ESTestTest42 extends CharStreams_ESTest_scaffolding {

    /**
     * Tests that appending a CharBuffer to a writer does not consume the buffer.
     *
     * <p>This behavior occurs because {@link PrintWriter#append(CharSequence)} internally calls the
     * CharSequence's {@code toString()} method. This creates a new string from the buffer's
     * remaining characters without advancing the buffer's position. The test uses
     * {@link CharStreams#nullWriter()} as a simple data sink.
     */
    @Test
    public void append_charBufferToWriter_doesNotChangeBufferState() {
        // Arrange
        Writer nullWriter = CharStreams.nullWriter();
        PrintWriter printWriter = new PrintWriter(nullWriter);

        CharBuffer buffer = CharStreams.createBuffer();
        int expectedPosition = buffer.position();
        int expectedRemaining = buffer.remaining();

        // Act
        printWriter.append(buffer);

        // Assert
        // The append operation should not have consumed the buffer or changed its position.
        assertEquals(
                "Buffer's position should remain unchanged after append",
                expectedPosition,
                buffer.position());
        assertEquals(
                "Buffer's remaining characters should be unchanged after append",
                expectedRemaining,
                buffer.remaining());
    }
}