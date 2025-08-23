package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    private static final int DAYS_PER_WEEK = 7;
    private static final int HOURS_PER_DAY = 24;

    /**
     * Tests that a standard conversion from a number of weeks to hours is calculated correctly.
     */
    @Test
    public void testToStandardHours_convertsCorrectly() {
        // Arrange
        final Weeks twoWeeks = Weeks.weeks(2);
        final Hours expectedHours = Hours.hours(2 * DAYS_PER_WEEK * HOURS_PER_DAY);

        // Act
        final Hours actualHours = twoWeeks.toStandardHours();

        // Assert
        assertEquals(expectedHours, actualHours);
    }

    /**
     * Tests that attempting to convert the maximum number of weeks to hours
     * throws an ArithmeticException, as the result would overflow an integer.
     */
    @Test(expected = ArithmeticException.class)
    public void testToStandardHours_whenResultOverflows_throwsArithmeticException() {
        // Act
        // This operation should fail with an overflow.
        Weeks.MAX_VALUE.toStandardHours();
        
        // Assert is handled by the 'expected' parameter of the @Test annotation.
    }
}