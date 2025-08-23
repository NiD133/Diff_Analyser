package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void toStandardSeconds_whenConvertingOneMinute_shouldReturnSixtySeconds() {
        // Arrange
        final Minutes oneMinute = Minutes.ONE;
        final int expectedSeconds = 60;

        // Act
        final Seconds actualSeconds = oneMinute.toStandardSeconds();

        // Assert
        assertEquals(expectedSeconds, actualSeconds.getSeconds());
    }
}