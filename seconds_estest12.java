package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Seconds class.
 */
public class SecondsTest {

    /**
     * Tests the round-trip conversion from Days to Seconds and back to Days.
     * This verifies that `toStandardDays()` correctly reverses the `toStandardSeconds()` conversion.
     */
    @Test
    public void toStandardDays_shouldRevertConversionFromDays() {
        // Arrange
        Days originalDays = Days.FIVE;
        int expectedSeconds = 5 * 24 * 60 * 60; // 432,000 seconds in 5 days

        // Act
        Seconds seconds = originalDays.toStandardSeconds();
        Days resultDays = seconds.toStandardDays();

        // Assert
        assertEquals("The number of seconds should match the standard conversion from 5 days",
                expectedSeconds, seconds.getSeconds());
        assertEquals("Converting back to days should yield the original value",
                originalDays, resultDays);
    }
}