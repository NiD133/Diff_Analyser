package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

// Note: The original test class name and inheritance from a scaffolding class
// have been preserved, as they are likely part of a larger generated test suite.
public class SegmentConstantPool_ESTestTest44 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Verifies that the {@link SegmentConstantPool#getValue(int, long)} method
     * throws a {@link Pack200Exception} when a negative index is provided.
     * The method should not permit negative indices as they are invalid.
     */
    @Test(timeout = 4000)
    public void getValueShouldThrowExceptionForNegativeIndex() {
        // Arrange: Create a SegmentConstantPool. The CpBands can be null for this test
        // as the index validity check occurs before the bands are accessed.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);
        final int constantPoolType = SegmentConstantPool.CP_CLASS;
        final long negativeIndex = -1L;

        // Act & Assert
        try {
            segmentConstantPool.getValue(constantPoolType, negativeIndex);
            fail("Expected a Pack200Exception because the index cannot be negative.");
        } catch (Pack200Exception e) {
            // The method correctly threw an exception. Now, verify its message.
            assertEquals("Cannot have a negative range", e.getMessage());
        }
    }
}