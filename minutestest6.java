package org.joda.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link Minutes} class, focusing on the {@code standardMinutesIn(ReadablePeriod)} factory method.
 */
class MinutesTest {

    @Test
    void standardMinutesIn_givenNullPeriod_returnsZeroMinutes() {
        assertEquals(0, Minutes.standardMinutesIn(null).getMinutes());
    }

    @Test
    void standardMinutesIn_givenZeroPeriod_returnsZeroMinutes() {
        assertEquals(0, Minutes.standardMinutesIn(Period.ZERO).getMinutes());
    }

    @Test
    void standardMinutesIn_givenPeriodWithOnlyMinutes_returnsSameAmount() {
        // Test with a positive value
        Minutes positiveResult = Minutes.standardMinutesIn(Period.minutes(123));
        assertEquals(123, positiveResult.getMinutes());

        // Test with a negative value
        Minutes negativeResult = Minutes.standardMinutesIn(Period.minutes(-987));
        assertEquals(-987, negativeResult.getMinutes());
    }

    @Test
    void standardMinutesIn_givenPeriodWithHours_convertsCorrectly() {
        Period twoHours = Period.hours(2);
        Minutes result = Minutes.standardMinutesIn(twoHours);
        assertEquals(120, result.getMinutes(), "2 hours should be 120 minutes");
    }

    @Test
    void standardMinutesIn_givenPeriodWithSeconds_truncatesToFullMinutes() {
        // 119 seconds is 1 full minute and 59 seconds, should truncate to 1
        assertEquals(1, Minutes.standardMinutesIn(Period.seconds(119)).getMinutes());

        // 120 seconds is exactly 2 minutes
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(120)).getMinutes());

        // 121 seconds is 2 full minutes and 1 second, should truncate to 2
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(121)).getMinutes());
    }

    @Test
    void standardMinutesIn_givenPeriodWithImpreciseField_throwsIllegalArgumentException() {
        // A period with months cannot be reliably converted to minutes
        Period periodWithMonths = Period.months(1);

        assertThrows(IllegalArgumentException.class, () -> {
            Minutes.standardMinutesIn(periodWithMonths);
        });
    }
}