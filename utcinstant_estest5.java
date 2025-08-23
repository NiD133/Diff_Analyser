package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link UtcInstant}.
 * This class focuses on the comparison logic of UtcInstant.
 */
public class UtcInstant_ESTestTest5 extends UtcInstant_ESTest_scaffolding {

    /**
     * Tests that isBefore() correctly returns false when comparing a later instant
     * to an earlier one. The comparison is primarily based on the Modified Julian Day.
     */
    @Test
    public void isBefore_shouldReturnFalse_whenComparingLaterInstantToEarlierOne() {
        // Arrange: Create two instants where one is clearly after the other.
        // The nano-of-day is kept constant to isolate the comparison to the day.
        long nanoOfDay = 123_456_789L;
        UtcInstant earlierInstant = UtcInstant.ofModifiedJulianDay(50000L, nanoOfDay);
        UtcInstant laterInstant = UtcInstant.ofModifiedJulianDay(50001L, nanoOfDay);

        // Act: Check if the later instant is before the earlier one.
        boolean isBefore = laterInstant.isBefore(earlierInstant);

        // Assert: The result should be false, as a later time is not "before" an earlier time.
        assertFalse("A later instant should not be before an earlier one.", isBefore);
    }
}