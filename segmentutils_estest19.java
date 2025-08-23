package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * This class contains tests for the {@link SegmentUtils} utility class.
 * The original test was auto-generated and has been refactored for clarity and maintainability.
 */
public class SegmentUtilsTest {

    /**
     * Verifies that {@link SegmentUtils#countMatches(long[], IMatcher)} throws a
     * {@link NullPointerException} when the provided matcher is null. This ensures
     * the method correctly handles invalid input.
     */
    @Test(expected = NullPointerException.class)
    public void countMatchesWithNullMatcherShouldThrowNullPointerException() {
        // The specific contents or size of the array do not matter for this test,
        // as the exception should be triggered by the null matcher.
        long[] flags = new long[4];

        // This call is expected to throw a NullPointerException.
        SegmentUtils.countMatches(flags, null);
    }
}