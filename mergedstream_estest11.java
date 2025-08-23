package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Unit tests for the {@link MergedStream} class, focusing on its read behavior
 * when delegating to the underlying input stream.
 */
public class MergedStream_ESTestTest11 {

    /**
     * Tests that a call to {@code MergedStream.read(byte[], int, int)} with invalid
     * arguments (a negative offset) correctly propagates the {@link IndexOutOfBoundsException}
     * from the underlying stream when the MergedStream's internal buffer is exhausted.
     */
    @Test
    public void readWithNegativeOffset_whenBufferExhausted_propagatesExceptionFromUnderlyingStream() throws IOException {
        // Arrange: Set up a MergedStream with an exhausted internal buffer to ensure
        // it delegates read operations to the underlying stream.
        byte[] prefixBuffer = new byte[16];
        int bufferStart = 8;
        int bufferEnd = 8; // A start equal to end signifies an empty/exhausted buffer segment.

        // Use a simple, predictable stream as the underlying source.
        InputStream underlyingStream = new ByteArrayInputStream(new byte[0]);

        // The IOContext is not relevant to this test's logic, so null is acceptable.
        MergedStream mergedStream = new MergedStream(null, underlyingStream, prefixBuffer, bufferStart, bufferEnd);

        byte[] targetBuffer = new byte[10];
        int invalidOffset = -1; // A negative offset is invalid per the InputStream#read contract.
        int lengthToRead = 0;

        // Act & Assert: Attempt to read with the invalid offset and verify that the
        // expected exception is thrown.
        try {
            mergedStream.read(targetBuffer, invalidOffset, lengthToRead);
            fail("Expected an IndexOutOfBoundsException due to the negative offset, but none was thrown.");
        } catch (IndexOutOfBoundsException e) {
            // This is the expected outcome. The MergedStream correctly upholds the
            // InputStream contract by propagating the exception from the delegate.
        }
    }
}