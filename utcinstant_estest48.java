package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Test suite for the equals() and hashCode() contract of {@link UtcInstant}.
 */
public class UtcInstantEqualsTest {

    /**
     * Tests that two UtcInstant objects are not equal if they share the same
     * Modified Julian Day but have different nano-of-day values.
     * This verifies that the nanoOfDay field is correctly used in the equality check.
     */
    @Test
    public void equals_returnsFalse_whenNanoOfDayDiffers() {
        // Arrange: Create two instants on the same day but at different nanoseconds.
        UtcInstant instantAtStartOfDay = UtcInstant.ofModifiedJulianDay(0L, 0L);
        UtcInstant instantLaterInDay = UtcInstant.ofModifiedJulianDay(0L, 1891L);

        // Act & Assert: The two instants should not be considered equal because their
        // nano-of-day components are different.
        assertNotEquals(instantAtStartOfDay, instantLaterInDay);
    }
}