package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Symmetry454Chronology} class, focusing on leap year calculations.
 */
public class Symmetry454ChronologyTest {

    @Test
    public void isLeapYear_shouldReturnTrue_forKnownLeapYear() {
        // The Symmetry454 leap year formula is defined as: (52 > ((52 * year + 146) % 293)).
        // For the year 3, the calculation is:
        // (52 * 3 + 146) % 293 = (156 + 146) % 293 = 302 % 293 = 9.
        // Since 52 > 9, the year 3 is a leap year.

        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        long leapYear = 3L;

        // Act
        boolean isLeap = chronology.isLeapYear(leapYear);

        // Assert
        assertTrue("Year 3 should be correctly identified as a leap year.", isLeap);
    }
}