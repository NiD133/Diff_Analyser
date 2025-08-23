package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that the static factory method {@code fromInitial} accepts a negative length
     * and that {@code size()} subsequently returns this negative value.
     *
     * This test covers an edge case where the builder is initialized with an invalid
     * (negative) length. The current implementation trusts the provided length argument
     * without validation, and this test verifies that behavior.
     */
    @Test
    public void fromInitial_withNegativeLength_shouldReturnNegativeSize() {
        // Arrange
        final int negativeInitialLength = -219;
        // The initial buffer is empty, but we are telling the builder its "size" is negative.
        final byte[] emptyBuffer = ByteArrayBuilder.NO_BYTES;

        // Act
        // Create a ByteArrayBuilder with a pre-existing buffer and a specified length.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(emptyBuffer, negativeInitialLength);
        int actualSize = builder.size();

        // Assert
        assertEquals("The size should reflect the negative length provided at creation.",
                negativeInitialLength, actualSize);
    }
}