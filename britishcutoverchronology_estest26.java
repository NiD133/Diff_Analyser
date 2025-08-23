package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;

/**
 * Unit tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that creating a date with a month value outside the valid range (1-12)
     * throws a DateTimeException.
     */
    @Test(expected = DateTimeException.class)
    public void date_withInvalidMonth_throwsDateTimeException() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        int year = 1737;
        int invalidMonth = 1737; // Month must be between 1 and 12.
        int dayOfMonth = 1;      // Use a valid day to isolate the month error.

        // Act: This call is expected to throw a DateTimeException.
        chronology.date(year, invalidMonth, dayOfMonth);
    }
}