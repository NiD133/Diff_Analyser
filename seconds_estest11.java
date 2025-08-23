package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void toStandardDuration_withMinValue_convertsToCorrectDurationInMilliseconds() {
        // Arrange
        Seconds minSeconds = Seconds.MIN_VALUE;
        // A standard second is 1000 milliseconds. The expected duration is the
        // number of seconds (Integer.MIN_VALUE) multiplied by 1000.
        // We cast to long to prevent integer overflow during multiplication.
        long expectedMillis = (long) Integer.MIN_VALUE * 1000L;

        // Act
        Duration resultingDuration = minSeconds.toStandardDuration();

        // Assert
        assertEquals("The duration in milliseconds should be Integer.MIN_VALUE * 1000.",
                     expectedMillis, resultingDuration.getMillis());
    }
}