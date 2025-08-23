package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the Seconds class.
 */
public class SecondsTest {

    /**
     * This test verifies the behavior of converting Seconds.MIN_VALUE to Weeks and back.
     * The conversion to Weeks involves integer division, which truncates the value (loses precision).
     * As a result, the value converted back to Seconds is a larger (less negative) number
     * than the original MIN_VALUE.
     */
    @Test
    public void isLessThan_returnsFalse_whenComparingMinValueToItselfAfterWeekConversionRoundtrip() {
        // Arrange
        final Seconds minSeconds = Seconds.MIN_VALUE;
        final int SECONDS_PER_WEEK = 604800; // 7 days * 24 hours * 60 minutes * 60 seconds

        // Act
        // 1. Convert MIN_VALUE seconds to weeks, which truncates the result.
        Weeks weeks = minSeconds.toStandardWeeks();
        
        // 2. Convert the truncated weeks back to seconds.
        Seconds roundTripSeconds = weeks.toStandardSeconds();

        // 3. Perform the comparison.
        boolean isLessThan = roundTripSeconds.isLessThan(minSeconds);

        // Assert
        // The main assertion: the truncated round-trip value is not less than the original.
        assertFalse("Round-trip seconds should not be less than the original MIN_VALUE", isLessThan);

        // Verify the intermediate and final values to confirm the truncation logic.
        int expectedWeeks = Integer.MIN_VALUE / SECONDS_PER_WEEK; // -3550
        assertEquals("The number of weeks should be the result of integer division",
                expectedWeeks, weeks.getWeeks());

        int expectedRoundTripSeconds = expectedWeeks * SECONDS_PER_WEEK; // -2147040000
        assertEquals("The round-trip seconds value should reflect the truncation",
                expectedRoundTripSeconds, roundTripSeconds.getSeconds());
    }
}