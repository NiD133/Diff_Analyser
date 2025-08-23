package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link MergedStream} class.
 * This class name is retained from the original auto-generated test suite.
 */
public class MergedStream_ESTestTest22 extends MergedStream_ESTest_scaffolding {

    /**
     * Tests that calling skip() with a negative number of bytes is a no-op
     * that returns the provided negative number.
     *
     * <p>Note: The standard contract for {@link InputStream#skip(long)} implies
     * a non-negative return value representing the number of bytes actually skipped.
     * This test verifies a specific, non-standard behavior of this implementation.
     */
    @Test
    public void skipWithNegativeArgumentShouldReturnTheArgument() throws IOException {
        // Arrange
        final long negativeBytesToSkip = -629L;

        // Use minimal dependencies since the internal state of the stream and buffer
        // should not affect the handling of a negative skip value.
        InputStream dummyInputStream = new ByteArrayInputStream(new byte[0]);
        byte[] emptyBuffer = new byte[0];
        MergedStream mergedStream = new MergedStream(null, dummyInputStream, emptyBuffer, 0, 0);

        // Act
        long actualBytesSkipped = mergedStream.skip(negativeBytesToSkip);

        // Assert
        assertEquals("The skip() method should return the negative argument it was given.",
                negativeBytesToSkip, actualBytesSkipped);
    }
}