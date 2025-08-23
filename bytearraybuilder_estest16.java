package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ByteArrayBuilder} class, focusing on its segment management.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that calling {@code finishCurrentSegment()} on a new builder correctly
     * accounts for the full size of the initial, unused segment.
     */
    @Test
    public void finishCurrentSegment_onNewBuilder_updatesSizeToInitialBlockSize() {
        // Arrange
        // From the source, the default initial block size is 500.
        final int initialBlockSize = 500;
        ByteArrayBuilder builder = new ByteArrayBuilder();

        // Act
        // "Finish" the current segment. This action moves the entire initial block
        // to the list of completed segments, making its full capacity part of the builder's size.
        builder.finishCurrentSegment();

        // Assert
        // The builder's size should now be the size of that first block.
        assertEquals("Builder size should equal the initial block size",
                initialBlockSize, builder.size());

        // Further, getting the contents should yield a byte array of that same size.
        byte[] result = builder.getClearAndRelease();
        assertEquals("Resulting byte array length should match initial block size",
                initialBlockSize, result.length);
    }
}