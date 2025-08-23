package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 * This class focuses on improving a single test case for clarity.
 */
public class RandomAccessFileOrArray_ESTestTest8 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that readLongLE() throws an EOFException when attempting to read
     * beyond the end of the underlying data source.
     *
     * A long requires 8 bytes, but this test sets up a condition where the read pointer
     * is at the end of a 5-byte source, making a full read impossible.
     */
    @Test(expected = EOFException.class)
    public void readLongLE_shouldThrowEOFException_whenReadingPastEndOfStream() throws IOException {
        // Arrange: Create a reader with a 5-byte source. A long is 8 bytes, so there
        // isn't enough data to begin with.
        byte[] sourceBytes = new byte[5];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceBytes);

        // To ensure we are at the end of the stream, we consume all available bytes.
        // readLine() will read all 5 bytes since there is no newline character.
        reader.readLine();

        // Act & Assert: Attempt to read a long (8 bytes) from the end of the stream.
        // This action is expected to throw an EOFException, which is verified by the
        // @Test(expected = ...) annotation.
        reader.readLongLE();
    }
}