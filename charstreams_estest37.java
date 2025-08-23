package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    /**
     * Verifies that readLines consumes the entire Readable, even when it contains no newline
     * characters.
     */
    @Test
    public void readLines_withReadableContainingNoNewlines_consumesEntireReadable() throws IOException {
        // Arrange: Create a CharBuffer to act as a Readable.
        // The buffer is filled with 2048 null characters and has no newlines.
        CharBuffer sourceBuffer = CharStreams.createBuffer();
        int bufferCapacity = sourceBuffer.capacity();

        // Act: Call readLines, which should read the entire buffer and return its
        // content as a single line.
        List<String> lines = CharStreams.readLines(sourceBuffer);

        // Assert:
        // 1. The buffer should be fully consumed.
        assertFalse("The buffer should have no remaining characters after being read.", sourceBuffer.hasRemaining());
        assertEquals("The buffer's position should be at its limit.", bufferCapacity, sourceBuffer.position());

        // 2. The returned list should contain one line with all the buffer's content.
        assertEquals("Expected a single line to be read from the buffer.", 1, lines.size());
        assertEquals("The length of the read line should match the buffer's capacity.",
                bufferCapacity, lines.get(0).length());
    }
}