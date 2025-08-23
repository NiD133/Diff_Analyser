package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A more focused test class for UtcInstant, demonstrating the improved test case.
 */
public class UtcInstantTest {

    /**
     * Tests that withNanoOfDay() creates a new instance with the updated nano-of-day
     * while preserving the original Modified Julian Day.
     */
    @Test
    public void withNanoOfDay_shouldUpdateNanoOfDayWhilePreservingMjd() {
        // Arrange: Create an initial UtcInstant at the start of a day.
        long initialMjd = 40587L; // Corresponds to 1970-01-01
        long initialNanoOfDay = 1_000_000_000L; // 1 second into the day
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanoOfDay);

        long newNanoOfDay = 123_456_789_012L; // A distinct, new nano-of-day value.

        // Act: Call the method under test to get a new instant.
        UtcInstant updatedInstant = initialInstant.withNanoOfDay(newNanoOfDay);

        // Assert: Verify the new instant has the correct state.
        assertEquals(
            "The nano-of-day should be updated to the new value.",
            newNanoOfDay,
            updatedInstant.getNanoOfDay()
        );
        assertEquals(
            "The Modified Julian Day should remain unchanged.",
            initialMjd,
            updatedInstant.getModifiedJulianDay()
        );
    }
}