package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class, focusing on the toStandardDuration method.
 */
public class SecondsTest {

    @Test
    public void toStandardDuration_shouldReturnZeroDuration_whenSecondsIsZero() {
        // Arrange
        Seconds zeroSeconds = Seconds.ZERO;
        Duration expectedDuration = Duration.ZERO;

        // Act
        Duration actualDuration = zeroSeconds.toStandardDuration();

        // Assert
        assertEquals(expectedDuration, actualDuration);
    }
}