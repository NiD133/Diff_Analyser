package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the leap year calculation in {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyLeapYearTest {

    /**
     * Tests that {@link Symmetry454Chronology#getLeapYearsBefore(long)} correctly calculates
     * the number of leap years for a large, arbitrary proleptic year.
     *
     * <p>The input and expected values are based on a known-good calculation from the
     * original, tool-generated test case.
     */
    @Test
    public void getLeapYearsBefore_forLargeYear_calculatesCorrectCount() {
        // Arrange: Define a large proleptic year and the expected number of leap years before it.
        long prolepticYear = 719162L;
        long expectedLeapYears = 127633L;

        // Act: Call the method under test.
        long actualLeapYears = Symmetry454Chronology.getLeapYearsBefore(prolepticYear);

        // Assert: Verify the actual result matches the expected one.
        assertEquals(expectedLeapYears, actualLeapYears);
    }
}