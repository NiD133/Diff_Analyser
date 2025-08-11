package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

/**
 * Unit tests for Minutes.
 * 
 * Goals:
 * - Use descriptive test names that explain behavior and intent.
 * - Reduce duplication with small helpers for constructing common objects.
 * - Prefer assertThrows over try/catch for exception expectations.
 * - Keep assertions focused and readable.
 */
public class TestMinutes {

    // Fixed, well-known time zone to avoid DST surprises in tests.
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    // ---------- helpers ----------

    private static DateTime dt(int y, int m, int d, int h, int min, int s, int ms) {
        return new DateTime(y, m, d, h, min, s, ms, PARIS);
    }

    private static LocalDateTime ldt(int y, int m, int d, int h, int min) {
        return new LocalDateTime(y, m, d, h, min, 0, 0);
    }

    private static Minutes minutes(int value) {
        return Minutes.minutes(value);
    }

    private static Interval interval(DateTime start, DateTime end) {
        return new Interval(start, end);
    }

    private static int weeksToMinutes(int weeks) {
        return weeks * DateTimeConstants.DAYS_PER_WEEK
               * DateTimeConstants.HOURS_PER_DAY
               * DateTimeConstants.MINUTES_PER_HOUR;
    }

    private static int daysToMinutes(int days) {
        return days * DateTimeConstants.HOURS_PER_DAY
               * DateTimeConstants.MINUTES_PER_HOUR;
    }

    private static int hoursToMinutes(int hours) {
        return hours * DateTimeConstants.MINUTES_PER_HOUR;
    }

    // ---------- constants ----------

    @Test
    public void constants_arePredefinedSingletonsWithExpectedValues() {
        assertEquals(0, Minutes.ZERO.getMinutes());
        assertEquals(1, Minutes.ONE.getMinutes());
        assertEquals(2, Minutes.TWO.getMinutes());
        assertEquals(3, Minutes.THREE.getMinutes());
        assertEquals(Integer.MAX_VALUE, Minutes.MAX_VALUE.getMinutes());
        assertEquals(Integer.MIN_VALUE, Minutes.MIN_VALUE.getMinutes());
    }

    // ---------- factories ----------

    @Test
    public void minutes_factory_returnsCachedInstances() {
        assertSame(Minutes.ZERO, minutes(0));
        assertSame(Minutes.ONE, minutes(1));
        assertSame(Minutes.TWO, minutes(2));
        assertSame(Minutes.THREE, minutes(3));
        assertSame(Minutes.MAX_VALUE, minutes(Integer.MAX_VALUE));
        assertSame(Minutes.MIN_VALUE, minutes(Integer.MIN_VALUE));

        assertEquals(-1, minutes(-1).getMinutes());
        assertEquals(4, minutes(4).getMinutes());
    }

    @Test
    public void minutesBetween_instants_returnsWholeMinutesAndSigns() {
        DateTime start = dt(2006, 6, 9, 12, 3, 0, 0);
        DateTime endPlus3 = dt(2006, 6, 9, 12, 6, 0, 0);
        DateTime endPlus6 = dt(2006, 6, 9, 12, 9, 0, 0);

        assertEquals(3, Minutes.minutesBetween(start, endPlus3).getMinutes());
        assertEquals(0, Minutes.minutesBetween(start, start).getMinutes());
        assertEquals(0, Minutes.minutesBetween(endPlus3, endPlus3).getMinutes());
        assertEquals(-3, Minutes.minutesBetween(endPlus3, start).getMinutes());
        assertEquals(6, Minutes.minutesBetween(start, endPlus6).getMinutes());
    }

    @Test
    public void minutesBetween_partials_returnsWholeMinutesAndSigns() {
        LocalTime start = new LocalTime(12, 3);
        LocalTime endPlus3 = new LocalTime(12, 6);
        @SuppressWarnings("deprecation")
        TimeOfDay endPlus6 = new TimeOfDay(12, 9);

        assertEquals(3, Minutes.minutesBetween(start, endPlus3).getMinutes());
        assertEquals(0, Minutes.minutesBetween(start, start).getMinutes());
        assertEquals(0, Minutes.minutesBetween(endPlus3, endPlus3).getMinutes());
        assertEquals(-3, Minutes.minutesBetween(endPlus3, start).getMinutes());
        assertEquals(6, Minutes.minutesBetween(start, endPlus6).getMinutes());
    }

    @Test
    public void minutesIn_interval_nullIsZero_andBoundsInclusiveExclusive() {
        DateTime start = dt(2006, 6, 9, 12, 3, 0, 0);
        DateTime endPlus3 = dt(2006, 6, 9, 12, 6, 0, 0);
        DateTime endPlus6 = dt(2006, 6, 9, 12, 9, 0, 0);

        assertEquals(0, Minutes.minutesIn((ReadableInterval) null).getMinutes());
        assertEquals(3, Minutes.minutesIn(interval(start, endPlus3)).getMinutes());
        assertEquals(0, Minutes.minutesIn(interval(start, start)).getMinutes());
        assertEquals(0, Minutes.minutesIn(interval(endPlus3, endPlus3)).getMinutes());
        assertEquals(6, Minutes.minutesIn(interval(start, endPlus6)).getMinutes());
    }

    @Test
    public void standardMinutesIn_period_convertsPreciselyOrThrows() {
        assertEquals(0, Minutes.standardMinutesIn((ReadablePeriod) null).getMinutes());
        assertEquals(0, Minutes.standardMinutesIn(Period.ZERO).getMinutes());
        assertEquals(1, Minutes.standardMinutesIn(new Period(0, 0, 0, 0, 0, 1, 0, 0)).getMinutes());
        assertEquals(123, Minutes.standardMinutesIn(Period.minutes(123)).getMinutes());
        assertEquals(-987, Minutes.standardMinutesIn(Period.minutes(-987)).getMinutes());
        assertEquals(1, Minutes.standardMinutesIn(Period.seconds(119)).getMinutes());
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(120)).getMinutes());
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(121)).getMinutes());
        assertEquals(120, Minutes.standardMinutesIn(Period.hours(2)).getMinutes());

        assertThrows(IllegalArgumentException.class, () -> Minutes.standardMinutesIn(Period.months(1)));
    }

    @Test
    public void parseMinutes_parsesIsoStringsOrThrows() {
        assertEquals(0, Minutes.parseMinutes((String) null).getMinutes());
        assertEquals(0, Minutes.parseMinutes("PT0M").getMinutes());
        assertEquals(1, Minutes.parseMinutes("PT1M").getMinutes());
        assertEquals(-3, Minutes.parseMinutes("PT-3M").getMinutes());
        assertEquals(2, Minutes.parseMinutes("P0Y0M0DT2M").getMinutes());
        assertEquals(2, Minutes.parseMinutes("PT0H2M").getMinutes());

        assertThrows(IllegalArgumentException.class, () -> Minutes.parseMinutes("P1Y1D"));
        assertThrows(IllegalArgumentException.class, () -> Minutes.parseMinutes("P1DT1M"));
    }

    // ---------- accessors & metadata ----------

    @Test
    public void getMinutes_returnsUnderlyingValue() {
        assertEquals(20, minutes(20).getMinutes());
    }

    @Test
    public void getFieldType_isMinutes() {
        assertEquals(DurationFieldType.minutes(), minutes(20).getFieldType());
    }

    @Test
    public void getPeriodType_isMinutes() {
        assertEquals(PeriodType.minutes(), minutes(20).getPeriodType());
    }

    // ---------- comparisons ----------

    @Test
    public void isGreaterThan_comparesByValue_nullMeansZero() {
        assertTrue(Minutes.THREE.isGreaterThan(Minutes.TWO));
        assertFalse(Minutes.THREE.isGreaterThan(Minutes.THREE));
        assertFalse(Minutes.TWO.isGreaterThan(Minutes.THREE));
        assertTrue(Minutes.ONE.isGreaterThan(null));
        assertFalse(minutes(-1).isGreaterThan(null));
    }

    @Test
    public void isLessThan_comparesByValue_nullMeansZero() {
        assertFalse(Minutes.THREE.isLessThan(Minutes.TWO));
        assertFalse(Minutes.THREE.isLessThan(Minutes.THREE));
        assertTrue(Minutes.TWO.isLessThan(Minutes.THREE));
        assertFalse(Minutes.ONE.isLessThan(null));
        assertTrue(minutes(-1).isLessThan(null));
    }

    // ---------- formatting ----------

    @Test
    public void toString_outputsIso8601Minutes() {
        assertEquals("PT20M", minutes(20).toString());
        assertEquals("PT-20M", minutes(-20).toString());
    }

    // ---------- serialization ----------

    @Test
    public void serialization_preservesSingletons() throws Exception {
        Minutes original = Minutes.THREE;

        byte[] bytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
            bytes = baos.toByteArray();
        }

        Minutes roundTripped;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            roundTripped = (Minutes) ois.readObject();
        }

        assertSame(original, roundTripped);
    }

    // ---------- conversions ----------

    @Test
    public void toStandardWeeks_convertsExactly() {
        Minutes twoWeeks = minutes(weeksToMinutes(2));
        assertEquals(Weeks.weeks(2), twoWeeks.toStandardWeeks());
    }

    @Test
    public void toStandardDays_convertsExactly() {
        Minutes twoDays = minutes(daysToMinutes(2));
        assertEquals(Days.days(2), twoDays.toStandardDays());
    }

    @Test
    public void toStandardHours_convertsExactly() {
        Minutes threeHours = minutes(hoursToMinutes(3));
        assertEquals(Hours.hours(3), threeHours.toStandardHours());
    }

    @Test
    public void toStandardSeconds_convertsOrThrowsOnOverflow() {
        assertEquals(Seconds.seconds(3 * DateTimeConstants.SECONDS_PER_MINUTE),
                     minutes(3).toStandardSeconds());

        assertThrows(ArithmeticException.class, () -> Minutes.MAX_VALUE.toStandardSeconds());
    }

    @Test
    public void toStandardDuration_convertsIncludingMaxValue() {
        assertEquals(new Duration(20L * DateTimeConstants.MILLIS_PER_MINUTE),
                     minutes(20).toStandardDuration());

        assertEquals(new Duration(((long) Integer.MAX_VALUE) * DateTimeConstants.MILLIS_PER_MINUTE),
                     Minutes.MAX_VALUE.toStandardDuration());
    }

    // ---------- arithmetic ----------

    @Test
    public void plus_int_addsOrThrowsOnOverflow() {
        Minutes two = minutes(2);
        Minutes result = two.plus(3);
        assertEquals(2, two.getMinutes());
        assertEquals(5, result.getMinutes());

        assertEquals(1, Minutes.ONE.plus(0).getMinutes());

        assertThrows(ArithmeticException.class, () -> Minutes.MAX_VALUE.plus(1));
    }

    @Test
    public void plus_minutes_addsOrTreatsNullAsZeroOrThrows() {
        Minutes two = minutes(2);
        Minutes three = minutes(3);
        Minutes result = two.plus(three);
        assertEquals(2, two.getMinutes());
        assertEquals(3, three.getMinutes());
        assertEquals(5, result.getMinutes());

        assertEquals(1, Minutes.ONE.plus(Minutes.ZERO).getMinutes());
        assertEquals(1, Minutes.ONE.plus((Minutes) null).getMinutes());

        assertThrows(ArithmeticException.class, () -> Minutes.MAX_VALUE.plus(Minutes.ONE));
    }

    @Test
    public void minus_int_subtractsOrThrowsOnUnderflow() {
        Minutes two = minutes(2);
        Minutes result = two.minus(3);
        assertEquals(2, two.getMinutes());
        assertEquals(-1, result.getMinutes());

        assertEquals(1, Minutes.ONE.minus(0).getMinutes());

        assertThrows(ArithmeticException.class, () -> Minutes.MIN_VALUE.minus(1));
    }

    @Test
    public void minus_minutes_subtractsOrTreatsNullAsZeroOrThrows() {
        Minutes two = minutes(2);
        Minutes three = minutes(3);
        Minutes result = two.minus(three);
        assertEquals(2, two.getMinutes());
        assertEquals(3, three.getMinutes());
        assertEquals(-1, result.getMinutes());

        assertEquals(1, Minutes.ONE.minus(Minutes.ZERO).getMinutes());
        assertEquals(1, Minutes.ONE.minus((Minutes) null).getMinutes());

        assertThrows(ArithmeticException.class, () -> Minutes.MIN_VALUE.minus(Minutes.ONE));
    }

    @Test
    public void multipliedBy_scalesOrThrowsOnOverflow() {
        Minutes two = minutes(2);
        assertEquals(6, two.multipliedBy(3).getMinutes());
        assertEquals(2, two.getMinutes());
        assertEquals(-6, two.multipliedBy(-3).getMinutes());
        assertSame(two, two.multipliedBy(1));

        Minutes halfMaxPlusOne = minutes(Integer.MAX_VALUE / 2 + 1);
        assertThrows(ArithmeticException.class, () -> halfMaxPlusOne.multipliedBy(2));
    }

    @Test
    public void dividedBy_usesIntegerDivisionAndThrowsOnZero() {
        Minutes twelve = minutes(12);
        assertEquals(6, twelve.dividedBy(2).getMinutes());
        assertEquals(12, twelve.getMinutes());
        assertEquals(4, twelve.dividedBy(3).getMinutes());
        assertEquals(3, twelve.dividedBy(4).getMinutes());
        assertEquals(2, twelve.dividedBy(5).getMinutes());
        assertEquals(2, twelve.dividedBy(6).getMinutes());
        assertSame(twelve, twelve.dividedBy(1));

        assertThrows(ArithmeticException.class, () -> Minutes.ONE.dividedBy(0));
    }

    @Test
    public void negated_invertsOrThrowsOnOverflow() {
        Minutes twelve = minutes(12);
        assertEquals(-12, twelve.negated().getMinutes());
        assertEquals(12, twelve.getMinutes());

        assertThrows(ArithmeticException.class, () -> Minutes.MIN_VALUE.negated());
    }

    // ---------- adding to other types ----------

    @Test
    public void addToLocalDateTime_addsMinutes() {
        LocalDateTime base = ldt(2006, 6, 1, 0, 0);
        LocalDateTime expected = ldt(2006, 6, 1, 0, 26);
        assertEquals(expected, base.plus(minutes(26)));
    }
}