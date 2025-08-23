package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    // The expected maximum year value is a hard-coded constant in IslamicChronology
    // to prevent calculation overflows.
    private static final int EXPECTED_MAX_YEAR = 292271022;

    /**
     * Verifies that getMaxYear() returns the correct, predefined maximum year value.
     */
    @Test
    public void getMaxYear_shouldReturnConstantMaxValue() {
        // Arrange: Get a standard instance of the IslamicChronology.
        // The time zone does not affect the result of getMaxYear().
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();

        // Act: Call the method under test.
        int actualMaxYear = chronology.getMaxYear();

        // Assert: Check if the returned value matches the expected constant.
        assertEquals("The maximum year should be the predefined constant value.",
                EXPECTED_MAX_YEAR, actualMaxYear);
    }
}