package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Contains tests for the {@link ObservableInputStream} class, focusing on its builder functionality.
 */
public class ObservableInputStream_ESTestTest47 { // Retaining original class name for context

    /**
     * Tests that an ObservableInputStream created via its builder with a CharSequence
     * as a source can be read into a byte array successfully.
     */
    @Test
    public void testReadIntoByteArrayFromStreamBuiltWithCharSequence() throws IOException {
        // Arrange
        // Create a source CharSequence with sufficient data.
        final int sourceCapacity = 100;
        final CharBuffer sourceCharSequence = CharBuffer.allocate(sourceCapacity);

        // Use the builder to create an ObservableInputStream from the CharSequence.
        final ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(sourceCharSequence);
        final ObservableInputStream inputStream = builder.get();

        // Prepare a destination buffer to read data into.
        final int bufferSize = 5;
        final byte[] destinationBuffer = new byte[bufferSize];

        // Act
        // Attempt to read from the stream, filling the destination buffer.
        final int bytesRead = inputStream.read(destinationBuffer);

        // Assert
        // Verify that the number of bytes read is equal to the buffer's size,
        // since the source had enough data to fill it completely.
        assertEquals("The number of bytes read should match the buffer size.", bufferSize, bytesRead);
    }
}