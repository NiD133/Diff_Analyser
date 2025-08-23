package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;

/**
 * This test class contains tests for the SegmentConstantPool class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class SegmentConstantPool_ESTestTest61 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Verifies that getConstantPoolEntry() throws an IOException when provided with a negative index,
     * as constant pool indices must be non-negative.
     */
    @Test(timeout = 4000)
    public void getConstantPoolEntryShouldThrowIOExceptionForNegativeIndex() {
        // Arrange: Create a SegmentConstantPool. The CpBands can be null because
        // the index validation should occur before it's accessed.
        final SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        final int anyPoolType = SegmentConstantPool.ALL; // Use a valid type constant for clarity
        final long negativeIndex = -1L;                  // Use a simple, representative negative index

        // Act & Assert: Expect an IOException with a specific message.
        try {
            segmentConstantPool.getConstantPoolEntry(anyPoolType, negativeIndex);
            fail("Expected an IOException for a negative index, but no exception was thrown.");
        } catch (final IOException e) {
            final String expectedMessage = "Cannot have a negative index.";
            assertEquals("The exception message did not match the expected value.", expectedMessage, e.getMessage());
        }
    }
}