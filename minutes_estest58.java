package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that the factory method `minutes(int)` correctly creates an instance
     * when provided with the maximum possible integer value.
     */
    @Test
    public void shouldCreateMinutesWithMaxValue() {
        // Arrange
        final int maxIntValue = Integer.MAX_VALUE;

        // Act
        Minutes maxMinutes = Minutes.minutes(maxIntValue);

        // Assert
        assertEquals("The value retrieved from getMinutes() should match the value used for creation.",
                maxIntValue, maxMinutes.getMinutes());
    }
}