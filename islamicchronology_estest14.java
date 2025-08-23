package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    @Test
    public void isLeapYear_shouldReturnFalse_forFirstYearInDefaultChronology() {
        // The Islamic calendar year 1 is not a leap year in the default (16-based) pattern.
        // This test verifies the isLeapYear method for a known non-leap year.

        // Arrange
        IslamicChronology chronology = IslamicChronology.getInstance();
        int year = 1;

        // Act
        boolean isLeap = chronology.isLeapYear(year);

        // Assert
        assertFalse("Year 1 should not be a leap year in the default Islamic chronology", isLeap);
    }
}