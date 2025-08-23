package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for arithmetic operations in the {@link Seconds} class.
 */
public class SecondsArithmeticTest {

    /**
     * Tests that adding a large negative number of seconds to an initial value,
     * and then converting the result to standard days, produces the correct outcome.
     * This verifies the chain of operations: plus() -> toStandardDays().
     */
    @Test
    public void testPlusWithLargeNegativeValueThenConvertToDays() {
        // Arrange
        // Start with the number of seconds in two standard weeks.
        Seconds twoWeeksInSeconds = Seconds.standardSecondsIn(Weeks.TWO);
        // Sanity check the initial value: 2 weeks * 7 days/week * 24 hours/day * 3600 sec/hour = 1,209,600
        assertEquals(1_209_600, twoWeeksInSeconds.getSeconds());

        // Define a large negative value to add.
        int secondsToAdd = -2147138048;

        // Calculate the expected results to avoid magic numbers in assertions.
        int expectedSecondsAfterAddition = 1_209_600 + secondsToAdd; // -2145928448
        final int SECONDS_PER_STANDARD_DAY = 86400; // 24 * 60 * 60
        int expectedDaysAfterConversion = expectedSecondsAfterAddition / SECONDS_PER_STANDARD_DAY; // -24837

        // Act
        Seconds resultAfterAddition = twoWeeksInSeconds.plus(secondsToAdd);
        Days finalResultInDays = resultAfterAddition.toStandardDays();

        // Assert
        assertEquals("The result of adding the negative value should be correct",
                expectedSecondsAfterAddition, resultAfterAddition.getSeconds());
        assertEquals("The final conversion to days should be correct",
                expectedDaysAfterConversion, finalResultInDays.getDays());
    }
}