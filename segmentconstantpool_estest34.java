package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class focuses on the SegmentConstantPool. The original test name
 * "SegmentConstantPool_ESTestTest34" suggests it was auto-generated. For clarity,
 * a manually written test class would typically be named "SegmentConstantPoolTest".
 */
public class SegmentConstantPool_ESTestTest34 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Verifies that matchSpecificPoolEntryIndex correctly returns the index
     * of the first occurrence of a matching string in an array.
     */
    @Test
    public void matchSpecificPoolEntryIndexShouldReturnIndexOfFirstMatch() {
        // Arrange
        // The method under test does not use any instance fields, so passing null
        // for the CpBands dependency is acceptable for this test.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        final String stringToFind = "^<init>.*";
        final String[] names = {"someMethod()V", stringToFind, "anotherMethod()I", stringToFind};
        final int desiredOccurrence = 0; // We are looking for the first (0-indexed) occurrence.
        final int expectedIndex = 1;

        // Act
        final int actualIndex = segmentConstantPool.matchSpecificPoolEntryIndex(names, stringToFind, desiredOccurrence);

        // Assert
        assertEquals("The method should find the index of the first matching string.", expectedIndex, actualIndex);
    }
}