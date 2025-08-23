package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ByteArrayBuilder} class, focusing on its internal state management.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests the behavior when a negative length is set for the current segment.
     *
     * This is an edge-case test to verify that if {@code setCurrentSegmentLength} is called
     * with a negative value, the builder's internal state (both current segment length
     * and total size) reflects this value directly. This documents the current behavior
     * for unconventional inputs.
     */
    @Test
    public void setCurrentSegmentLength_withNegativeValue_shouldUpdateInternalState() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        final int negativeLength = -1;

        // Act
        builder.setCurrentSegmentLength(negativeLength);

        // Assert
        // The total size is calculated as the sum of past block lengths plus the current segment length.
        // Since there are no past blocks, the size should equal the negative length set.
        assertEquals("Total size should reflect the negative current segment length",
                negativeLength, builder.size());

        // The getter for the current segment length should return the value that was set.
        assertEquals("Current segment length should be the negative value that was set",
                negativeLength, builder.getCurrentSegmentLength());
    }
}