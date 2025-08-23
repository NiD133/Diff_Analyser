package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CopticChronology} class, focusing on the
 * calculation of the first day of the year.
 */
public class CopticChronology_ESTestTest7 {

    /**
     * Tests that calculateFirstDayOfYearMillis() returns the correct millisecond
     * value for a given year. The expected value is derived by creating a
     * DateTime object for the first day of the Coptic year and retrieving its
     * millisecond value, making the test more self-documenting than using a
     * magic number.
     */
    @Test
    public void testCalculateFirstDayOfYearMillisForPositiveYear() {
        // Arrange
        final CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        final int year = 3571;

        // The first day of the Coptic year is the 1st of the month of Tout.
        // We create a DateTime for this specific date to get the expected millis,
        // avoiding the use of an opaque long literal.
        final DateTime firstDayOfYear = new DateTime(year, 1, 1, 0, 0, copticChronology);
        final long expectedFirstDayMillis = firstDayOfYear.getMillis();

        // Act
        final long actualFirstDayMillis = copticChronology.calculateFirstDayOfYearMillis(year);

        // Assert
        assertEquals("The calculated first day of year in milliseconds should match the expected value.",
                     expectedFirstDayMillis, actualFirstDayMillis);
    }
}