package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#isUnixTime(long)} returns false for the minimum possible
     * long value, as it falls far outside the representable range of a standard Unix timestamp.
     */
    @Test
    public void isUnixTimeShouldReturnFalseForLongMinValue() {
        // The value Long.MIN_VALUE is an extreme boundary case that is not a valid
        // number of seconds since the epoch. The valid range is constrained by
        // the limits of java.time.Instant, which FileTime is based on.
        final boolean isWithinRange = FileTimes.isUnixTime(Long.MIN_VALUE);

        // Assert that the method correctly identifies this value as out of range.
        assertFalse("Long.MIN_VALUE should be considered outside the valid Unix time range.", isWithinRange);
    }
}