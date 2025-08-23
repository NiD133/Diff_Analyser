package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link EthiopicChronology}.
 */
public class EthiopicChronologyTest {

    @Test
    public void isLeapDay_shouldReturnFalse_forInstantInNonLeapYear() {
        // Arrange
        // The Ethiopic calendar rule for a leap year is (year + 1) % 4 == 0.
        // For the year 1962, (1962 + 1) % 4 = 3, so it is not a leap year.
        final EthiopicChronology ethiopicChronology = EthiopicChronology.getInstanceUTC();
        
        // Create an instant representing a date within the non-leap Ethiopic year 1962.
        final DateTime dateInNonLeapYear = new DateTime(1962, 1, 1, 0, 0, ethiopicChronology);
        final long instant = dateInNonLeapYear.getMillis();

        // Act
        final boolean isLeap = ethiopicChronology.isLeapDay(instant);

        // Assert
        assertFalse("An instant within a non-leap year should not be identified as a leap day.", isLeap);
    }
}