package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    /**
     * Tests that {@code matchSpecificPoolEntryIndex} returns -1 when a negative desired index is provided.
     * A negative index is an invalid search parameter, so no match should be found.
     */
    @Test
    public void matchSpecificPoolEntryIndexShouldReturnNegativeOneForNegativeDesiredIndex() {
        // Arrange
        // The method under test does not rely on the state of the SegmentConstantPool instance,
        // so we can pass null to its constructor.
        SegmentConstantPool segmentConstantPool = new SegmentConstantPool(null);

        // Set up arrays that would yield a match if the desired index were valid (e.g., 0).
        // This isolates the negative index as the reason for the test's outcome.
        String[] classNames = {"java/lang/String", "java/lang/Object"};
        String[] methodNames = {"toString", "<init>"};

        String classToMatch = "java/lang/Object";
        // This regex matches constructor methods like "<init>".
        String methodRegexToMatch = "^<init>.*";
        int invalidDesiredIndex = -1;

        // Act
        // Attempt to find the -1th match, which is logically impossible.
        int foundIndex = segmentConstantPool.matchSpecificPoolEntryIndex(
                classNames,
                methodNames,
                classToMatch,
                methodRegexToMatch,
                invalidDesiredIndex
        );

        // Assert
        // The method should return -1 to indicate that no match was found.
        assertEquals(-1, foundIndex);
    }
}