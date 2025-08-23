package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * This test class contains tests for the SegmentConstantPool class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class SegmentConstantPool_ESTestTest63 extends SegmentConstantPool_ESTest_scaffolding {

    /**
     * Verifies that matchSpecificPoolEntryIndex throws a NullPointerException when the
     * primary array being searched contains a null element. The method is expected to
     * throw the exception when it attempts to call .equals() on the null element.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void matchSpecificPoolEntryIndexShouldThrowNPEWhenPrimaryArrayContainsNull() {
        // Arrange
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Create an array with a non-null element followed by a null. The method will
        // process the first element and then throw an NPE upon reaching the second.
        String[] arrayWithNull = {"someValue", null};
        String primaryCompareString = "aDifferentValue"; // A value that won't match "someValue"
        String secondaryCompareRegex = ".*"; // A regex that matches anything
        int desiredIndex = 0;

        // Act & Assert
        // This call is expected to throw a NullPointerException when it encounters
        // the null element at index 1 of arrayWithNull. The assertion is handled
        // by the 'expected' attribute of the @Test annotation.
        segmentConstantPool.matchSpecificPoolEntryIndex(
            arrayWithNull,
            arrayWithNull, // The secondary array's content is not relevant for this NPE test.
            primaryCompareString,
            secondaryCompareRegex,
            desiredIndex
        );
    }
}