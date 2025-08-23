package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test cases for the {@link Weeks} class, focusing on conversion methods.
 */
public class WeeksTest {

    private static final int WEEKS_IN_A_DAY = 7;
    private static final int HOURS_IN_A_DAY = 24;
    private static final int MINUTES_IN_AN_HOUR = 60;

    @Test
    public void toStandardMinutes_shouldConvertWeeksToMinutes() {
        // Arrange
        Weeks twoWeeks = Weeks.weeks(2);
        int expectedMinutesValue = 2 * WEEKS_IN_A_DAY * HOURS_IN_A_DAY * MINUTES_IN_AN_HOUR;
        Minutes expectedMinutes = Minutes.minutes(expectedMinutesValue);

        // Act
        Minutes actualMinutes = twoWeeks.toStandardMinutes();

        // Assert
        assertEquals(expectedMinutes, actualMinutes);
    }

    @Test(expected = ArithmeticException.class)
    public void toStandardMinutes_shouldThrowExceptionWhenResultOverflows() {
        // Act: This should throw an ArithmeticException due to integer overflow.
        Weeks.MAX_VALUE.toStandardMinutes();

        // Assert: The 'expected' parameter of the @Test annotation handles the assertion.
    }
}