package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that `completeAndCoalesce` correctly combines previous segments with a
     * specified portion of the current, partially-filled segment.
     *
     * This scenario is triggered by appending data that exceeds the initial buffer's capacity,
     * which forces the allocation of a new segment. The test then coalesces the old segment
     * with only a part of the new one.
     */
    @Test
    public void completeAndCoalesce_whenSegmentIsPartiallyUsed_thenReturnsCorrectBytes() {
        // Arrange: Create a builder with a very small initial buffer (size 1)
        // to easily force a new segment to be allocated.
        final int initialCapacity = 1;
        ByteArrayBuilder builder = new ByteArrayBuilder(initialCapacity);

        // Act: Append three bytes for the integer 1 (0x000001).
        // 1. The first byte (0) fills the initial buffer.
        // 2. The second byte (0) triggers allocation of a new, larger segment.
        // 3. The second and third bytes (0, 1) are written to this new segment.
        builder.appendThreeBytes(1);

        // Assert: Verify the intermediate state after appending.
        // The first segment (size 1) is now in "past blocks", and the new current
        // segment contains two bytes.
        assertEquals("Current segment should contain 2 bytes after overflow", 2, builder.getCurrentSegmentLength());

        // Act: Finalize the byte array, but instruct it to only take 1 byte from the current segment.
        final int bytesToTakeFromCurrentSegment = 1;
        byte[] result = builder.completeAndCoalesce(bytesToTakeFromCurrentSegment);

        // Assert: The final array should contain the full first segment (1 byte) plus
        // the specified portion of the current segment (1 byte).
        // Expected content: [0] from the first segment, and the first [0] from the second segment.
        int expectedTotalLength = initialCapacity + bytesToTakeFromCurrentSegment;
        assertEquals("Final array length should be sum of old segments and partial new one",
                expectedTotalLength, result.length);

        byte[] expectedBytes = new byte[]{0, 0};
        assertArrayEquals("Final array content should be correct", expectedBytes, result);
    }
}