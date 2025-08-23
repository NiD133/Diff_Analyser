package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class, focusing on period conversions.
 */
public class MinutesTest {

    private static final int MINUTES_PER_HOUR = 60;
    private static final int HOURS_PER_DAY = 24;
    private static final int DAYS_PER_WEEK = 7;
    private static final int MINUTES_PER_WEEK = MINUTES_PER_HOUR * HOURS_PER_DAY * DAYS_PER_WEEK;

    @Test
    public void toStandardWeeks_forMinutesEquivalentToTwoWeeks_returnsTwoWeeks() {
        // Arrange
        final int numberOfWeeks = 2;
        Minutes minutesToConvert = Minutes.minutes(numberOfWeeks * MINUTES_PER_WEEK);
        Weeks expectedWeeks = Weeks.weeks(numberOfWeeks);

        // Act
        Weeks actualWeeks = minutesToConvert.toStandardWeeks();

        // Assert
        assertEquals("Conversion from minutes to weeks should be correct", expectedWeeks, actualWeeks);
    }
}