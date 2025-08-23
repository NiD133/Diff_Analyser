package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CopticChronology} class.
 */
public class CopticChronologyTest {

    /**
     * Tests that calculateFirstDayOfYearMillis() correctly computes the start
     * of the maximum supported year. This is an important edge-case test.
     */
    @Test
    public void calculateFirstDayOfYearMillis_forMaximumYear_returnsCorrectValue() {
        // Arrange
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        int maxYear = copticChronology.getMaxYear(); // The maximum year is 292272708

        // This is the pre-calculated expected millisecond value for the start of the max year.
        long expectedMillis = 9223371994233600000L;

        // Act
        long actualMillis = copticChronology.calculateFirstDayOfYearMillis(maxYear);

        // Assert
        assertEquals("First day of the maximum year should be calculated correctly",
                     expectedMillis, actualMillis);
    }
}