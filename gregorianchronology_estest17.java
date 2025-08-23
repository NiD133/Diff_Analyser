package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link GregorianChronology} class.
 */
public class GregorianChronologyTest {

    /**
     * Tests that calculateFirstDayOfYearMillis() returns the correct value for the
     * minimum supported year. This is a crucial boundary value test.
     */
    @Test
    public void calculateFirstDayOfYearMillis_forMinimumYear_returnsCorrectValue() {
        // Arrange
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        int minYear = chronology.getMinYear(); // The minimum year is -292275054

        // This is the pre-calculated expected millisecond value for the first day
        // of the minimum supported year (00:00:00.000 on 01-Jan--292275054).
        long expectedMillis = -9223372017043200000L;

        // Act
        long actualMillis = chronology.calculateFirstDayOfYearMillis(minYear);

        // Assert
        assertEquals(
            "The first day of the minimum year should have the correct millisecond value.",
            expectedMillis,
            actualMillis
        );
    }
}