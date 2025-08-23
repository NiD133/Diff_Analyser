package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class, focusing on the toStandardDuration() method.
 */
public class MinutesTest {

    private static final long MILLIS_PER_MINUTE = DateTimeConstants.MILLIS_PER_MINUTE;

    @Test
    public void toStandardDuration_withTypicalMinutes_returnsCorrectDuration() {
        // Arrange
        final int minutesValue = 20;
        final Minutes minutes = Minutes.minutes(minutesValue);
        final Duration expectedDuration = new Duration(minutesValue * MILLIS_PER_MINUTE);

        // Act
        final Duration actualDuration = minutes.toStandardDuration();

        // Assert
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void toStandardDuration_withMaxValue_returnsCorrectDuration() {
        // Arrange
        final Minutes maxMinutes = Minutes.MAX_VALUE;
        final Duration expectedDuration = new Duration((long) Integer.MAX_VALUE * MILLIS_PER_MINUTE);

        // Act
        final Duration actualDuration = maxMinutes.toStandardDuration();

        // Assert
        assertEquals(expectedDuration, actualDuration);
    }
}