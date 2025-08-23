package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that the number of leap years before year 0 is correctly calculated as zero.
     * <p>
     * The {@code getLeapYearsBefore} method counts leap years since CE 1. Therefore, for any
     * proleptic year before 1 (such as 0), the expected result is 0.
     */
    @Test
    public void getLeapYearsBefore_shouldReturnZero_forYearZero() {
        // Arrange
        long prolepticYear = 0L;
        long expectedLeapYearCount = 0L;

        // Act
        long actualLeapYearCount = Symmetry010Chronology.getLeapYearsBefore(prolepticYear);

        // Assert
        assertEquals("There should be no leap years before year 1.",
                expectedLeapYearCount, actualLeapYearCount);
    }
}