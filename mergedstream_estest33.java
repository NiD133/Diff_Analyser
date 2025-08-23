package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Contains tests for the {@link MergedStream} class.
 */
public class MergedStreamTest {

    /**
     * Tests that calling {@link MergedStream#mark(int)} with a negative readlimit
     * does not throw an exception.
     * <p>
     * This scenario also uses a {@link MergedStream} constructed with invalid
     * (negative) buffer indices to ensure the method is robust even when the
     * object is in an unusual state. The test's success is confirmed by the
     * absence of any thrown exceptions.
     */
    @Test
    public void markWithNegativeReadlimitShouldNotThrowException() throws IOException {
        // Arrange: Create a MergedStream with an invalid state (negative indices)
        // to test the robustness of the mark() method.
        InputStream underlyingStream = new ByteArrayInputStream(new byte[0]);
        byte[] prefixBuffer = new byte[1];
        int invalidStartIndex = -1;
        int invalidEndIndex = -1;

        // The IOContext is not relevant for this specific test, so it can be null.
        MergedStream mergedStream = new MergedStream(null, underlyingStream, prefixBuffer, invalidStartIndex, invalidEndIndex);

        // Act & Assert: Call mark() with a negative value.
        // The test passes if this operation completes without throwing an exception.
        // The InputStream contract does not specify behavior for a negative readlimit,
        // so we are verifying that our implementation handles it gracefully.
        int negativeReadlimit = -1;
        mergedStream.mark(negativeReadlimit);
    }
}