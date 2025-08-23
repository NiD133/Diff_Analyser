package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.io.InputStream;
import java.io.PipedInputStream;

/**
 * Unit tests for the {@link MergedStream} class.
 */
public class MergedStreamTest {

    /**
     * Verifies that the {@code markSupported()} method correctly returns {@code false},
     * as {@link MergedStream} is not designed to support mark/reset operations.
     */
    @Test
    public void markSupportedShouldReturnFalse() {
        // Arrange: Create a MergedStream instance.
        // The specific underlying stream and buffer content are not relevant for this test,
        // as markSupported() is expected to always return false.
        InputStream underlyingInputStream = new PipedInputStream();
        byte[] buffer = new byte[16];
        MergedStream mergedStream = new MergedStream(null, underlyingInputStream, buffer, 0, 0);

        // Act & Assert: Call markSupported() and verify it returns false.
        assertFalse("MergedStream is not expected to support mark/reset operations.",
                mergedStream.markSupported());
    }
}