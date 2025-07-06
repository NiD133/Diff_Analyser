package org.joda.time;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.*;

/**
 * Test suite for the {@link Seconds} class, focusing on its core functionalities.
 * This includes testing factory methods, arithmetic operations, comparison methods,
 * and serialization.
 */
public class TestSeconds {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private DateTime baseDateTime;

    @Before
    public void setUp() {
        // Initialize a base DateTime for use in interval/between tests.
        baseDateTime = new DateTime(2006, 6, 9, 12, 0, 3, 0, PARIS);
    }

    @Test
    public void testConstants() {
        assertEquals(0, Seconds.ZERO.getSeconds());
        assertEquals(1, Seconds.ONE.getSeconds());
        assertEquals(2, Seconds.TWO.getSeconds());
        assertEquals(3, Seconds.THREE.getSeconds());
        assertEquals(Integer.MAX_VALUE, Seconds.MAX_VALUE.getSeconds());
        assertEquals(Integer.MIN_VALUE, Seconds.MIN_VALUE.getSeconds());
    }

    @Test
    public void testFactory_seconds_int() {
        // Verify that the factory method returns cached instances for common values.
        assertSame(Seconds.ZERO, Seconds.seconds(0));
        assertSame(Seconds.ONE, Seconds.seconds(1));
        assertSame(Seconds.TWO, Seconds.seconds(2));
        assertSame(Seconds.THREE, Seconds.seconds(3));
        assertSame(Seconds.MAX_VALUE, Seconds.seconds(Integer.MAX_VALUE));
        assertSame(Seconds.MIN_VALUE, Seconds.seconds(Integer.MIN_VALUE));

        // Verify that the factory method returns a new instance for other values.
        assertEquals(-1, Seconds.seconds(-1).getSeconds());
        assertEquals(4, Seconds.seconds(4).getSeconds());
    }

    @Test
    public void testFactory_secondsBetween_RInstant() {
        DateTime end1 = new DateTime(2006, 6, 9, 12, 0, 6, 0, PARIS);
        DateTime end2 = new DateTime(2006, 6, 9, 12, 0, 9, 0, PARIS);

        assertEquals(3, Seconds.secondsBetween(baseDateTime, end1).getSeconds());
        assertEquals(0, Seconds.secondsBetween(baseDateTime, baseDateTime).getSeconds());
        assertEquals(0, Seconds.secondsBetween(end1, end1).getSeconds());
        assertEquals(-3, Seconds.secondsBetween(end1, baseDateTime).getSeconds());
        assertEquals(6, Seconds.secondsBetween(baseDateTime, end2).getSeconds());
    }

    @Test
    public void testFactory_secondsBetween_RPartial() {
        LocalTime start = new LocalTime(12, 0, 3);
        LocalTime end1 = new LocalTime(12, 0, 6);
        @SuppressWarnings("deprecation")
        TimeOfDay end2 = new TimeOfDay(12, 0, 9);

        assertEquals(3, Seconds.secondsBetween(start, end1).getSeconds());
        assertEquals(0, Seconds.secondsBetween(start, start).getSeconds());
        assertEquals(0, Seconds.secondsBetween(end1, end1).getSeconds());
        assertEquals(-3, Seconds.secondsBetween(end1, start).getSeconds());
        assertEquals(6, Seconds.secondsBetween(start, end2).getSeconds());
    }

    @Test
    public void testFactory_secondsIn_RInterval() {
        DateTime end1 = new DateTime(2006, 6, 9, 12, 0, 6, 0, PARIS);
        DateTime end2 = new DateTime(2006, 6, 9, 12, 0, 9, 0, PARIS);

        assertEquals(0, Seconds.secondsIn((ReadableInterval) null).getSeconds());
        assertEquals(3, Seconds.secondsIn(new Interval(baseDateTime, end1)).getSeconds());
        assertEquals(0, Seconds.secondsIn(new Interval(baseDateTime, baseDateTime)).getSeconds());
        assertEquals(0, Seconds.secondsIn(new Interval(end1, end1)).getSeconds());
        assertEquals(6, Seconds.secondsIn(new Interval(baseDateTime, end2)).getSeconds());
    }

    @Test
    public void testFactory_standardSecondsIn_RPeriod() {
        assertEquals(0, Seconds.standardSecondsIn((ReadablePeriod) null).getSeconds());
        assertEquals(0, Seconds.standardSecondsIn(Period.ZERO).getSeconds());
        assertEquals(1, Seconds.standardSecondsIn(new Period(0, 0, 0, 0, 0, 0, 1, 0)).getSeconds());
        assertEquals(123, Seconds.standardSecondsIn(Period.seconds(123)).getSeconds());
        assertEquals(-987, Seconds.standardSecondsIn(Period.seconds(-987)).getSeconds());
        assertEquals(2 * 24 * 60 * 60, Seconds.standardSecondsIn(Period.days(2)).getSeconds());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Seconds.standardSecondsIn(Period.months(1));
        });
    }

    @Test
    public void testFactory_parseSeconds_String() {
        assertEquals(0, Seconds.parseSeconds((String) null).getSeconds());
        assertEquals(0, Seconds.parseSeconds("PT0S").getSeconds());
        assertEquals(1, Seconds.parseSeconds("PT1S").getSeconds());
        assertEquals(-3, Seconds.parseSeconds("PT-3S").getSeconds());
        assertEquals(2, Seconds.parseSeconds("P0Y0M0DT2S").getSeconds());
        assertEquals(2, Seconds.parseSeconds("PT0H2S").getSeconds());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Seconds.parseSeconds("P1Y1D");
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Seconds.parseSeconds("P1DT1S");
        });
    }

    @Test
    public void testGetMethods() {
        Seconds test = Seconds.seconds(20);
        assertEquals(20, test.getSeconds());
    }

    @Test
    public void testGetFieldType() {
        Seconds test = Seconds.seconds(20);
        assertEquals(DurationFieldType.seconds(), test.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        Seconds test = Seconds.seconds(20);
        assertEquals(PeriodType.seconds(), test.getPeriodType());
    }

    @Test
    public void testIsGreaterThan() {
        assertTrue(Seconds.THREE.isGreaterThan(Seconds.TWO));
        assertFalse(Seconds.THREE.isGreaterThan(Seconds.THREE));
        assertFalse(Seconds.TWO.isGreaterThan(Seconds.THREE));
        assertTrue(Seconds.ONE.isGreaterThan(null));
        assertFalse(Seconds.seconds(-1).isGreaterThan(null));
    }

    @Test
    public void testIsLessThan() {
        assertFalse(Seconds.THREE.isLessThan(Seconds.TWO));
        assertFalse(Seconds.THREE.isLessThan(Seconds.THREE));
        assertTrue(Seconds.TWO.isLessThan(Seconds.THREE));
        assertFalse(Seconds.ONE.isLessThan(null));
        assertTrue(Seconds.seconds(-1).isLessThan(null));
    }

    @Test
    public void testToString() {
        Seconds test = Seconds.seconds(20);
        assertEquals("PT20S", test.toString());

        test = Seconds.seconds(-20);
        assertEquals("PT-20S", test.toString());
    }

    @Test
    public void testSerialization() throws Exception {
        Seconds test = Seconds.THREE;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        oos.close();
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Seconds result = (Seconds) ois.readObject();
        ois.close();

        assertSame(test, result);
    }

    @Test
    public void testToStandardWeeks() {
        Seconds test = Seconds.seconds(60 * 60 * 24 * 7 * 2);
        Weeks expected = Weeks.weeks(2);
        assertEquals(expected, test.toStandardWeeks());
    }

    @Test
    public void testToStandardDays() {
        Seconds test = Seconds.seconds(60 * 60 * 24 * 2);
        Days expected = Days.days(2);
        assertEquals(expected, test.toStandardDays());
    }

    @Test
    public void testToStandardHours() {
        Seconds test = Seconds.seconds(60 * 60 * 2);
        Hours expected = Hours.hours(2);
        assertEquals(expected, test.toStandardHours());
    }

    @Test
    public void testToStandardMinutes() {
        Seconds test = Seconds.seconds(60 * 2);
        Minutes expected = Minutes.minutes(2);
        assertEquals(expected, test.toStandardMinutes());
    }

    @Test
    public void testToStandardDuration() {
        Seconds test = Seconds.seconds(20);
        Duration expected = new Duration(20L * DateTimeConstants.MILLIS_PER_SECOND);
        assertEquals(expected, test.toStandardDuration());

        expected = new Duration(((long) Integer.MAX_VALUE) * DateTimeConstants.MILLIS_PER_SECOND);
        assertEquals(expected, Seconds.MAX_VALUE.toStandardDuration());
    }

    @Test
    public void testPlus_int() {
        Seconds test2 = Seconds.seconds(2);
        Seconds result = test2.plus(3);
        assertEquals(2, test2.getSeconds());
        assertEquals(5, result.getSeconds());

        assertEquals(1, Seconds.ONE.plus(0).getSeconds());

        Assertions.assertThrows(ArithmeticException.class, () -> {
            Seconds.MAX_VALUE.plus(1);
        });
    }

    @Test
    public void testPlus_Seconds() {
        Seconds test2 = Seconds.seconds(2);
        Seconds test3 = Seconds.seconds(3);
        Seconds result = test2.plus(test3);
        assertEquals(2, test2.getSeconds());
        assertEquals(3, test3.getSeconds());
        assertEquals(5, result.getSeconds());

        assertEquals(1, Seconds.ONE.plus(Seconds.ZERO).getSeconds());
        assertEquals(1, Seconds.ONE.plus((Seconds) null).getSeconds());

        Assertions.assertThrows(ArithmeticException.class, () -> {
            Seconds.MAX_VALUE.plus(Seconds.ONE);
        });
    }

    @Test
    public void testMinus_int() {
        Seconds test2 = Seconds.seconds(2);
        Seconds result = test2.minus(3);
        assertEquals(2, test2.getSeconds());
        assertEquals(-1, result.getSeconds());

        assertEquals(1, Seconds.ONE.minus(0).getSeconds());

        Assertions.assertThrows(ArithmeticException.class, () -> {
            Seconds.MIN_VALUE.minus(1);
        });
    }

    @Test
    public void testMinus_Seconds() {
        Seconds test2 = Seconds.seconds(2);
        Seconds test3 = Seconds.seconds(3);
        Seconds result = test2.minus(test3);
        assertEquals(2, test2.getSeconds());
        assertEquals(3, test3.getSeconds());
        assertEquals(-1, result.getSeconds());

        assertEquals(1, Seconds.ONE.minus(Seconds.ZERO).getSeconds());
        assertEquals(1, Seconds.ONE.minus((Seconds) null).getSeconds());

        Assertions.assertThrows(ArithmeticException.class, () -> {
            Seconds.MIN_VALUE.minus(Seconds.ONE);
        });
    }

    @Test
    public void testMultipliedBy_int() {
        Seconds test = Seconds.seconds(2);
        assertEquals(6, test.multipliedBy(3).getSeconds());
        assertEquals(2, test.getSeconds());
        assertEquals(-6, test.multipliedBy(-3).getSeconds());
        assertSame(test, test.multipliedBy(1));

        Seconds halfMax = Seconds.seconds(Integer.MAX_VALUE / 2 + 1);

        Assertions.assertThrows(ArithmeticException.class, () -> {
            halfMax.multipliedBy(2);
        });
    }

    @Test
    public void testDividedBy_int() {
        Seconds test = Seconds.seconds(12);
        assertEquals(6, test.dividedBy(2).getSeconds());
        assertEquals(12, test.getSeconds());
        assertEquals(4, test.dividedBy(3).getSeconds());
        assertEquals(3, test.dividedBy(4).getSeconds());
        assertEquals(2, test.dividedBy(5).getSeconds());
        assertEquals(2, test.dividedBy(6).getSeconds());
        assertSame(test, test.dividedBy(1));

        Assertions.assertThrows(ArithmeticException.class, () -> {
            Seconds.ONE.dividedBy(0);
        });
    }

    @Test
    public void testNegated() {
        Seconds test = Seconds.seconds(12);
        assertEquals(-12, test.negated().getSeconds());
        assertEquals(12, test.getSeconds());

        Assertions.assertThrows(ArithmeticException.class, () -> {
            Seconds.MIN_VALUE.negated();
        });
    }

    @Test
    public void testAddToLocalDate() {
        Seconds test = Seconds.seconds(26);
        LocalDateTime date = new LocalDateTime(2006, 6, 1, 0, 0, 0, 0);
        LocalDateTime expected = new LocalDateTime(2006, 6, 1, 0, 0, 26, 0);
        assertEquals(expected, date.plus(test));
    }
}