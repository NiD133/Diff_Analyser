package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CopticChronology} class.
 */
public class CopticChronologyTest {

    /**
     * Tests that getMaxYear() returns the documented maximum supported year.
     * The CopticChronology has a hardcoded maximum year to ensure calculations
     * do not overflow the 64-bit long used for storing milliseconds.
     */
    @Test
    public void getMaxYear_shouldReturnConstantMaximumValue() {
        // Arrange
        CopticChronology copticChronology = CopticChronology.getInstance();
        // This value is defined as a private constant MAX_YEAR in CopticChronology.
        final int EXPECTED_MAX_YEAR = 292272708;

        // Act
        int actualMaxYear = copticChronology.getMaxYear();

        // Assert
        assertEquals("The maximum year should match the constant defined in the class.",
                     EXPECTED_MAX_YEAR, actualMaxYear);
    }
}