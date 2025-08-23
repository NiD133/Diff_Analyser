package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains an improved, more understandable version of an
 * auto-generated test for the {@link UtcInstant} class.
 */
public class UtcInstantTest {

    /**
     * Tests that {@link UtcInstant#isAfter(UtcInstant)} correctly returns true
     * when comparing an instant to one that is chronologically earlier.
     * The comparison is primarily based on the Modified Julian Day (MJD).
     */
    @Test
    public void isAfter_shouldReturnTrue_whenComparingToAnEarlierInstant() {
        // Arrange
        // Create a "later" instant with a positive Modified Julian Day (MJD).
        long nanoOfDay = 123_456_789L;
        UtcInstant laterInstant = UtcInstant.ofModifiedJulianDay(100L, nanoOfDay);

        // Create an "earlier" instant by taking the later one and setting
        // a smaller (in this case, negative) MJD. The nano-of-day remains the same.
        UtcInstant earlierInstant = laterInstant.withModifiedJulianDay(-100L);

        // Sanity check: Verify the state of the earlier instant after its creation.
        assertEquals(-100L, earlierInstant.getModifiedJulianDay());
        assertEquals(nanoOfDay, earlierInstant.getNanoOfDay());

        // Act
        // Perform the comparison.
        boolean isAfter = laterInstant.isAfter(earlierInstant);

        // Assert
        // The later instant should be after the earlier one.
        assertTrue(
            "An instant with a greater MJD should be considered 'after' an instant with a smaller MJD.",
            isAfter
        );
    }
}