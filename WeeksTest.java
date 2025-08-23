package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

/**
 * Focused, readable tests for Weeks.
 * Notes:
 * - Times are set to noon in Europe/Paris to avoid DST edge cases.
 * - Helper constants and methods reduce duplication and magic numbers.
 */
public class TestWeeks {

    // Well-known time zone for date-time tests
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    // Common time constants to make intent explicit
    private static final int DAYS_PER_WEEK = 7;
    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;

    // Helpers
    private static DateTime atNoonParis(int year, int month, int day) {
        return new DateTime(year, month, day, 12, 0, 0, 0, PARIS);
    }

    private static void assertWeeksValue(int expected, Weeks actual) {
        assertEquals("Weeks value mismatch", expected, actual.getWeeks());
    }

    // ---------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------

    @Test
    public void constants_shouldExposeCanonicalInstances() {
        assertWeeksValue(0, Weeks.ZERO);
        assertWeeksValue(1, Weeks.ONE);
        assertWeeksValue(2, Weeks.TWO);
        assertWeeksValue(3, Weeks.THREE);
        assertWeeksValue(Integer.MAX_VALUE, Weeks.MAX_VALUE);
        assertWeeksValue(Integer.MIN_VALUE, Weeks.MIN_VALUE);
    }

    // ---------------------------------------------------------------------
    // Factory: weeks(int)
    // ---------------------------------------------------------------------

    @Test
    public void weeks_int_shouldReturnCachedInstancesForCommonValues() {
        assertSame(Weeks.ZERO, Weeks.weeks(0));
        assertSame(Weeks.ONE, Weeks.weeks(1));
        assertSame(Weeks.TWO, Weeks.weeks(2));
        assertSame(Weeks.THREE, Weeks.weeks(3));
        assertSame(Weeks.MAX_VALUE, Weeks.weeks(Integer.MAX_VALUE));
        assertSame(Weeks.MIN_VALUE, Weeks.weeks(Integer.MIN_VALUE));

        assertWeeksValue(-1, Weeks.weeks(-1));
        assertWeeksValue(4, Weeks.weeks(4));
    }

    // ---------------------------------------------------------------------
    // Factory: weeksBetween(ReadableInstant, ReadableInstant)
    // ---------------------------------------------------------------------

    @Test
    public void weeksBetween_instant_shouldReturnWholeWeeks() {
        DateTime start = atNoonParis(2006, 6, 9);
        DateTime end1 = atNoonParis(2006, 6, 30);
        DateTime end2 = atNoonParis(2006, 7, 21);

        assertWeeksValue(3, Weeks.weeksBetween(start, end1));
        assertWeeksValue(0, Weeks.weeksBetween(start, start));
        assertWeeksValue(0, Weeks.weeksBetween(end1, end1));
        assertWeeksValue(-3, Weeks.weeksBetween(end1, start));
        assertWeeksValue(6, Weeks.weeksBetween(start, end2));
    }

    // ---------------------------------------------------------------------
    // Factory: weeksBetween(ReadablePartial, ReadablePartial)
    // ---------------------------------------------------------------------

    @Test
    @SuppressWarnings("deprecation")
    public void weeksBetween_partial_shouldReturnWholeWeeks() {
        LocalDate start = new LocalDate(2006, 6, 9);
        LocalDate end1 = new LocalDate(2006, 6, 30);
        YearMonthDay end2 = new YearMonthDay(2006, 7, 21);

        assertWeeksValue(3, Weeks.weeksBetween(start, end1));
        assertWeeksValue(0, Weeks.weeksBetween(start, start));
        assertWeeksValue(0, Weeks.weeksBetween(end1, end1));
        assertWeeksValue(-3, Weeks.weeksBetween(end1, start));
        assertWeeksValue(6, Weeks.weeksBetween(start, end2));
    }

    // ---------------------------------------------------------------------
    // Factory: weeksIn(ReadableInterval)
    // ---------------------------------------------------------------------

    @Test
    public void weeksIn_interval_shouldReturnWholeWeeks() {
        DateTime start = atNoonParis(2006, 6, 9);
        DateTime end1 = atNoonParis(2006, 6, 30);
        DateTime end2 = atNoonParis(2006, 7, 21);

        assertWeeksValue(0, Weeks.weeksIn((ReadableInterval) null));
        assertWeeksValue(3, Weeks.weeksIn(new Interval(start, end1)));
        assertWeeksValue(0, Weeks.weeksIn(new Interval(start, start)));
        assertWeeksValue(0, Weeks.weeksIn(new Interval(end1, end1)));
        assertWeeksValue(6, Weeks.weeksIn(new Interval(start, end2)));
    }

    // ---------------------------------------------------------------------
    // Factory: standardWeeksIn(ReadablePeriod)
    // ---------------------------------------------------------------------

    @Test
    public void standardWeeksIn_period_shouldConvertOnlyPreciseFields() {
        assertWeeksValue(0, Weeks.standardWeeksIn((ReadablePeriod) null));
        assertWeeksValue(0, Weeks.standardWeeksIn(Period.ZERO));
        assertWeeksValue(1, Weeks.standardWeeksIn(new Period(0, 0, 1, 0, 0, 0, 0, 0)));
        assertWeeksValue(123, Weeks.standardWeeksIn(Period.weeks(123)));
        assertWeeksValue(-987, Weeks.standardWeeksIn(Period.weeks(-987)));
        assertWeeksValue(1, Weeks.standardWeeksIn(Period.days(13))); // 13 days -> 1 week
        assertWeeksValue(2, Weeks.standardWeeksIn(Period.days(14))); // 14 days -> 2 weeks
        assertWeeksValue(2, Weeks.standardWeeksIn(Period.days(15))); // 15 days -> 2 weeks

        assertThrows(IllegalArgumentException.class, () -> Weeks.standardWeeksIn(Period.months(1)));
    }

    // ---------------------------------------------------------------------
    // Factory: parseWeeks(String)
    // ---------------------------------------------------------------------

    @Test
    public void parseWeeks_shouldParseIso8601WeeksOnly() {
        assertWeeksValue(0, Weeks.parseWeeks((String) null));
        assertWeeksValue(0, Weeks.parseWeeks("P0W"));
        assertWeeksValue(1, Weeks.parseWeeks("P1W"));
        assertWeeksValue(-3, Weeks.parseWeeks("P-3W"));
        assertWeeksValue(2, Weeks.parseWeeks("P0Y0M2W"));
        assertWeeksValue(2, Weeks.parseWeeks("P2WT0H0M"));

        assertThrows(IllegalArgumentException.class, () -> Weeks.parseWeeks("P1Y1D"));
        assertThrows(IllegalArgumentException.class, () -> Weeks.parseWeeks("P1WT1H"));
    }

    // ---------------------------------------------------------------------
    // Accessors
    // ---------------------------------------------------------------------

    @Test
    public void getWeeks_returnsUnderlyingValue() {
        assertWeeksValue(20, Weeks.weeks(20));
    }

    @Test
    public void getFieldType_returnsWeeks() {
        assertEquals(DurationFieldType.weeks(), Weeks.weeks(20).getFieldType());
    }

    @Test
    public void getPeriodType_returnsWeeks() {
        assertEquals(PeriodType.weeks(), Weeks.weeks(20).getPeriodType());
    }

    // ---------------------------------------------------------------------
    // Comparators
    // ---------------------------------------------------------------------

    @Test
    public void isGreaterThan_comparesToOtherInstancesAndNull() {
        assertEquals(true, Weeks.THREE.isGreaterThan(Weeks.TWO));
        assertEquals(false, Weeks.THREE.isGreaterThan(Weeks.THREE));
        assertEquals(false, Weeks.TWO.isGreaterThan(Weeks.THREE));
        assertEquals(true, Weeks.ONE.isGreaterThan(null));
        assertEquals(false, Weeks.weeks(-1).isGreaterThan(null));
    }

    @Test
    public void isLessThan_comparesToOtherInstancesAndNull() {
        assertEquals(false, Weeks.THREE.isLessThan(Weeks.TWO));
        assertEquals(false, Weeks.THREE.isLessThan(Weeks.THREE));
        assertEquals(true, Weeks.TWO.isLessThan(Weeks.THREE));
        assertEquals(false, Weeks.ONE.isLessThan(null));
        assertEquals(true, Weeks.weeks(-1).isLessThan(null));
    }

    // ---------------------------------------------------------------------
    // String representation
    // ---------------------------------------------------------------------

    @Test
    public void toString_returnsIso8601Weeks() {
        assertEquals("P20W", Weeks.weeks(20).toString());
        assertEquals("P-20W", Weeks.weeks(-20).toString());
    }

    // ---------------------------------------------------------------------
    // Serialization
    // ---------------------------------------------------------------------

    @Test
    public void serialization_preservesSingletons() throws Exception {
        Weeks original = Weeks.THREE;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }

        byte[] bytes = baos.toByteArray();

        Weeks deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            deserialized = (Weeks) ois.readObject();
        }

        assertSame("Deserialized instance should resolve to the same singleton", original, deserialized);
    }

    // ---------------------------------------------------------------------
    // Conversions to other units and Duration
    // ---------------------------------------------------------------------

    @Test
    public void toStandardDays_convertsSafelyAndDetectsOverflow() {
        assertEquals(Days.days(2 * DAYS_PER_WEEK), Weeks.weeks(2).toStandardDays());
        assertThrows(ArithmeticException.class, () -> Weeks.MAX_VALUE.toStandardDays());
    }

    @Test
    public void toStandardHours_convertsSafelyAndDetectsOverflow() {
        assertEquals(Hours.hours(2 * DAYS_PER_WEEK * HOURS_PER_DAY), Weeks.weeks(2).toStandardHours());
        assertThrows(ArithmeticException.class, () -> Weeks.MAX_VALUE.toStandardHours());
    }

    @Test
    public void toStandardMinutes_convertsSafelyAndDetectsOverflow() {
        int minutes = 2 * DAYS_PER_WEEK * HOURS_PER_DAY * MINUTES_PER_HOUR;
        assertEquals(Minutes.minutes(minutes), Weeks.weeks(2).toStandardMinutes());
        assertThrows(ArithmeticException.class, () -> Weeks.MAX_VALUE.toStandardMinutes());
    }

    @Test
    public void toStandardSeconds_convertsSafelyAndDetectsOverflow() {
        int seconds = 2 * DAYS_PER_WEEK * HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE;
        assertEquals(Seconds.seconds(seconds), Weeks.weeks(2).toStandardSeconds());
        assertThrows(ArithmeticException.class, () -> Weeks.MAX_VALUE.toStandardSeconds());
    }

    @Test
    public void toStandardDuration_convertsUsingMillisPerWeek() {
        Weeks twenty = Weeks.weeks(20);
        Duration expected = new Duration(20L * DateTimeConstants.MILLIS_PER_WEEK);
        assertEquals(expected, twenty.toStandardDuration());

        Duration expectedMax = new Duration(((long) Integer.MAX_VALUE) * DateTimeConstants.MILLIS_PER_WEEK);
        assertEquals(expectedMax, Weeks.MAX_VALUE.toStandardDuration());
    }

    // ---------------------------------------------------------------------
    // Arithmetic operations
    // ---------------------------------------------------------------------

    @Test
    public void plus_int_addsAndDetectsOverflow() {
        Weeks two = Weeks.weeks(2);
        assertWeeksValue(2, two); // original unchanged
        assertWeeksValue(5, two.plus(3));
        assertWeeksValue(1, Weeks.ONE.plus(0));

        assertThrows(ArithmeticException.class, () -> Weeks.MAX_VALUE.plus(1));
    }

    @Test
    public void plus_Weeks_addsAndTreatsNullAsZero() {
        Weeks two = Weeks.weeks(2);
        Weeks three = Weeks.weeks(3);

        assertWeeksValue(5, two.plus(three));
        assertWeeksValue(1, Weeks.ONE.plus(Weeks.ZERO));
        assertWeeksValue(1, Weeks.ONE.plus((Weeks) null));

        assertThrows(ArithmeticException.class, () -> Weeks.MAX_VALUE.plus(Weeks.ONE));
    }

    @Test
    public void minus_int_subtractsAndDetectsOverflow() {
        Weeks two = Weeks.weeks(2);
        assertWeeksValue(-1, two.minus(3));
        assertWeeksValue(1, Weeks.ONE.minus(0));

        assertThrows(ArithmeticException.class, () -> Weeks.MIN_VALUE.minus(1));
    }

    @Test
    public void minus_Weeks_subtractsAndTreatsNullAsZero() {
        Weeks two = Weeks.weeks(2);
        Weeks three = Weeks.weeks(3);

        assertWeeksValue(-1, two.minus(three));
        assertWeeksValue(1, Weeks.ONE.minus(Weeks.ZERO));
        assertWeeksValue(1, Weeks.ONE.minus((Weeks) null));

        assertThrows(ArithmeticException.class, () -> Weeks.MIN_VALUE.minus(Weeks.ONE));
    }

    @Test
    public void multipliedBy_int_multipliesAndDetectsOverflow() {
        Weeks two = Weeks.weeks(2);
        assertWeeksValue(6, two.multipliedBy(3));
        assertWeeksValue(-6, two.multipliedBy(-3));
        assertSame(two, two.multipliedBy(1)); // identity

        Weeks halfMaxPlusOne = Weeks.weeks(Integer.MAX_VALUE / 2 + 1);
        assertThrows(ArithmeticException.class, () -> halfMaxPlusOne.multipliedBy(2));
    }

    @Test
    public void dividedBy_int_usesIntegerDivisionAndDetectsDivideByZero() {
        Weeks twelve = Weeks.weeks(12);
        assertWeeksValue(6, twelve.dividedBy(2));
        assertWeeksValue(4, twelve.dividedBy(3));
        assertWeeksValue(3, twelve.dividedBy(4));
        assertWeeksValue(2, twelve.dividedBy(5));
        assertWeeksValue(2, twelve.dividedBy(6));
        assertSame(twelve, twelve.dividedBy(1)); // identity

        assertThrows(ArithmeticException.class, () -> Weeks.ONE.dividedBy(0));
    }

    @Test
    public void negated_invertsSignAndDetectsOverflow() {
        Weeks twelve = Weeks.weeks(12);
        assertWeeksValue(-12, twelve.negated());
        assertWeeksValue(12, twelve); // original unchanged

        assertThrows(ArithmeticException.class, () -> Weeks.MIN_VALUE.negated());
    }

    // ---------------------------------------------------------------------
    // Interaction with other types
    // ---------------------------------------------------------------------

    @Test
    public void addToLocalDate_shouldAddCorrectNumberOfDays() {
        Weeks threeWeeks = Weeks.weeks(3);
        LocalDate start = new LocalDate(2006, 6, 1);
        LocalDate expected = new LocalDate(2006, 6, 22); // 21 days later
        assertEquals(expected, start.plus(threeWeeks));
    }
}