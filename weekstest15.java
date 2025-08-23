package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class, focusing on the toStandardDays() method.
 */
public class WeeksTest {

    // A standard week has 7 days.
    private static final int DAYS_PER_WEEK = 7;

    @Test
    public void toStandardDays_shouldConvertWeeksToDaysForStandardCase() {
        // Arrange
        final int numberOfWeeks = 2;
        final Weeks twoWeeks = Weeks.weeks(numberOfWeeks);
        final Days expectedDays = Days.days(numberOfWeeks * DAYS_PER_WEEK);

        // Act
        final Days actualDays = twoWeeks.toStandardDays();

        // Assert
        assertEquals(expectedDays, actualDays);
    }

    @Test(expected = ArithmeticException.class)
    public void toStandardDays_shouldThrowExceptionWhenDayCalculationOverflows() {
        // Act
        // The following call is expected to throw an ArithmeticException because
        // multiplying Weeks.MAX_VALUE (which is Integer.MAX_VALUE) by 7
        // will cause an integer overflow.
        Weeks.MAX_VALUE.toStandardDays();
    }
}