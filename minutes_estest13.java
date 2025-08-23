package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that converting the maximum possible number of minutes (Integer.MAX_VALUE)
     * to standard days produces the correct number of whole days.
     */
    @Test
    public void toStandardDays_fromMaxValue_returnsCorrectNumberOfDays() {
        // Arrange
        // A standard day is defined as 24 hours * 60 minutes/hour = 1440 minutes.
        // The expected result is the integer division of the maximum minutes value
        // by the number of minutes in a day.
        final int minutesInStandardDay = 24 * 60;
        final int expectedDays = Integer.MAX_VALUE / minutesInStandardDay; // 2,147,483,647 / 1440 = 1,491,308

        // Act
        // Convert the maximum Minutes value to Days.
        Days actualDays = Minutes.MAX_VALUE.toStandardDays();

        // Assert
        assertEquals("The number of days should be Integer.MAX_VALUE / (24 * 60)",
                     expectedDays, actualDays.getDays());
    }
}