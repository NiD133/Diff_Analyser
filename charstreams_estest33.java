package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    // The default buffer size used by CharStreams.createBuffer() is 2048 (0x800).
    private static final int DEFAULT_BUFFER_SIZE = 2048;

    /**
     * Tests that {@link CharStreams#exhaust(Readable)} fully consumes a {@link CharBuffer}
     * and returns the total number of characters read.
     */
    @Test
    public void exhaust_onFullBuffer_readsAllCharactersAndReturnsCount() {
        // Arrange: Create a new buffer, which is full and ready for reading.
        CharBuffer buffer = CharStreams.createBuffer();
        assertEquals("Precondition: Buffer should be full upon creation.",
                DEFAULT_BUFFER_SIZE, buffer.remaining());

        // Act: Exhaust the buffer by reading all its characters.
        long charsRead = CharStreams.exhaust(buffer);

        // Assert: Verify that the correct number of characters was read and the buffer is now empty.
        assertEquals("The number of characters read should match the buffer's capacity.",
                DEFAULT_BUFFER_SIZE, charsRead);
        assertEquals("The buffer's position should be at the end after being exhausted.",
                DEFAULT_BUFFER_SIZE, buffer.position());
        assertEquals("The buffer should have no characters remaining after being exhausted.",
                0, buffer.remaining());
    }
}