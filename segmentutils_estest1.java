package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Unit tests for the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Tests that countMatches throws a NullPointerException when called with a two-dimensional
     * array and a null IMatcher. The method is expected to perform a null-check on its
     * arguments before processing them.
     */
    @Test(expected = NullPointerException.class)
    public void countMatchesFor2DArrayShouldThrowNPEForNullMatcher() {
        // Arrange: A non-null array of flags. The actual content is irrelevant for this test,
        // as the exception should be thrown before the array is accessed.
        final long[][] flags = new long[1][1];
        final IMatcher matcher = null;

        // Act: Call the method under test with the null matcher.
        SegmentUtils.countMatches(flags, matcher);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected) annotation.
    }
}