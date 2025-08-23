package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader}.
 */
class SequenceReaderTest {

    /**
     * Tests that after a {@link SequenceReader} is closed, subsequent calls to {@code read()}
     * consistently return {@code EOF}, as per the {@link Reader#close()} contract.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void readAfterCloseShouldReturnEof() throws IOException {
        // Arrange: Create a SequenceReader with some data.
        final Reader reader = new SequenceReader(new CharSequenceReader("FooBar"));

        // Act & Assert (Pre-condition): Read some data to confirm the reader is active
        // and not at the end of the stream.
        assertEquals('F', reader.read());
        assertEquals('o', reader.read());
        assertEquals('o', reader.read());

        // Act: Close the reader. This is the action under test.
        reader.close();

        // Assert: Verify that subsequent reads return EOF.
        assertEquals(EOF, reader.read(), "First read after close should return EOF.");
        assertEquals(EOF, reader.read(), "Second read after close should also return EOF to ensure state.");
    }
}