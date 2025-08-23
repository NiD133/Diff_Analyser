package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the compareTo method of {@link UtcInstant}.
 */
public class UtcInstantCompareToTest {

    /**
     * Tests that compareTo() correctly identifies that an instant is after another
     * instant on the same day.
     */
    @Test
    public void compareTo_returnsPositive_whenComparingToEarlierInstantOnSameDay() {
        // Arrange: Create two instants on the same day, one at the start and one slightly after.
        long modifiedJulianDay = 0L;
        UtcInstant startOfDay = UtcInstant.ofModifiedJulianDay(modifiedJulianDay, 0L);
        UtcInstant laterOnSameDay = UtcInstant.ofModifiedJulianDay(modifiedJulianDay, 5000L);

        // Act: Compare the later instant to the earlier one.
        int comparisonResult = laterOnSameDay.compareTo(startOfDay);

        // Assert: The result must be positive, as the first instant is later in time.
        assertTrue(
            "Comparing a later instant to an earlier one should return a positive value.",
            comparisonResult > 0
        );
    }
}