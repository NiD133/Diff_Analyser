package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the {@link SegmentConstantPool} class.
 * This specific test focuses on the {@code matchSpecificPoolEntryIndex} method.
 */
public class SegmentConstantPool_ESTestTest26 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Tests that matchSpecificPoolEntryIndex returns the index 0 when searching for a null value
     * and the first element in the array is the first match.
     */
    @Test
    public void matchSpecificPoolEntryIndexShouldReturnZeroForFirstMatchAtFirstPosition() {
        // Arrange
        // The method under test does not use any instance state, so passing null for CpBands is acceptable.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        String[] names = {null, "someString", "anotherString", null};
        String valueToFind = null;
        int desiredOccurrence = 0; // We are looking for the first occurrence (0-indexed).
        int expectedIndex = 0;

        // Act
        int actualIndex = segmentConstantPool.matchSpecificPoolEntryIndex(names, valueToFind, desiredOccurrence);

        // Assert
        assertEquals("Should return the index of the first element (0) as it's the first match.",
                     expectedIndex, actualIndex);
    }
}