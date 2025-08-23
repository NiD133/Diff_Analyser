package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This class contains unit tests for the {@link Seconds} class, focusing on the
 * {@link Seconds#toStandardDuration()} method.
 */
public class SecondsTest {

    /**
     * Tests that a typical Seconds value is correctly converted to a standard Duration.
     */
    @Test
    public void toStandardDuration_convertsRegularValue() {
        // Arrange
        final int secondsValue = 20;
        Seconds seconds = Seconds.seconds(secondsValue);
        Duration expectedDuration = new Duration((long) secondsValue * DateTimeConstants.MILLIS_PER_SECOND);

        // Act
        Duration actualDuration = seconds.toStandardDuration();

        // Assert
        assertEquals("20 seconds should convert to the equivalent duration in milliseconds",
                expectedDuration, actualDuration);
    }

    /**
     * Tests that the maximum Seconds value is correctly converted to a standard Duration,
     * verifying the handling of boundary values.
     */
    @Test
    public void toStandardDuration_convertsMaxValue() {
        // Arrange
        Seconds maxSeconds = Seconds.MAX_VALUE;
        Duration expectedDuration = new Duration((long) Integer.MAX_VALUE * DateTimeConstants.MILLIS_PER_SECOND);

        // Act
        Duration actualDuration = maxSeconds.toStandardDuration();

        // Assert
        assertEquals("Seconds.MAX_VALUE should convert correctly to a duration",
                expectedDuration, actualDuration);
    }
}