package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the {@link ByteArrayBuilder}.
 * The original test was auto-generated, leading to a non-obvious setup.
 * This version has been refactored for clarity.
 */
public class ByteArrayBuilderRefactoredTest {

    /**
     * Tests that writing a byte array larger than the builder's initial segment
     * correctly triggers multiple new segment allocations and that the builder's
     * state is updated accurately.
     */
    @Test
    public void write_whenDataExceedsSegmentSize_shouldAllocateNewSegmentsAndUpdateLength() {
        // Arrange

        // Create the primary builder to be tested. By default, its first segment
        // has a size of 500 bytes (ByteArrayBuilder.INITIAL_BLOCK_SIZE).
        ByteArrayBuilder builder = new ByteArrayBuilder();

        // The original test used a convoluted method to create a large byte array.
        // We replicate it here to maintain the test's original intent.
        // 1. A 'sourceBuilder' is created with an empty initial block but an
        //    artificially high current length (2000). This is an unusual state.
        ByteArrayBuilder sourceBuilder = ByteArrayBuilder.fromInitial(ByteArrayBuilder.NO_BYTES, 2000);

        // 2. `finishCurrentSegment()` uses this artificial length to calculate the size
        //    of a new segment, resulting in a 3000-byte array (2000 + 2000/2).
        byte[] largeDataToWrite = sourceBuilder.finishCurrentSegment();

        // Act

        // Write the 3000-byte array to the primary builder. This operation will:
        // 1. Fill the initial 500-byte segment.
        // 2. Repeatedly allocate new 500-byte segments until all 3000 bytes are written.
        //    (The allocation logic grows based on past length, resulting in 500-byte chunks here).
        // 3. The final allocated segment will be completely filled with the last 500 bytes.
        builder.write(largeDataToWrite);

        // Assert

        // After `finishCurrentSegment`, the source builder's new current segment should be empty.
        assertEquals("The source builder's current segment should be empty after finishing.",
                0, sourceBuilder.getCurrentSegmentLength());

        // The length of the *current* segment in the main builder should be 500, as the
        // last chunk of the write operation completely filled the last allocated segment.
        assertEquals("The main builder's current segment should be full.",
                500, builder.getCurrentSegmentLength());
    }
}