package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void toStandardHours_whenConvertingZeroSeconds_shouldReturnZeroHours() {
        // Arrange
        Seconds zeroSeconds = Seconds.ZERO;
        int expectedHours = 0;

        // Act
        Hours actualHours = zeroSeconds.toStandardHours();

        // Assert
        assertEquals("Converting zero seconds to hours should result in zero hours.",
                     expectedHours, actualHours.getHours());
    }
}