package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link InternationalFixedChronology} class.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that {@link InternationalFixedChronology#dateEpochDay(long)} correctly
     * constructs a date within a long month of a non-leap year.
     */
    @Test
    public void dateEpochDay_forLongMonthInNonLeapYear_hasCorrectYearAndMonthLength() {
        // ARRANGE
        // According to the International Fixed Calendar rules:
        // 1. A non-leap year has 365 days.
        // 2. Month 12 is always a "long month" with 29 days.
        // We choose a reference date in month 12 of a known non-leap year (1971) to verify this.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        InternationalFixedDate referenceDate = InternationalFixedDate.of(1971, 12, 15);
        long epochDayForReferenceDate = referenceDate.toEpochDay();

        // ACT
        // Create a date from its epoch day value. This is the method under test.
        InternationalFixedDate dateFromEpochDay = chronology.dateEpochDay(epochDayForReferenceDate);

        // ASSERT
        // The created date should have the correct length for its year and month,
        // and it should be equal to the original reference date.
        assertEquals("A non-leap year should have 365 days", 365, dateFromEpochDay.lengthOfYear());
        assertEquals("Month 12 should have 29 days", 29, dateFromEpochDay.lengthOfMonth());
        assertEquals("The date created from the epoch day should match the reference date",
                referenceDate, dateFromEpochDay);
    }
}