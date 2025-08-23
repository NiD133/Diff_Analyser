package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test class for the {@link Symmetry454Chronology}.
 *
 * Note: This class is an improved version of an auto-generated test case.
 * The original class name "Symmetry454Chronology_ESTestTest31" was replaced
 * with a more conventional name.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that {@link Symmetry454Chronology#isLeapYear(long)} correctly identifies a non-leap year.
     */
    @Test
    public void isLeapYear_returnsFalse_forNonLeapYear() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // The formula for a leap year is `(52 > ((52 * year + 146) % 293))`.
        // For year 32, the expression `((52 * 32 + 146) % 293)` evaluates to exactly 52.
        // Since `52 > 52` is false, year 32 is not a leap year. This makes it a good
        // boundary case to test.
        long year = 32L;

        // Act
        boolean isLeap = chronology.isLeapYear(year);

        // Assert
        assertFalse("Year 32 should not be a leap year.", isLeap);
    }
}