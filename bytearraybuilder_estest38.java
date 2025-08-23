package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test suite focuses on the behavior of the ByteArrayBuilder class
 * under exceptional or edge-case conditions.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that attempting to append bytes when the internal buffer pointer
     * is in an invalid state (i.e., a negative index) correctly throws an
     * ArrayIndexOutOfBoundsException. This simulates a corrupted internal state
     * to ensure the class behaves safely.
     */
    @Test
    public void appendThreeBytes_whenCurrentSegmentLengthIsNegative_shouldThrowArrayIndexOutOfBounds() {
        // Arrange: Create a ByteArrayBuilder and manually set its internal state to be invalid.
        // This is not a normal use case, but it tests the robustness of the append methods.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        int invalidNegativeLength = -1;
        builder.setCurrentSegmentLength(invalidNegativeLength);

        // Act & Assert
        try {
            // The integer argument here is irrelevant, as the exception is triggered by the invalid index.
            builder.appendThreeBytes(999);
            fail("Expected an ArrayIndexOutOfBoundsException because the current segment length is negative.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Optional: Verify the exception message to ensure it reports the correct invalid index.
            // This makes the test more specific and helps in debugging.
            assertEquals(String.valueOf(invalidNegativeLength), e.getMessage());
        }
    }
}