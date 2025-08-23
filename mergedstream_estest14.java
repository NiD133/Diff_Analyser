package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.InputStream;
import java.io.PipedInputStream;

/**
 * Contains tests for the {@link MergedStream} class, focusing on edge cases and error handling.
 */
public class MergedStreamTest {

    /**
     * Verifies that calling read() on a MergedStream throws an ArrayIndexOutOfBoundsException
     * if the stream was constructed with a starting pointer that is outside the bounds of the
     * initial buffer.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void read_whenConstructedWithOutOfBoundsStartIndex_shouldThrowException() throws Exception {
        // Arrange: Set up a MergedStream where the initial read position in the
        //          pre-filled buffer is invalid.
        byte[] buffer = new byte[10];
        int outOfBoundsStartIndex = 100; // An index clearly outside the buffer's valid range [0-9]
        int endIndex = 100;
        InputStream underlyingStream = new PipedInputStream(); // A dummy stream, as it won't be reached.

        // The IOContext is not relevant for this specific failure path, so null is acceptable.
        MergedStream mergedStream = new MergedStream(null, underlyingStream, buffer, outOfBoundsStartIndex, endIndex);

        byte[] destination = new byte[10];

        // Act: Attempt to read from the stream. This should immediately try to access the
        // internal buffer at the invalid start index.
        mergedStream.read(destination);

        // Assert: The test is expected to throw an ArrayIndexOutOfBoundsException,
        // which is handled by the @Test(expected=...) annotation. The test will fail
        // if any other exception is thrown or if no exception is thrown.
    }
}