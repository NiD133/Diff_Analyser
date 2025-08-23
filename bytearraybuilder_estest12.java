package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that setting the current segment's length correctly updates both
     * the reported segment length and the builder's total size.
     *
     * This is important for scenarios where the builder's internal buffer is
     * manipulated directly and the builder needs to be informed of the new size.
     */
    @Test
    public void setCurrentSegmentLength_ShouldUpdateBothSegmentLengthAndTotalSize() {
        // Arrange: Create a new ByteArrayBuilder. The default constructor is sufficient.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        final int newLength = 100;

        // Act: Manually set the length of the current data segment.
        builder.setCurrentSegmentLength(newLength);

        // Assert: Check that both the current segment length and the total size
        // are updated to the specified value.
        assertEquals("The current segment length should be updated to the new value.",
                newLength, builder.getCurrentSegmentLength());

        assertEquals("The total size of the builder should reflect the new segment length.",
                newLength, builder.size());
    }
}