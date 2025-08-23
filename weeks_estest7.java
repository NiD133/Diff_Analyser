package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that converting one week to standard minutes yields the correct result.
     * A standard week is defined as 7 days, a standard day as 24 hours,
     * and a standard hour as 60 minutes.
     */
    @Test
    public void toStandardMinutes_forOneWeek_returnsCorrectNumberOfMinutes() {
        // Arrange
        Weeks oneWeek = Weeks.ONE;
        
        // The expected number of minutes in one standard week (7 days * 24 hours * 60 minutes)
        final int expectedMinutes = 7 * 24 * 60;

        // Act
        Minutes actualMinutes = oneWeek.toStandardMinutes();

        // Assert
        assertEquals(expectedMinutes, actualMinutes.getMinutes());
    }
}