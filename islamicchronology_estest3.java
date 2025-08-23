package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link IslamicChronology}.
 */
public class IslamicChronologyTest {

    /**
     * Verifies that calculateFirstDayOfYearMillis computes the correct millisecond value
     * for the minimum supported year in the Islamic calendar.
     */
    @Test
    public void calculateFirstDayOfYearMillis_forMinimumSupportedYear_returnsCorrectValue() {
        // Arrange
        // The input year is the minimum supported year, as defined by the private constant
        // IslamicChronology.MIN_YEAR.
        final int minSupportedYear = -292269337;
        final long expectedMillis = -8948534433609600000L;
        
        IslamicChronology islamicChronology = IslamicChronology.getInstance();

        // Act
        long actualMillis = islamicChronology.calculateFirstDayOfYearMillis(minSupportedYear);

        // Assert
        assertEquals("Milliseconds for the first day of the minimum year should be correct",
                expectedMillis, actualMillis);
    }
}