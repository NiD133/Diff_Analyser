package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.junit.Test;

/**
 * Tests for the static utility methods in {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Verifies that the {@code toIndex} method correctly converts a long value of 0 to an integer 0.
     * This tests the simplest, non-exceptional case for the conversion logic.
     */
    @Test
    public void toIndexShouldConvertZeroLongToZeroInt() throws Pack200Exception {
        // Arrange
        final long inputIndex = 0L;
        final int expectedIndex = 0;

        // Act
        final int actualIndex = SegmentConstantPool.toIndex(inputIndex);

        // Assert
        assertEquals(expectedIndex, actualIndex);
    }
}