package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * JUnit test suite for the Minutes class.
 * Tests various functionalities such as factory methods, arithmetic operations, and serialization.
 */
public class TestMinutes extends TestCase {

    // Time zone constant for Paris, used in date-time tests
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestMinutes.class);
    }

    public TestMinutes(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        // Setup code if needed
    }

    @Override
    protected void tearDown() throws Exception {
        // Teardown code if needed
    }

    // Test constants defined in the Minutes class
    public void testConstants() {
        assertEquals(0, Minutes.ZERO.getMinutes());
        assertEquals(1, Minutes.ONE.getMinutes());
        assertEquals(2, Minutes.TWO.getMinutes());
        assertEquals(3, Minutes.THREE.getMinutes());
        assertEquals(Integer.MAX_VALUE, Minutes.MAX_VALUE.getMinutes());
        assertEquals(Integer.MIN_VALUE, Minutes.MIN_VALUE.getMinutes());
    }

    // Test factory method for creating Minutes instances from an integer
    public void testFactory_minutes_int() {
        assertSame(Minutes.ZERO, Minutes.minutes(0));
        assertSame(Minutes.ONE, Minutes.minutes(1));
        assertSame(Minutes.TWO, Minutes.minutes(2));
        assertSame(Minutes.THREE, Minutes.minutes(3));
        assertSame(Minutes.MAX_VALUE, Minutes.minutes(Integer.MAX_VALUE));
        assertSame(Minutes.MIN_VALUE, Minutes.minutes(Integer.MIN_VALUE));
        assertEquals(-1, Minutes.minutes(-1).getMinutes());
        assertEquals(4, Minutes.minutes(4).getMinutes());
    }

    // Test factory method for calculating minutes between two instants
    public void testFactory_minutesBetween_RInstant() {
        DateTime start = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);
        DateTime end1 = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS);
        DateTime end2 = new DateTime(2006, 6, 9, 12, 9, 0, 0, PARIS);

        assertEquals(3, Minutes.minutesBetween(start, end1).getMinutes());
        assertEquals(0, Minutes.minutesBetween(start, start).getMinutes());
        assertEquals(0, Minutes.minutesBetween(end1, end1).getMinutes());
        assertEquals(-3, Minutes.minutesBetween(end1, start).getMinutes());
        assertEquals(6, Minutes.minutesBetween(start, end2).getMinutes());
    }

    // Test factory method for calculating minutes between two partial times
    public void testFactory_minutesBetween_RPartial() {
        LocalTime start = new LocalTime(12, 3);
        LocalTime end1 = new LocalTime(12, 6);
        @SuppressWarnings("deprecation")
        TimeOfDay end2 = new TimeOfDay(12, 9);

        assertEquals(3, Minutes.minutesBetween(start, end1).getMinutes());
        assertEquals(0, Minutes.minutesBetween(start, start).getMinutes());
        assertEquals(0, Minutes.minutesBetween(end1, end1).getMinutes());
        assertEquals(-3, Minutes.minutesBetween(end1, start).getMinutes());
        assertEquals(6, Minutes.minutesBetween(start, end2).getMinutes());
    }

    // Test factory method for calculating minutes in an interval
    public void testFactory_minutesIn_RInterval() {
        DateTime start = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);
        DateTime end1 = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS);
        DateTime end2 = new DateTime(2006, 6, 9, 12, 9, 0, 0, PARIS);

        assertEquals(0, Minutes.minutesIn((ReadableInterval) null).getMinutes());
        assertEquals(3, Minutes.minutesIn(new Interval(start, end1)).getMinutes());
        assertEquals(0, Minutes.minutesIn(new Interval(start, start)).getMinutes());
        assertEquals(0, Minutes.minutesIn(new Interval(end1, end1)).getMinutes());
        assertEquals(6, Minutes.minutesIn(new Interval(start, end2)).getMinutes());
    }

    // Test factory method for calculating standard minutes in a period
    public void testFactory_standardMinutesIn_RPeriod() {
        assertEquals(0, Minutes.standardMinutesIn((ReadablePeriod) null).getMinutes());
        assertEquals(0, Minutes.standardMinutesIn(Period.ZERO).getMinutes());
        assertEquals(1, Minutes.standardMinutesIn(new Period(0, 0, 0, 0, 0, 1, 0, 0)).getMinutes());
        assertEquals(123, Minutes.standardMinutesIn(Period.minutes(123)).getMinutes());
        assertEquals(-987, Minutes.standardMinutesIn(Period.minutes(-987)).getMinutes());
        assertEquals(1, Minutes.standardMinutesIn(Period.seconds(119)).getMinutes());
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(120)).getMinutes());
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(121)).getMinutes());
        assertEquals(120, Minutes.standardMinutesIn(Period.hours(2)).getMinutes());

        try {
            Minutes.standardMinutesIn(Period.months(1));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    // Test parsing minutes from a string
    public void testFactory_parseMinutes_String() {
        assertEquals(0, Minutes.parseMinutes((String) null).getMinutes());
        assertEquals(0, Minutes.parseMinutes("PT0M").getMinutes());
        assertEquals(1, Minutes.parseMinutes("PT1M").getMinutes());
        assertEquals(-3, Minutes.parseMinutes("PT-3M").getMinutes());
        assertEquals(2, Minutes.parseMinutes("P0Y0M0DT2M").getMinutes());
        assertEquals(2, Minutes.parseMinutes("PT0H2M").getMinutes());

        try {
            Minutes.parseMinutes("P1Y1D");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }

        try {
            Minutes.parseMinutes("P1DT1M");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    // Test basic getters
    public void testGetMethods() {
        Minutes test = Minutes.minutes(20);
        assertEquals(20, test.getMinutes());
    }

    public void testGetFieldType() {
        Minutes test = Minutes.minutes(20);
        assertEquals(DurationFieldType.minutes(), test.getFieldType());
    }

    public void testGetPeriodType() {
        Minutes test = Minutes.minutes(20);
        assertEquals(PeriodType.minutes(), test.getPeriodType());
    }

    // Test comparison methods
    public void testIsGreaterThan() {
        assertTrue(Minutes.THREE.isGreaterThan(Minutes.TWO));
        assertFalse(Minutes.THREE.isGreaterThan(Minutes.THREE));
        assertFalse(Minutes.TWO.isGreaterThan(Minutes.THREE));
        assertTrue(Minutes.ONE.isGreaterThan(null));
        assertFalse(Minutes.minutes(-1).isGreaterThan(null));
    }

    public void testIsLessThan() {
        assertFalse(Minutes.THREE.isLessThan(Minutes.TWO));
        assertFalse(Minutes.THREE.isLessThan(Minutes.THREE));
        assertTrue(Minutes.TWO.isLessThan(Minutes.THREE));
        assertFalse(Minutes.ONE.isLessThan(null));
        assertTrue(Minutes.minutes(-1).isLessThan(null));
    }

    // Test string representation
    public void testToString() {
        Minutes test = Minutes.minutes(20);
        assertEquals("PT20M", test.toString());

        test = Minutes.minutes(-20);
        assertEquals("PT-20M", test.toString());
    }

    // Test serialization
    public void testSerialization() throws Exception {
        Minutes test = Minutes.THREE;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        oos.close();
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Minutes result = (Minutes) ois.readObject();
        ois.close();

        assertSame(test, result);
    }

    // Test conversion to standard periods
    public void testToStandardWeeks() {
        Minutes test = Minutes.minutes(60 * 24 * 7 * 2);
        Weeks expected = Weeks.weeks(2);
        assertEquals(expected, test.toStandardWeeks());
    }

    public void testToStandardDays() {
        Minutes test = Minutes.minutes(60 * 24 * 2);
        Days expected = Days.days(2);
        assertEquals(expected, test.toStandardDays());
    }

    public void testToStandardHours() {
        Minutes test = Minutes.minutes(3 * 60);
        Hours expected = Hours.hours(3);
        assertEquals(expected, test.toStandardHours());
    }

    public void testToStandardSeconds() {
        Minutes test = Minutes.minutes(3);
        Seconds expected = Seconds.seconds(3 * 60);
        assertEquals(expected, test.toStandardSeconds());

        try {
            Minutes.MAX_VALUE.toStandardSeconds();
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // Expected exception
        }
    }

    public void testToStandardDuration() {
        Minutes test = Minutes.minutes(20);
        Duration expected = new Duration(20L * DateTimeConstants.MILLIS_PER_MINUTE);
        assertEquals(expected, test.toStandardDuration());

        expected = new Duration(((long) Integer.MAX_VALUE) * DateTimeConstants.MILLIS_PER_MINUTE);
        assertEquals(expected, Minutes.MAX_VALUE.toStandardDuration());
    }

    // Test arithmetic operations
    public void testPlus_int() {
        Minutes test2 = Minutes.minutes(2);
        Minutes result = test2.plus(3);
        assertEquals(2, test2.getMinutes());
        assertEquals(5, result.getMinutes());

        assertEquals(1, Minutes.ONE.plus(0).getMinutes());

        try {
            Minutes.MAX_VALUE.plus(1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // Expected exception
        }
    }

    public void testPlus_Minutes() {
        Minutes test2 = Minutes.minutes(2);
        Minutes test3 = Minutes.minutes(3);
        Minutes result = test2.plus(test3);
        assertEquals(2, test2.getMinutes());
        assertEquals(3, test3.getMinutes());
        assertEquals(5, result.getMinutes());

        assertEquals(1, Minutes.ONE.plus(Minutes.ZERO).getMinutes());
        assertEquals(1, Minutes.ONE.plus((Minutes) null).getMinutes());

        try {
            Minutes.MAX_VALUE.plus(Minutes.ONE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // Expected exception
        }
    }

    public void testMinus_int() {
        Minutes test2 = Minutes.minutes(2);
        Minutes result = test2.minus(3);
        assertEquals(2, test2.getMinutes());
        assertEquals(-1, result.getMinutes());

        assertEquals(1, Minutes.ONE.minus(0).getMinutes());

        try {
            Minutes.MIN_VALUE.minus(1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // Expected exception
        }
    }

    public void testMinus_Minutes() {
        Minutes test2 = Minutes.minutes(2);
        Minutes test3 = Minutes.minutes(3);
        Minutes result = test2.minus(test3);
        assertEquals(2, test2.getMinutes());
        assertEquals(3, test3.getMinutes());
        assertEquals(-1, result.getMinutes());

        assertEquals(1, Minutes.ONE.minus(Minutes.ZERO).getMinutes());
        assertEquals(1, Minutes.ONE.minus((Minutes) null).getMinutes());

        try {
            Minutes.MIN_VALUE.minus(Minutes.ONE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // Expected exception
        }
    }

    public void testMultipliedBy_int() {
        Minutes test = Minutes.minutes(2);
        assertEquals(6, test.multipliedBy(3).getMinutes());
        assertEquals(2, test.getMinutes());
        assertEquals(-6, test.multipliedBy(-3).getMinutes());
        assertSame(test, test.multipliedBy(1));

        Minutes halfMax = Minutes.minutes(Integer.MAX_VALUE / 2 + 1);
        try {
            halfMax.multipliedBy(2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // Expected exception
        }
    }

    public void testDividedBy_int() {
        Minutes test = Minutes.minutes(12);
        assertEquals(6, test.dividedBy(2).getMinutes());
        assertEquals(12, test.getMinutes());
        assertEquals(4, test.dividedBy(3).getMinutes());
        assertEquals(3, test.dividedBy(4).getMinutes());
        assertEquals(2, test.dividedBy(5).getMinutes());
        assertEquals(2, test.dividedBy(6).getMinutes());
        assertSame(test, test.dividedBy(1));

        try {
            Minutes.ONE.dividedBy(0);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // Expected exception
        }
    }

    public void testNegated() {
        Minutes test = Minutes.minutes(12);
        assertEquals(-12, test.negated().getMinutes());
        assertEquals(12, test.getMinutes());

        try {
            Minutes.MIN_VALUE.negated();
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // Expected exception
        }
    }

    // Test adding minutes to a LocalDateTime
    public void testAddToLocalDate() {
        Minutes test = Minutes.minutes(26);
        LocalDateTime date = new LocalDateTime(2006, 6, 1, 0, 0, 0, 0);
        LocalDateTime expected = new LocalDateTime(2006, 6, 1, 0, 26, 0, 0);
        assertEquals(expected, date.plus(test));
    }
}