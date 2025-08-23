package org.apache.commons.io.input;

import org.junit.Test;

import java.io.Reader;
import java.util.Collections;

/**
 * Contains tests for the {@link SequenceReader} class, focusing on its read behavior.
 */
public class SequenceReaderTest {

    /**
     * Verifies that read(char[], int, int) throws an IndexOutOfBoundsException
     * when the provided offset and length are invalid for the given buffer.
     * This is part of the standard contract for the java.io.Reader#read(char[], int, int) method.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void readWithBufferOutOfBoundsShouldThrowException() throws Exception {
        // Arrange: Create a SequenceReader. The content of the reader is irrelevant
        // for this test, as the exception is triggered by invalid buffer arguments.
        final SequenceReader sequenceReader = new SequenceReader(Collections.<Reader>emptyList());
        
        final char[] emptyBuffer = new char[0];
        final int invalidOffset = 1; // An offset that is out of bounds for an empty array.
        final int invalidLength = 1; // A length that is out of bounds for an empty array.

        // Act & Assert: Attempting to read with an offset and length that are
        // out of bounds for the empty buffer should throw an IndexOutOfBoundsException.
        // The assertion is handled by the @Test(expected=...) annotation.
        sequenceReader.read(emptyBuffer, invalidOffset, invalidLength);
    }
}