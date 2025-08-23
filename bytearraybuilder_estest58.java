package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@link ByteArrayBuilder#appendThreeBytes(int)}
     * correctly increases the current segment's length by exactly 3.
     */
    @Test
    public void appendThreeBytesShouldAdvancePointerByThree() {
        // Arrange: Create a ByteArrayBuilder. The default initial capacity is sufficient.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        assertEquals("Builder should be empty upon creation", 0, builder.getCurrentSegmentLength());

        // Act: Append three bytes to the builder. The specific integer value is not
        // relevant to this test, as we are only checking the length.
        builder.appendThreeBytes(0x123456);

        // Assert: The length of the current segment should now be 3.
        int expectedLength = 3;
        assertEquals("After appending three bytes, the segment length should be 3",
                expectedLength, builder.getCurrentSegmentLength());
    }
}