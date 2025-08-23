package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on the initial state of the ByteArrayBuilder class.
 *
 * Note: The original test class 'ByteArrayBuilder_ESTestTest8' was refactored
 * to 'ByteArrayBuilderTest' for improved clarity and to remove auto-generation artifacts.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that a new ByteArrayBuilder, created with a specific initial
     * block size, correctly reports its current segment length as zero before
     * any data is written.
     */
    @Test
    public void newBuilderWithInitialSizeShouldHaveZeroCurrentSegmentLength() {
        // Arrange: Create a ByteArrayBuilder with a non-default initial block size.
        // The constructor allocates an internal buffer, but the pointer (_currBlockPtr)
        // remains at the start.
        final int initialBlockSize = 131072; // 128 KiB
        ByteArrayBuilder builder = new ByteArrayBuilder(initialBlockSize);

        // Act: Retrieve the length of the current segment.
        int currentLength = builder.getCurrentSegmentLength();

        // Assert: The length should be 0, as no data has been appended yet.
        assertEquals("A newly constructed ByteArrayBuilder should have a current segment length of 0",
                0, currentLength);
    }
}