package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that calling {@code withModifiedJulianDay} with the same day value
     * results in an equal UtcInstant.
     */
    @Test
    public void withModifiedJulianDay_whenDayIsUnchanged_returnsEqualInstant() {
        // Arrange: Create an initial UtcInstant
        long initialMjd = 40587L; // Corresponds to 1970-01-01
        long nanoOfDay = 12345L;
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, nanoOfDay);

        // Act: Call the method with the same Modified Julian Day
        UtcInstant resultantInstant = initialInstant.withModifiedJulianDay(initialMjd);

        // Assert: The new instant should be equal to the original,
        // as no change was made.
        assertEquals(initialInstant, resultantInstant);
    }
}