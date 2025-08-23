package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ByteArrayBuilder} class, focusing on its initial state.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that a newly created ByteArrayBuilder has a current segment length of 0.
     * This is the expected state before any data has been written to it.
     */
    @Test
    public void newBuilderShouldHaveZeroCurrentSegmentLength() {
        // Arrange: Create a new ByteArrayBuilder using its default constructor.
        // This is the simplest way to get an instance for testing its initial state.
        ByteArrayBuilder builder = new ByteArrayBuilder();

        // Act: Get the length of the current, empty segment.
        int currentSegmentLength = builder.getCurrentSegmentLength();

        // Assert: The length should be 0 for a new builder.
        assertEquals("Initial current segment length should be 0", 0, currentSegmentLength);
    }
}