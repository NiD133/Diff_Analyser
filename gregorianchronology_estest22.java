package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link GregorianChronology}.
 */
public class GregorianChronologyTest {

    /**
     * Tests that getMinYear() returns the correct, hardcoded minimum year value.
     */
    @Test
    public void getMinYear_shouldReturnConstantMinimumYearValue() {
        // Arrange
        // The getMinYear() method returns a constant value, so the time zone
        // of the chronology instance does not affect the result.
        // We use the UTC instance for simplicity.
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        
        // This is the value of the private static final constant MIN_YEAR in GregorianChronology.
        final int expectedMinYear = -292275054;

        // Act
        int actualMinYear = chronology.getMinYear();

        // Assert
        assertEquals(expectedMinYear, actualMinYear);
    }
}