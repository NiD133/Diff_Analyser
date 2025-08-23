package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests the calculation of leap years for a proleptic year in the BCE era.
     * <p>
     * The getLeapYearsBefore method calculates the number of leap years that occurred
     * before the given proleptic year. For a negative year (BCE), the expected result
     * is a negative count. This test verifies a known, pre-calculated value.
     */
    @Test
    public void getLeapYearsBefore_withNegativeYear_returnsCorrectNegativeCount() {
        // The Symmetry010 calendar has a complex leap year rule.
        // This test verifies a known, pre-calculated result for a year before the common era (BCE).
        int prolepticYear = -2552;
        long expectedLeapYearCount = -453L;

        // Act
        long actualLeapYearCount = Symmetry010Chronology.getLeapYearsBefore(prolepticYear);

        // Assert
        assertEquals(
            "The number of leap years before year " + prolepticYear + " should be correctly calculated.",
            expectedLeapYearCount,
            actualLeapYearCount
        );
    }
}