package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static utility methods of {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests the calculation of the number of leap years before a given negative (BCE) proleptic year.
     *
     * <p>The Symmetry454 calendar has a specific leap year rule. This test verifies that the
     * implementation correctly calculates the cumulative count of leap years for years
     * before the common era (represented by negative proleptic years).
     */
    @Test
    public void getLeapYearsBefore_withNegativeYear_returnsCorrectCount() {
        // Arrange: Define a proleptic year in the BCE era and the expected leap year count.
        long prolepticYearBCE = -60L;
        long expectedLeapYears = -11L;

        // Act: Calculate the actual number of leap years before the given year.
        long actualLeapYears = Symmetry454Chronology.getLeapYearsBefore(prolepticYearBCE);

        // Assert: Verify that the calculated count matches the expected value.
        assertEquals(expectedLeapYears, actualLeapYears);
    }
}