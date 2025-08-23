package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link SegmentConstantPool} class.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that matchSpecificPoolEntryIndex() returns -1 when the desired index
     * of a matching entry does not exist in the array.
     * <p>
     * This test case specifically verifies the correct handling of:
     * 1. A search term that is {@code null}.
     * 2. A desired match index that is out of bounds (i.e., greater than the
     *    number of actual matches found).
     * </p>
     */
    @Test
    public void matchSpecificPoolEntryIndexShouldReturnNegativeOneWhenDesiredIndexIsOutOfBounds() {
        // Arrange
        // The method under test does not rely on the state of the SegmentConstantPool instance,
        // so we can pass null for its constructor dependency (CpBands).
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Create an array with four null elements to search within.
        String[] names = new String[4]; // Equivalent to [null, null, null, null]
        String stringToMatch = null;

        // We are looking for the 4th occurrence (zero-indexed) of `null`.
        // The array contains `null` at indices 0, 1, 2, and 3.
        // Therefore, the highest valid desired match index is 3.
        // Requesting an index of 4 is out of bounds.
        int desiredMatchIndex = 4;

        // Act
        int actualIndex = segmentConstantPool.matchSpecificPoolEntryIndex(names, stringToMatch, desiredMatchIndex);

        // Assert
        assertEquals("Expected -1 because the desired match index (4) was not found.", -1, actualIndex);
    }
}