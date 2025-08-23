package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that subtracting a positive number of weeks from a Weeks object
     * results in a new Weeks object with the correctly calculated value.
     */
    @Test
    public void minus_shouldReturnCorrectWeeks_whenSubtractingAnInteger() {
        // Arrange
        final int initialWeekCount = 317351877;
        final int weeksToSubtract = 2285;
        final Weeks initialWeeks = Weeks.weeks(initialWeekCount);

        final int expectedWeekCount = initialWeekCount - weeksToSubtract;

        // Act
        Weeks result = initialWeeks.minus(weeksToSubtract);

        // Assert
        assertEquals(expectedWeekCount, result.getWeeks());
    }
}