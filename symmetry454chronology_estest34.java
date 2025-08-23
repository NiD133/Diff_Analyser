package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests the calculation of leap years for a proleptic year before the Common Era begins.
     * The method should return 0 for year 0, as the count of leap years starts from year 1 CE.
     */
    @Test
    public void getLeapYearsBefore_withYearZero_returnsZero() {
        // Arrange: Define the input and expected outcome for this boundary case.
        // Proleptic year 0 is the year immediately preceding year 1 CE.
        long prolepticYear = 0L;
        long expectedLeapYears = 0L;

        // Act: Call the method under test.
        long actualLeapYears = Symmetry454Chronology.getLeapYearsBefore(prolepticYear);

        // Assert: Verify that the actual result matches the expected result.
        assertEquals("The number of leap years before year 1 CE should be 0.", expectedLeapYears, actualLeapYears);
    }
}