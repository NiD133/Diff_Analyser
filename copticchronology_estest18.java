package org.joda.time.chrono;

import static org.junit.Assert.assertFalse;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Unit tests for the CopticChronology class.
 */
public class CopticChronologyTest {

    @Test
    public void isLeapDay_shouldReturnFalse_forDayInNonLeapYear() {
        // Arrange
        // Use a fixed time zone (UTC) for reproducible tests.
        CopticChronology copticChronology = CopticChronology.getInstance(DateTimeZone.UTC);

        // The instant 1L (one millisecond after the Unix epoch) corresponds to
        // 1970-01-01T00:00:00.001Z. This date falls within the Coptic year 1686.
        // A Coptic year is a leap year if (year % 4) == 3.
        // Since 1686 % 4 == 2, the year 1686 is not a leap year and thus has no leap day.
        long instantInNonLeapYear = 1L;

        // Act
        boolean isLeap = copticChronology.isLeapDay(instantInNonLeapYear);

        // Assert
        assertFalse("A day within a non-leap year should not be a leap day.", isLeap);
    }
}