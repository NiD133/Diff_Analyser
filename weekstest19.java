package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void toStandardDuration_convertsTypicalValueToCorrectDuration() {
        // Arrange
        Weeks twentyWeeks = Weeks.weeks(20);
        Duration expectedDuration = new Duration(20L * DateTimeConstants.MILLIS_PER_WEEK);

        // Act
        Duration actualDuration = twentyWeeks.toStandardDuration();

        // Assert
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void toStandardDuration_convertsMaxValueToCorrectDuration() {
        // Arrange
        Weeks maxWeeks = Weeks.MAX_VALUE;
        long expectedMillis = (long) Integer.MAX_VALUE * DateTimeConstants.MILLIS_PER_WEEK;
        Duration expectedDuration = new Duration(expectedMillis);

        // Act
        Duration actualDuration = maxWeeks.toStandardDuration();

        // Assert
        assertEquals(expectedDuration, actualDuration);
    }
}