package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void date_fromYearMonthDay_calculatesCorrectEpochDay() {
        // Arrange: Set up the chronology and the date components.
        // Use the singleton instance as recommended by the class documentation.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        int year = 3309;
        int month = 8;
        int dayOfMonth = 10;

        // The expected epoch day for 3309-08-10 in the International Fixed calendar.
        // This value is a known, pre-calculated constant for this specific date.
        long expectedEpochDay = 489265L;

        // Act: Create an InternationalFixedDate using the chronology.
        InternationalFixedDate ifcDate = chronology.date(year, month, dayOfMonth);

        // Assert: Verify that the calculated epoch day matches the expected value.
        assertEquals(expectedEpochDay, ifcDate.toEpochDay());
    }
}