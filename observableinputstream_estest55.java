package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Tests for {@link ObservableInputStream} focusing on its creation via the {@link ObservableInputStream.Builder}.
 */
public class ObservableInputStream_ESTestTest55 { // Note: Original class name retained for context.

    /**
     * Tests that an ObservableInputStream, created from a CharBuffer source using the builder,
     * correctly reads the first byte. A newly allocated CharBuffer is filled with null
     * characters ('\u0000'), which have a byte value of 0.
     */
    @Test
    public void readShouldReturnFirstByteFromCharSequenceSource() throws IOException {
        // Arrange
        final int bufferSize = 10;
        // A CharBuffer allocated this way is filled with null characters ('\u0000').
        final CharBuffer sourceBuffer = CharBuffer.allocate(bufferSize);

        final ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(sourceBuffer);
        final ObservableInputStream inputStream = builder.get();

        // Act
        final int firstByteRead = inputStream.read();

        // Assert
        // The byte representation of a null character is 0.
        final int expectedByte = 0;
        assertEquals("The first byte read from a buffer of null characters should be 0.",
                expectedByte, firstByteRead);
    }
}