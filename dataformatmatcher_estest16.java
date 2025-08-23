package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link DataFormatMatcher} constructor's input validation.
 */
// The original test class name is preserved for context.
public class DataFormatMatcher_ESTestTest16 extends DataFormatMatcher_ESTest_scaffolding {

    /**
     * Verifies that the DataFormatMatcher constructor throws an IllegalArgumentException
     * when the provided buffer start offset and length combined exceed the buffer's bounds.
     */
    @Test
    public void constructorShouldThrowExceptionForOutOfBoundsBufferRange() {
        // Arrange: Set up an empty buffer and parameters that are clearly out of bounds.
        byte[] emptyBuffer = new byte[0];
        InputStream inputStream = new ByteArrayInputStream(emptyBuffer);
        JsonFactory jsonFactory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.INCONCLUSIVE;

        // The constructor validates that (start + length) is not greater than the buffer's length.
        // Here, we use values that will fail this check against a zero-length buffer.
        int outOfBoundsStart = 34;
        int outOfBoundsLength = 34;

        String expectedMessage = String.format(
            "Illegal start/length (%d/%d) wrt input array of %d bytes",
            outOfBoundsStart, outOfBoundsLength, emptyBuffer.length
        );

        // Act & Assert
        try {
            new DataFormatMatcher(inputStream, emptyBuffer, outOfBoundsStart, outOfBoundsLength, jsonFactory, matchStrength);
            fail("Expected an IllegalArgumentException because the start/length is out of bounds.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, confirming the right validation failed.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}