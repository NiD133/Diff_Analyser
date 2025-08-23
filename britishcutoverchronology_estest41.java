package org.threeten.extra.chrono;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that the cutover year, 1752, is correctly identified as a leap year.
     * <p>
     * The {@link BritishCutoverChronology} uses Julian calendar rules for years up to and including 1752.
     * In the Julian calendar, a year is a leap year if it is divisible by 4.
     * Since 1752 is divisible by 4, it must be treated as a leap year.
     */
    @Test
    public void isLeapYear_forCutoverYear1752_shouldBeLeap() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        long prolepticYear = 1752L;

        // Act
        boolean isLeap = chronology.isLeapYear(prolepticYear);

        // Assert
        assertTrue("The cutover year 1752 should be a leap year under Julian rules.", isLeap);
    }
}