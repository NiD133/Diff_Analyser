package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that the plus(int) method correctly adds a given number of weeks
     * to an existing Weeks object.
     */
    @Test
    public void plus_whenAddingWeeks_shouldReturnNewInstanceWithCorrectSum() {
        // Arrange: Set up the initial state and expectations.
        final Weeks initialWeeks = Weeks.weeks(2);
        final int weeksToAdd = 2;
        final int expectedTotalWeeks = 4;

        // Act: Call the method under test.
        final Weeks result = initialWeeks.plus(weeksToAdd);

        // Assert: Verify the outcome.
        assertEquals(expectedTotalWeeks, result.getWeeks());
    }
}