package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CopticChronology}.
 */
public class CopticChronologyTest {

    /**
     * Tests that getMinYear() returns the correct, documented minimum supported year.
     */
    @Test
    public void getMinYear_shouldReturnCorrectMinimumSupportedYear() {
        // Arrange
        // The expected minimum year is defined as a private constant in CopticChronology.
        final int expectedMinYear = -292269337;
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();

        // Act
        int actualMinYear = copticChronology.getMinYear();

        // Assert
        assertEquals("The minimum year should match the defined constant.",
                expectedMinYear, actualMinYear);
    }
}