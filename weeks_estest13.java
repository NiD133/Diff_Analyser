package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Verifies that calling Weeks.weeksIn() with a null interval correctly returns zero weeks,
     * and that this value converts to zero days. This aligns with the method's Javadoc,
     * which states that a null interval should result in a zero-length period.
     */
    @Test
    public void toStandardDays_forWeeksCalculatedFromNullInterval_returnsZeroDays() {
        // Arrange: The Javadoc for weeksIn() specifies that a null interval should be treated as zero.
        ReadableInterval nullInterval = null;
        
        // Act: Calculate the number of weeks in the null interval and convert it to days.
        Weeks weeks = Weeks.weeksIn(nullInterval);
        Days resultingDays = weeks.toStandardDays();

        // Assert: The result should be zero days.
        assertEquals(Days.ZERO, resultingDays);
    }
}