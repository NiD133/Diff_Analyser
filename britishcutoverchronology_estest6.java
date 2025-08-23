package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    @Test
    public void isLeapYear_shouldReturnFalse_forNonLeapYearBeforeCutover() {
        // Arrange
        // The BritishCutoverChronology uses Julian calendar rules for years before the 1752 cutover.
        // In the Julian calendar, a year is a leap year if it is divisible by 4.
        // The year 371 is not divisible by 4, so it should not be a leap year.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        long year = 371;

        // Act
        boolean isLeap = chronology.isLeapYear(year);

        // Assert
        assertFalse("Year 371 should not be a leap year under Julian rules", isLeap);
    }
}