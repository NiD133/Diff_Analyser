package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

import org.joda.time.chrono.IslamicChronology;

/**
 * Tests for the {@link IslamicChronology#getMinYear()} method.
 */
public class IslamicChronology_ESTestTest62 { // Note: Retaining original class name for context.

    /**
     * Verifies that the minimum year for the IslamicChronology is always 1,
     * as the calendar is not proleptic (does not support dates before year 1 AH).
     */
    @Test
    public void getMinYear_shouldAlwaysReturnOne() {
        // Arrange: The IslamicChronology is a non-proleptic calendar system.
        // Its first year is defined as 1 AH (Anno Hegirae).
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int expectedMinYear = 1;

        // Act: Get the minimum supported year from the chronology.
        int actualMinYear = islamicChronology.getMinYear();

        // Assert: The minimum year must be 1.
        assertEquals("The minimum year for the IslamicChronology should be 1.", expectedMinYear, actualMinYear);
    }
}