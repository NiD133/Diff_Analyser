package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.CharBuffer;

// The test class name is preserved from the original context.
public class CharSequenceReader_ESTestTest30 extends CharSequenceReader_ESTest_scaffolding {

    /**
     * Verifies that attempting to read from a reader after it has been closed
     * throws an IOException.
     */
    @Test
    public void readIntoBuffer_whenReaderIsClosed_throwsIOException() throws IOException {
        // Arrange: Create a reader and then close it.
        CharSequence sequence = CharBuffer.wrap(new char[3]);
        CharSequenceReader reader = new CharSequenceReader(sequence);
        reader.close();

        char[] destinationBuffer = new char[3];

        // Act & Assert: Attempting to read should throw an IOException.
        // We use assertThrows for a clear and concise exception check.
        IOException thrown = assertThrows(
            IOException.class,
            () -> reader.read(destinationBuffer, 0, 0)
        );

        // Further assert on the exception details for more precise testing.
        assertEquals("reader closed", thrown.getMessage());
    }
}