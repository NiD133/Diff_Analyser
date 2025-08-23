package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#isUnixTime(long)} returns true for the minimum value
     * of a 32-bit signed integer. This value represents the lower bound of the traditional
     * Unix time range, which is significant due to the "Year 2038 problem" context.
     */
    @Test
    public void isUnixTimeShouldReturnTrueForMinimum32BitIntegerValue() {
        // The value Integer.MIN_VALUE (-2^31) corresponds to 1901-12-13.
        // It is the earliest time representable by a 32-bit signed Unix timestamp.
        final long minUnixTimeSeconds = Integer.MIN_VALUE;

        assertTrue("The minimum 32-bit integer value should be considered a valid Unix time",
            FileTimes.isUnixTime(minUnixTimeSeconds));
    }
}