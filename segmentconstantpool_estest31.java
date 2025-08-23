package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * This test suite focuses on the SegmentConstantPool class.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that {@link SegmentConstantPool#matchSpecificPoolEntryIndex(String[], String[], String, String, int)}
     * throws an {@link ArrayIndexOutOfBoundsException} when the secondary array is shorter than the primary array.
     * The method's contract implies that both arrays should be of the same length, as it iterates through them
     * concurrently.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void matchSpecificPoolEntryIndexShouldThrowExceptionWhenArraysHaveMismatchedLengths() {
        // Arrange: Create a SegmentConstantPool instance. The CpBands can be null as the
        // method under test does not rely on it.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Arrange: Set up arrays with mismatched lengths to trigger the exception.
        // A primary array with one element is sufficient to cause an access attempt on the empty secondary array.
        String[] primaryArray = new String[]{"java/lang/Object"};
        String[] secondaryArray = new String[0];

        // Arrange: The values for comparison and the desired index are not relevant to this test,
        // as the exception occurs during array iteration before these are used.
        String primaryCompareString = "any-class-name";
        String secondaryCompareRegex = "any-method-name";
        int desiredIndex = 0;

        // Act: Call the method with a primary array that is longer than the secondary array.
        // This is expected to throw an ArrayIndexOutOfBoundsException.
        segmentConstantPool.matchSpecificPoolEntryIndex(
            primaryArray,
            secondaryArray,
            primaryCompareString,
            secondaryCompareRegex,
            desiredIndex
        );

        // Assert: The test will pass if the expected ArrayIndexOutOfBoundsException is thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}