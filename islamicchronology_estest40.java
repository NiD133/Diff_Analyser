package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IslamicChronology} class, focusing on month properties.
 */
// Note: The original class name and scaffolding are retained from the source file.
// In a real-world scenario, this would likely be part of a single 'IslamicChronologyTest' class.
public class IslamicChronology_ESTestTest40 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Tests that the maximum number of days for the first month (Muharram) is always 30.
     */
    @Test
    public void getDaysInMonthMax_forFirstMonth_shouldReturn30() {
        // Arrange: The Islamic calendar has months that alternate between 30 and 29 days.
        // Odd-numbered months, like the first month, consistently have 30 days.
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        final int firstMonth = 1; // Represents Muharram
        final int expectedMaxDays = 30;

        // Act: Get the maximum number of days for the first month.
        int actualMaxDays = islamicChronology.getDaysInMonthMax(firstMonth);

        // Assert: Verify that the result is 30.
        assertEquals(expectedMaxDays, actualMaxDays);
    }
}