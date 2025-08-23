package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.io.InputStream;
import java.io.PipedInputStream;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link MergedStream} class.
 */
public class MergedStreamTest {

    /**
     * Verifies that {@link MergedStream#markSupported()} correctly delegates the call
     * to the underlying input stream.
     * <p>
     * This test uses a {@link PipedInputStream}, which does not support marking,
     * to confirm that MergedStream returns false in this scenario.
     */
    @Test
    public void markSupportedShouldReturnFalseWhenUnderlyingStreamDoesNotSupportIt() {
        // Arrange
        // Use an underlying stream that is known not to support mark/reset.
        InputStream underlyingStream = new PipedInputStream();

        // Create a MergedStream instance wrapping the underlying stream.
        // The buffer and IOContext are not relevant for this test.
        MergedStream mergedStream = new MergedStream(null, underlyingStream, null, 0, 0);

        // Act
        boolean isMarkSupported = mergedStream.markSupported();

        // Assert
        assertFalse("MergedStream should report mark is not supported if the underlying stream does not.", isMarkSupported);
    }
}