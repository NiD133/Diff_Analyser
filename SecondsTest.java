package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

/**
 * Focused, readable tests for Seconds.
 * 
 * Conventions used:
 * - Clear test names that describe behavior.
 * - Arrange / Act / Assert layout where helpful.
 * - Helper methods/constants to remove magic numbers and incidental complexity.
 */
public class SecondsTest {

    // Time zone chosen to avoid DST edge-case confusion in these tests
    private static final DateTimeZone TZ_PARIS = DateTimeZone.forID("Europe/Paris");

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = 60 * SECONDS_PER_MINUTE;
    private static final int SECONDS_PER_DAY = 24 * SECONDS_PER_HOUR;
    private static final int SECONDS_PER_WEEK = 7 * SECONDS_PER_DAY;

    // ---------------------------------------------------------------------
    // Helpers
    private static DateTime dt(int y, int m, int d, int h, int min, int s) {
        return new DateTime(y, m, d, h, min, s, 0, TZ_PARIS);
    }

    private static Interval interval(DateTime start, DateTime end) {
        return new Interval(start, end);
    }

    private static <T> T roundTripViaJavaSerialization(T obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            @SuppressWarnings("unchecked")
            T result = (T) ois.readObject();
            return result;
        }
    }

    // ---------------------------------------------------------------------
    // Constants
    @Test
    public void constantsExposeExpectedValues() {
        assertEquals(0, Seconds.ZERO.getSeconds());
        assertEquals(1, Seconds.ONE.getSeconds());
        assertEquals(2, Seconds.TWO.getSeconds());
        assertEquals(3, Seconds.THREE.getSeconds());
        assertEquals(Integer.MAX_VALUE, Seconds.MAX_VALUE.getSeconds());
        assertEquals(Integer.MIN_VALUE, Seconds.MIN_VALUE.getSeconds());
    }

    // ---------------------------------------------------------------------
    // Factories
    @Test
    public void secondsFactoryReturnsCachedInstancesAndValues() {
        assertSame(Seconds.ZERO, Seconds.seconds(0));
        assertSame(Seconds.ONE, Seconds.seconds(1));
        assertSame(Seconds.TWO, Seconds.seconds(2));
        assertSame(Seconds.THREE, Seconds.seconds(3));
        assertSame(Seconds.MAX_VALUE, Seconds.seconds(Integer.MAX_VALUE));
        assertSame(Seconds.MIN_VALUE, Seconds.seconds(Integer.MIN_VALUE));
        assertEquals(-1, Seconds.seconds(-1).getSeconds());
        assertEquals(4, Seconds.seconds(4).getSeconds());
    }

    @Test
    public void secondsBetween_forInstants_inclusiveBehavior() {
        DateTime start = dt(2006, 6, 9, 12, 0, 3);
        DateTime endPlus3 = dt(2006, 6, 9, 12, 0, 6);
        DateTime endPlus6 = dt(2006, 6, 9, 12, 0, 9);

        assertEquals(3, Seconds.secondsBetween(start, endPlus3).getSeconds());
        assertEquals(0, Seconds.secondsBetween(start, start).getSeconds());
        assertEquals(0, Seconds.secondsBetween(endPlus3, endPlus3).getSeconds());
        assertEquals(-3, Seconds.secondsBetween(endPlus3, start).getSeconds());
        assertEquals(6, Seconds.secondsBetween(start, endPlus6).getSeconds());
    }

    @Test
    public void secondsBetween_forPartials_handlesLocalTimeAndTimeOfDay() {
        LocalTime start = new LocalTime(12, 0, 3);
        LocalTime endPlus3 = new LocalTime(12, 0, 6);
        @SuppressWarnings("deprecation")
        TimeOfDay endPlus6_timeOfDay = new TimeOfDay(12, 0, 9);

        assertEquals(3, Seconds.secondsBetween(start, endPlus3).getSeconds());
        assertEquals(0, Seconds.secondsBetween(start, start).getSeconds());
        assertEquals(0, Seconds.secondsBetween(endPlus3, endPlus3).getSeconds());
        assertEquals(-3, Seconds.secondsBetween(endPlus3, start).getSeconds());
        assertEquals(6, Seconds.secondsBetween(start, endPlus6_timeOfDay).getSeconds());
    }

    @Test
    public void secondsIn_intervalComputesWholeSeconds() {
        DateTime start = dt(2006, 6, 9, 12, 0, 3);
        DateTime endPlus3 = dt(2006, 6, 9, 12, 0, 6);
        DateTime endPlus6 = dt(2006, 6, 9, 12, 0, 9);

        assertEquals(0, Seconds.secondsIn((ReadableInterval) null).getSeconds());
        assertEquals(3, Seconds.secondsIn(interval(start, endPlus3)).getSeconds());
        assertEquals(0, Seconds.secondsIn(interval(start, start)).getSeconds());
        assertEquals(0, Seconds.secondsIn(interval(endPlus3, endPlus3)).getSeconds());
        assertEquals(6, Seconds.secondsIn(interval(start, endPlus6)).getSeconds());
    }

    @Test
    public void standardSecondsIn_convertsOnlyPreciseFields() {
        assertEquals(0, Seconds.standardSecondsIn((ReadablePeriod) null).getSeconds());
        assertEquals(0, Seconds.standardSecondsIn(Period.ZERO).getSeconds());
        assertEquals(1, Seconds.standardSecondsIn(new Period(0, 0, 0, 0, 0, 0, 1, 0)).getSeconds());
        assertEquals(123, Seconds.standardSecondsIn(Period.seconds(123)).getSeconds());
        assertEquals(-987, Seconds.standardSecondsIn(Period.seconds(-987)).getSeconds());
        assertEquals(2 * SECONDS_PER_DAY, Seconds.standardSecondsIn(Period.days(2)).getSeconds());

        assertThrows(IllegalArgumentException.class, () -> Seconds.standardSecondsIn(Period.months(1)));
    }

    @Test
    public void parseSeconds_acceptsIsoFormsWithOnlySecondsNonZero() {
        assertEquals(0, Seconds.parseSeconds((String) null).getSeconds());
        assertEquals(0, Seconds.parseSeconds("PT0S").getSeconds());
        assertEquals(1, Seconds.parseSeconds("PT1S").getSeconds());
        assertEquals(-3, Seconds.parseSeconds("PT-3S").getSeconds());
        assertEquals(2, Seconds.parseSeconds("P0Y0M0DT2S").getSeconds());
        assertEquals(2, Seconds.parseSeconds("PT0H2S").getSeconds());

        assertThrows(IllegalArgumentException.class, () -> Seconds.parseSeconds("P1Y1D"));
        assertThrows(IllegalArgumentException.class, () -> Seconds.parseSeconds("P1DT1S"));
    }

    // ---------------------------------------------------------------------
    // Introspection
    @Test
    public void getSecondsReturnsUnderlyingValue() {
        assertEquals(20, Seconds.seconds(20).getSeconds());
    }

    @Test
    public void getFieldTypeIsSeconds() {
        assertEquals(DurationFieldType.seconds(), Seconds.seconds(20).getFieldType());
    }

    @Test
    public void getPeriodTypeIsSeconds() {
        assertEquals(PeriodType.seconds(), Seconds.seconds(20).getPeriodType());
    }

    // ---------------------------------------------------------------------
    // Comparisons
    @Test
    public void comparison_isGreaterThan() {
        assertTrue(Seconds.THREE.isGreaterThan(Seconds.TWO));
        assertFalse(Seconds.THREE.isGreaterThan(Seconds.THREE));
        assertFalse(Seconds.TWO.isGreaterThan(Seconds.THREE));
        assertTrue(Seconds.ONE.isGreaterThan(null));
        assertFalse(Seconds.seconds(-1).isGreaterThan(null));
    }

    @Test
    public void comparison_isLessThan() {
        assertFalse(Seconds.THREE.isLessThan(Seconds.TWO));
        assertFalse(Seconds.THREE.isLessThan(Seconds.THREE));
        assertTrue(Seconds.TWO.isLessThan(Seconds.THREE));
        assertFalse(Seconds.ONE.isLessThan(null));
        assertTrue(Seconds.seconds(-1).isLessThan(null));
    }

    // ---------------------------------------------------------------------
    // Formatting
    @Test
    public void toStringFormatsIso8601() {
        assertEquals("PT20S", Seconds.seconds(20).toString());
        assertEquals("PT-20S", Seconds.seconds(-20).toString());
    }

    // ---------------------------------------------------------------------
    // Serialization
    @Test
    public void serialization_preservesSingletonInstances() throws Exception {
        Seconds original = Seconds.THREE;
        Seconds copy = roundTripViaJavaSerialization(original);
        assertSame(original, copy);
    }

    // ---------------------------------------------------------------------
    // Conversions to other period types and duration
    @Test
    public void toStandardWeeks_convertsExactly() {
        Seconds twoWeeksInSeconds = Seconds.seconds(SECONDS_PER_WEEK * 2);
        assertEquals(Weeks.weeks(2), twoWeeksInSeconds.toStandardWeeks());
    }

    @Test
    public void toStandardDays_convertsExactly() {
        Seconds twoDaysInSeconds = Seconds.seconds(SECONDS_PER_DAY * 2);
        assertEquals(Days.days(2), twoDaysInSeconds.toStandardDays());
    }

    @Test
    public void toStandardHours_convertsExactly() {
        Seconds twoHoursInSeconds = Seconds.seconds(SECONDS_PER_HOUR * 2);
        assertEquals(Hours.hours(2), twoHoursInSeconds.toStandardHours());
    }

    @Test
    public void toStandardMinutes_convertsExactly() {
        Seconds twoMinutesInSeconds = Seconds.seconds(SECONDS_PER_MINUTE * 2);
        assertEquals(Minutes.minutes(2), twoMinutesInSeconds.toStandardMinutes());
    }

    @Test
    public void toStandardDuration_convertsToMillis() {
        Seconds twenty = Seconds.seconds(20);
        Duration expected = new Duration(20L * DateTimeConstants.MILLIS_PER_SECOND);
        assertEquals(expected, twenty.toStandardDuration());

        Duration expectedMax =
            new Duration(((long) Integer.MAX_VALUE) * DateTimeConstants.MILLIS_PER_SECOND);
        assertEquals(expectedMax, Seconds.MAX_VALUE.toStandardDuration());
    }

    // ---------------------------------------------------------------------
    // Arithmetic - plus
    @Test
    public void plus_withPrimitiveAddsAndDoesNotMutate() {
        Seconds two = Seconds.seconds(2);
        Seconds result = two.plus(3);

        assertEquals(2, two.getSeconds());
        assertEquals(5, result.getSeconds());
        assertEquals(1, Seconds.ONE.plus(0).getSeconds());
    }

    @Test
    public void plus_withPrimitive_overflowThrows() {
        assertThrows(ArithmeticException.class, () -> Seconds.MAX_VALUE.plus(1));
    }

    @Test
    public void plus_withSecondsAddsAndHandlesNull() {
        Seconds two = Seconds.seconds(2);
        Seconds three = Seconds.seconds(3);
        Seconds result = two.plus(three);

        assertEquals(2, two.getSeconds());
        assertEquals(3, three.getSeconds());
        assertEquals(5, result.getSeconds());

        assertEquals(1, Seconds.ONE.plus(Seconds.ZERO).getSeconds());
        assertEquals(1, Seconds.ONE.plus((Seconds) null).getSeconds());
    }

    @Test
    public void plus_withSeconds_overflowThrows() {
        assertThrows(ArithmeticException.class, () -> Seconds.MAX_VALUE.plus(Seconds.ONE));
    }

    // ---------------------------------------------------------------------
    // Arithmetic - minus
    @Test
    public void minus_withPrimitiveSubtractsAndDoesNotMutate() {
        Seconds two = Seconds.seconds(2);
        Seconds result = two.minus(3);

        assertEquals(2, two.getSeconds());
        assertEquals(-1, result.getSeconds());
        assertEquals(1, Seconds.ONE.minus(0).getSeconds());
    }

    @Test
    public void minus_withPrimitive_overflowThrows() {
        assertThrows(ArithmeticException.class, () -> Seconds.MIN_VALUE.minus(1));
    }

    @Test
    public void minus_withSecondsSubtractsAndHandlesNull() {
        Seconds two = Seconds.seconds(2);
        Seconds three = Seconds.seconds(3);
        Seconds result = two.minus(three);

        assertEquals(2, two.getSeconds());
        assertEquals(3, three.getSeconds());
        assertEquals(-1, result.getSeconds());

        assertEquals(1, Seconds.ONE.minus(Seconds.ZERO).getSeconds());
        assertEquals(1, Seconds.ONE.minus((Seconds) null).getSeconds());
    }

    @Test
    public void minus_withSeconds_overflowThrows() {
        assertThrows(ArithmeticException.class, () -> Seconds.MIN_VALUE.minus(Seconds.ONE));
    }

    // ---------------------------------------------------------------------
    // Arithmetic - multipliedBy / dividedBy / negated
    @Test
    public void multipliedBy_scalesValueAndDoesNotMutate() {
        Seconds two = Seconds.seconds(2);
        assertEquals(6, two.multipliedBy(3).getSeconds());
        assertEquals(2, two.getSeconds());
        assertEquals(-6, two.multipliedBy(-3).getSeconds());
        assertSame(two, two.multipliedBy(1));
    }

    @Test
    public void multipliedBy_overflowThrows() {
        Seconds halfMaxPlusOne = Seconds.seconds(Integer.MAX_VALUE / 2 + 1);
        assertThrows(ArithmeticException.class, () -> halfMaxPlusOne.multipliedBy(2));
    }

    @Test
    public void dividedBy_usesIntegerDivisionAndDoesNotMutate() {
        Seconds twelve = Seconds.seconds(12);
        assertEquals(6, twelve.dividedBy(2).getSeconds());
        assertEquals(12, twelve.getSeconds());
        assertEquals(4, twelve.dividedBy(3).getSeconds());
        assertEquals(3, twelve.dividedBy(4).getSeconds());
        assertEquals(2, twelve.dividedBy(5).getSeconds());
        assertEquals(2, twelve.dividedBy(6).getSeconds());
        assertSame(twelve, twelve.dividedBy(1));
    }

    @Test
    public void dividedBy_byZeroThrows() {
        assertThrows(ArithmeticException.class, () -> Seconds.ONE.dividedBy(0));
    }

    @Test
    public void negated_flipsSignWithoutMutation() {
        Seconds twelve = Seconds.seconds(12);
        assertEquals(-12, twelve.negated().getSeconds());
        assertEquals(12, twelve.getSeconds());
    }

    @Test
    public void negated_overflowOnMinValue() {
        assertThrows(ArithmeticException.class, () -> Seconds.MIN_VALUE.negated());
    }

    // ---------------------------------------------------------------------
    // Interop with other time types
    @Test
    public void plusToLocalDateTime_addsSeconds() {
        Seconds twentySix = Seconds.seconds(26);
        LocalDateTime base = new LocalDateTime(2006, 6, 1, 0, 0, 0, 0);
        LocalDateTime expected = new LocalDateTime(2006, 6, 1, 0, 0, 26, 0);

        assertEquals(expected, base.plus(twentySix));
    }
}