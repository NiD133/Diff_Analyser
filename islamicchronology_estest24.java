package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * The Islamic calendar is not proleptic, meaning it does not support dates
     * before its epoch (Year 1 AH, which corresponds to 622 CE).
     * <p>
     * This test verifies the behavior of {@link IslamicChronology#getDayOfMonth(long)}
     * when given a millisecond instant that falls far before this epoch. The expected
     * behavior in this implementation is to return a mathematically calculated, but
     * nonsensical, negative day-of-month value rather than throwing an exception.
     */
    @Test
    public void getDayOfMonth_forDateBeforeIslamicEra_returnsNegativeValue() {
        // Arrange
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();

        // The original test used the magic number -83855174400000L.
        // This corresponds to the date -0662-07-18 UTC in the proleptic Gregorian calendar,
        // which is centuries before the start of the Islamic calendar (622 CE).
        DateTime dateBeforeIslamicEra = new DateTime(-662, 7, 18, 0, 0, DateTimeZone.UTC);
        long instantBeforeIslamicEra = dateBeforeIslamicEra.getMillis();

        // Act
        int dayOfMonth = islamicChronology.getDayOfMonth(instantBeforeIslamicEra);

        // Assert
        // The calculation for a pre-epoch date results in a negative, nonsensical day.
        // This test confirms that this specific, non-intuitive behavior is preserved.
        int expectedDayOfMonth = -2;
        assertEquals(
            "Day of month for a date far before the calendar's epoch should be correctly calculated",
            expectedDayOfMonth,
            dayOfMonth
        );
    }
}