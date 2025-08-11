package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Understandable tests for {@link EthiopicChronology}.
 */
public class EthiopicChronologyTest {

    private static final DateTimeZone NEW_YORK = DateTimeZone.forID("America/New_York");
    private static final DateTimeZone UTC = DateTimeZone.UTC;

    // In Joda-Time's EthiopicChronology, a year is leap if (year % 4) == 0.
    private static final int A_LEAP_YEAR = 1972;
    private static final int A_NON_LEAP_YEAR = 1973;

    // --- getInstance() Factory Methods ---

    @Test
    public void getInstance_noArgs_shouldUseDefaultZone() {
        EthiopicChronology chrono = EthiopicChronology.getInstance();
        assertEquals(DateTimeZone.getDefault(), chrono.getZone());
    }

    @Test
    public void getInstance_withUTCZone_shouldUseUTC() {
        EthiopicChronology chrono = EthiopicChronology.getInstance(UTC);
        assertEquals(UTC, chrono.getZone());
    }

    @Test
    public void getInstance_withNullZone_shouldUseDefaultZone() {
        EthiopicChronology chrono = EthiopicChronology.getInstance(null);
        assertEquals(DateTimeZone.getDefault(), chrono.getZone());
    }

    @Test
    public void getInstance_withZoneAndMinDays_shouldCreateCorrectChronology() {
        EthiopicChronology chrono = EthiopicChronology.getInstance(NEW_YORK, 1);
        assertEquals(NEW_YORK, chrono.getZone());
        assertEquals(1, chrono.getMinimumDaysInFirstWeek());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInstance_shouldThrowException_forMinDaysTooLow() {
        EthiopicChronology.getInstance(UTC, 0); // Minimum days in the first week must be at least 1.
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInstance_shouldThrowException_forMinDaysTooHigh() {
        EthiopicChronology.getInstance(UTC, 8); // Minimum days in the first week cannot be more than 7.
    }

    // --- withZone() and withUTC() ---

    @Test
    public void withZone_shouldReturnChronologyInSpecifiedZone() {
        EthiopicChronology baseChronology = EthiopicChronology.getInstance(NEW_YORK);
        Chronology utcChronology = baseChronology.withZone(UTC);

        assertEquals(UTC, utcChronology.getZone());
        assertNotSame(baseChronology, utcChronology);
    }

    @Test
    public void withZone_withSameZone_shouldReturnSameInstance() {
        EthiopicChronology baseChronology = EthiopicChronology.getInstance(NEW_YORK);
        Chronology sameChronology = baseChronology.withZone(NEW_YORK);
        assertSame(baseChronology, sameChronology);
    }

    @Test
    public void withUTC_shouldReturnChronologyInUTC() {
        EthiopicChronology baseChronology = EthiopicChronology.getInstance(NEW_YORK);
        Chronology utcChronology = baseChronology.withUTC();

        assertEquals(UTC, utcChronology.getZone());
        assertNotSame(baseChronology, utcChronology);
    }

    @Test
    public void withUTC_whenAlreadyUTC_shouldReturnSameInstance() {
        EthiopicChronology baseChronology = EthiopicChronology.getInstanceUTC();
        Chronology utcChronology = baseChronology.withUTC();
        assertSame(baseChronology, utcChronology);
    }

    // --- Leap Year Logic ---

    @Test
    public void isLeapDay_shouldReturnTrue_forLeapDay() {
        // The leap day is the 6th day of the 13th month in a leap year.
        Chronology chrono = EthiopicChronology.getInstanceUTC();
        DateTime leapDay = new DateTime(A_LEAP_YEAR, 13, 6, 0, 0, chrono);

        assertTrue(chrono.isLeapDay(leapDay.getMillis()));
    }

    @Test
    public void isLeapDay_shouldReturnFalse_forDayBeforeLeapDay() {
        Chronology chrono = EthiopicChronology.getInstanceUTC();
        DateTime dayBeforeLeapDay = new DateTime(A_LEAP_YEAR, 13, 5, 0, 0, chrono);

        assertFalse(chrono.isLeapDay(dayBeforeLeapDay.getMillis()));
    }

    @Test
    public void isLeapDay_shouldReturnFalse_forLastDayOfNonLeapYear() {
        // The last day of a non-leap year is the 5th day of the 13th month.
        Chronology chrono = EthiopicChronology.getInstanceUTC();
        DateTime lastDay = new DateTime(A_NON_LEAP_YEAR, 13, 5, 0, 0, chrono);

        assertFalse(chrono.isLeapDay(lastDay.getMillis()));
    }

    @Test(expected = IllegalFieldValueException.class)
    public void constructor_shouldThrowException_forLeapDayInNonLeapYear() {
        // Attempting to create the 6th day of the 13th month in a non-leap year should fail.
        new DateTime(A_NON_LEAP_YEAR, 13, 6, 0, 0, EthiopicChronology.getInstanceUTC());
    }

    // --- Year Boundaries and Calculations ---

    @Test
    public void calculateFirstDayOfYearMillis_forYear1_shouldBeCorrect() {
        EthiopicChronology chrono = EthiopicChronology.getInstanceUTC();
        // This instant corresponds to 0001-01-01T00:00:00.000Z in the Ethiopic calendar.
        long expectedMillis = -61894108800000L;
        assertEquals(expectedMillis, chrono.calculateFirstDayOfYearMillis(1));
    }

    @Test
    public void getMinYear_shouldReturnConstantValue() {
        EthiopicChronology chrono = EthiopicChronology.getInstance();
        assertEquals(-292269337, chrono.getMinYear());
    }

    @Test
    public void getMaxYear_shouldReturnConstantValue() {
        EthiopicChronology chrono = EthiopicChronology.getInstance();
        assertEquals(292272984, chrono.getMaxYear());
    }

    @Test
    public void getApproxMillisAtEpochDividedByTwo_shouldReturnConstantValue() {
        EthiopicChronology chrono = EthiopicChronology.getInstance();
        // This is an internal constant used for CutoverChronology, derived from the
        // difference between the Ethiopic and Gregorian epochs.
        assertEquals(30962844000000L, chrono.getApproxMillisAtEpochDividedByTwo());
    }

    // --- Edge Cases and Exceptions ---

    @Test(expected = IllegalArgumentException.class)
    public void isLeapDay_shouldThrowException_forInstantBelowMinimum() {
        // Using UTC, LimitChronology throws IllegalArgumentException for out-of-bounds instants.
        EthiopicChronology.getInstanceUTC().isLeapDay(Long.MIN_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void isLeapDay_shouldThrowException_whenZoneOffsetCausesOverflow() {
        // With a specific offset, an overflow can occur during the zone adjustment.
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-4246);
        EthiopicChronology.getInstance(offsetZone).isLeapDay(Long.MIN_VALUE);
    }
}