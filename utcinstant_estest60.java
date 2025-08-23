package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link UtcInstant} class.
 * Note: The original class name was preserved, but a more descriptive name
 * like {@code UtcInstantTest} would be preferable.
 */
public class UtcInstant_ESTestTest60 extends UtcInstant_ESTest_scaffolding {

    /**
     * Tests that {@code withModifiedJulianDay()} correctly updates the Modified Julian Day
     * while preserving the nano-of-day component of the instant.
     */
    @Test
    public void withModifiedJulianDay_shouldUpdateDayAndPreserveNanos() {
        // Arrange: Create an initial UtcInstant at the start of a day.
        final long initialNanoOfDay = 0L;
        UtcInstant baseInstant = UtcInstant.ofModifiedJulianDay(0L, initialNanoOfDay);
        long newModifiedJulianDay = -2547L;

        // Act: Create a new instant by changing the Modified Julian Day.
        UtcInstant updatedInstant = baseInstant.withModifiedJulianDay(newModifiedJulianDay);

        // Assert: Verify the new instant has the updated day and the original nano-of-day.
        assertEquals(
                "The Modified Julian Day should be updated to the new value.",
                newModifiedJulianDay,
                updatedInstant.getModifiedJulianDay());
        assertEquals(
                "The nano-of-day should be preserved from the original instant.",
                initialNanoOfDay,
                updatedInstant.getNanoOfDay());
    }
}