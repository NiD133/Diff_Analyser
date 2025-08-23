package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link GregorianChronology}.
 */
public class GregorianChronologyTest {

    /**
     * Verifies that getMaxYear() returns the documented constant maximum year value.
     * The GregorianChronology is designed to support a specific range of years,
     * and this test ensures the upper bound is correctly reported.
     */
    @Test
    public void testGetMaxYearReturnsCorrectValue() {
        // Arrange
        // This is the documented maximum year for the GregorianChronology.
        final int EXPECTED_MAX_YEAR = 292278993;
        GregorianChronology chronology = GregorianChronology.getInstance();

        // Act
        int actualMaxYear = chronology.getMaxYear();

        // Assert
        assertEquals("The maximum year should match the constant value.",
                     EXPECTED_MAX_YEAR, actualMaxYear);
    }
}