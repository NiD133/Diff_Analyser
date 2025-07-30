package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * JUnit test suite for the Weeks class.
 */
public class TestWeeks extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestWeeks.class);
    }

    public TestWeeks(String name) {
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

    // Test constants
    public void testWeeksConstants() {
        assertEquals(0, Weeks.ZERO.getWeeks());
        assertEquals(1, Weeks.ONE.getWeeks());
        assertEquals(2, Weeks.TWO.getWeeks());
        assertEquals(3, Weeks.THREE.getWeeks());
        assertEquals(Integer.MAX_VALUE, Weeks.MAX_VALUE.getWeeks());
        assertEquals(Integer.MIN_VALUE, Weeks.MIN_VALUE.getWeeks());
    }

    // Test factory methods
    public void testWeeksFactoryMethod() {
        assertSame(Weeks.ZERO, Weeks.weeks(0));
        assertSame(Weeks.ONE, Weeks.weeks(1));
        assertSame(Weeks.TWO, Weeks.weeks(2));
        assertSame(Weeks.THREE, Weeks.weeks(3));
        assertSame(Weeks.MAX_VALUE, Weeks.weeks(Integer.MAX_VALUE));
        assertSame(Weeks.MIN_VALUE, Weeks.weeks(Integer.MIN_VALUE));
        assertEquals(-1, Weeks.weeks(-1).getWeeks());
        assertEquals(4, Weeks.weeks(4).getWeeks());
    }

    public void testWeeksBetweenInstants() {
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end1 = new DateTime(2006, 6, 30, 12, 0, 0, 0, PARIS);
        DateTime end2 = new DateTime(2006, 7, 21, 12, 0, 0, 0, PARIS);

        assertEquals(3, Weeks.weeksBetween(start, end1).getWeeks());
        assertEquals(0, Weeks.weeksBetween(start, start).getWeeks());
        assertEquals(0, Weeks.weeksBetween(end1, end1).getWeeks());
        assertEquals(-3, Weeks.weeksBetween(end1, start).getWeeks());
        assertEquals(6, Weeks.weeksBetween(start, end2).getWeeks());
    }

    @SuppressWarnings("deprecation")
    public void testWeeksBetweenPartials() {
        LocalDate start = new LocalDate(2006, 6, 9);
        LocalDate end1 = new LocalDate(2006, 6, 30);
        YearMonthDay end2 = new YearMonthDay(2006, 7, 21);

        assertEquals(3, Weeks.weeksBetween(start, end1).getWeeks());
        assertEquals(0, Weeks.weeksBetween(start, start).getWeeks());
        assertEquals(0, Weeks.weeksBetween(end1, end1).getWeeks());
        assertEquals(-3, Weeks.weeksBetween(end1, start).getWeeks());
        assertEquals(6, Weeks.weeksBetween(start, end2).getWeeks());
    }

    public void testWeeksInInterval() {
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end1 = new DateTime(2006, 6, 30, 12, 0, 0, 0, PARIS);
        DateTime end2 = new DateTime(2006, 7, 21, 12, 0, 0, 0, PARIS);

        assertEquals(0, Weeks.weeksIn((ReadableInterval) null).getWeeks());
        assertEquals(3, Weeks.weeksIn(new Interval(start, end1)).getWeeks());
        assertEquals(0, Weeks.weeksIn(new Interval(start, start)).getWeeks());
        assertEquals(0, Weeks.weeksIn(new Interval(end1, end1)).getWeeks());
        assertEquals(6, Weeks.weeksIn(new Interval(start, end2)).getWeeks());
    }

    public void testStandardWeeksInPeriod() {
        assertEquals(0, Weeks.standardWeeksIn((ReadablePeriod) null).getWeeks());
        assertEquals(0, Weeks.standardWeeksIn(Period.ZERO).getWeeks());
        assertEquals(1, Weeks.standardWeeksIn(new Period(0, 0, 1, 0, 0, 0, 0, 0)).getWeeks());
        assertEquals(123, Weeks.standardWeeksIn(Period.weeks(123)).getWeeks());
        assertEquals(-987, Weeks.standardWeeksIn(Period.weeks(-987)).getWeeks());
        assertEquals(1, Weeks.standardWeeksIn(Period.days(13)).getWeeks());
        assertEquals(2, Weeks.standardWeeksIn(Period.days(14)).getWeeks());
        assertEquals(2, Weeks.standardWeeksIn(Period.days(15)).getWeeks());

        try {
            Weeks.standardWeeksIn(Period.months(1));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void testParseWeeksFromString() {
        assertEquals(0, Weeks.parseWeeks((String) null).getWeeks());
        assertEquals(0, Weeks.parseWeeks("P0W").getWeeks());
        assertEquals(1, Weeks.parseWeeks("P1W").getWeeks());
        assertEquals(-3, Weeks.parseWeeks("P-3W").getWeeks());
        assertEquals(2, Weeks.parseWeeks("P0Y0M2W").getWeeks());
        assertEquals(2, Weeks.parseWeeks("P2WT0H0M").getWeeks());

        try {
            Weeks.parseWeeks("P1Y1D");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        try {
            Weeks.parseWeeks("P1WT1H");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    // Test getters
    public void testGetWeeks() {
        Weeks test = Weeks.weeks(20);
        assertEquals(20, test.getWeeks());
    }

    public void testGetFieldType() {
        Weeks test = Weeks.weeks(20);
        assertEquals(DurationFieldType.weeks(), test.getFieldType());
    }

    public void testGetPeriodType() {
        Weeks test = Weeks.weeks(20);
        assertEquals(PeriodType.weeks(), test.getPeriodType());
    }

    // Test comparison methods
    public void testIsGreaterThan() {
        assertTrue(Weeks.THREE.isGreaterThan(Weeks.TWO));
        assertFalse(Weeks.THREE.isGreaterThan(Weeks.THREE));
        assertFalse(Weeks.TWO.isGreaterThan(Weeks.THREE));
        assertTrue(Weeks.ONE.isGreaterThan(null));
        assertFalse(Weeks.weeks(-1).isGreaterThan(null));
    }

    public void testIsLessThan() {
        assertFalse(Weeks.THREE.isLessThan(Weeks.TWO));
        assertFalse(Weeks.THREE.isLessThan(Weeks.THREE));
        assertTrue(Weeks.TWO.isLessThan(Weeks.THREE));
        assertFalse(Weeks.ONE.isLessThan(null));
        assertTrue(Weeks.weeks(-1).isLessThan(null));
    }

    // Test toString method
    public void testToStringMethod() {
        Weeks test = Weeks.weeks(20);
        assertEquals("P20W", test.toString());

        test = Weeks.weeks(-20);
        assertEquals("P-20W", test.toString());
    }

    // Test serialization
    public void testSerialization() throws Exception {
        Weeks test = Weeks.THREE;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        oos.close();
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Weeks result = (Weeks) ois.readObject();
        ois.close();

        assertSame(test, result);
    }

    // Test conversion to standard periods
    public void testToStandardDays() {
        Weeks test = Weeks.weeks(2);
        Days expected = Days.days(14);
        assertEquals(expected, test.toStandardDays());

        try {
            Weeks.MAX_VALUE.toStandardDays();
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    public void testToStandardHours() {
        Weeks test = Weeks.weeks(2);
        Hours expected = Hours.hours(2 * 7 * 24);
        assertEquals(expected, test.toStandardHours());

        try {
            Weeks.MAX_VALUE.toStandardHours();
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    public void testToStandardMinutes() {
        Weeks test = Weeks.weeks(2);
        Minutes expected = Minutes.minutes(2 * 7 * 24 * 60);
        assertEquals(expected, test.toStandardMinutes());

        try {
            Weeks.MAX_VALUE.toStandardMinutes();
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    public void testToStandardSeconds() {
        Weeks test = Weeks.weeks(2);
        Seconds expected = Seconds.seconds(2 * 7 * 24 * 60 * 60);
        assertEquals(expected, test.toStandardSeconds());

        try {
            Weeks.MAX_VALUE.toStandardSeconds();
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    public void testToStandardDuration() {
        Weeks test = Weeks.weeks(20);
        Duration expected = new Duration(20L * DateTimeConstants.MILLIS_PER_WEEK);
        assertEquals(expected, test.toStandardDuration());

        expected = new Duration(((long) Integer.MAX_VALUE) * DateTimeConstants.MILLIS_PER_WEEK);
        assertEquals(expected, Weeks.MAX_VALUE.toStandardDuration());
    }

    // Test arithmetic operations
    public void testPlusInt() {
        Weeks test2 = Weeks.weeks(2);
        Weeks result = test2.plus(3);
        assertEquals(2, test2.getWeeks());
        assertEquals(5, result.getWeeks());

        assertEquals(1, Weeks.ONE.plus(0).getWeeks());

        try {
            Weeks.MAX_VALUE.plus(1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    public void testPlusWeeks() {
        Weeks test2 = Weeks.weeks(2);
        Weeks test3 = Weeks.weeks(3);
        Weeks result = test2.plus(test3);
        assertEquals(2, test2.getWeeks());
        assertEquals(3, test3.getWeeks());
        assertEquals(5, result.getWeeks());

        assertEquals(1, Weeks.ONE.plus(Weeks.ZERO).getWeeks());
        assertEquals(1, Weeks.ONE.plus((Weeks) null).getWeeks());

        try {
            Weeks.MAX_VALUE.plus(Weeks.ONE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    public void testMinusInt() {
        Weeks test2 = Weeks.weeks(2);
        Weeks result = test2.minus(3);
        assertEquals(2, test2.getWeeks());
        assertEquals(-1, result.getWeeks());

        assertEquals(1, Weeks.ONE.minus(0).getWeeks());

        try {
            Weeks.MIN_VALUE.minus(1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    public void testMinusWeeks() {
        Weeks test2 = Weeks.weeks(2);
        Weeks test3 = Weeks.weeks(3);
        Weeks result = test2.minus(test3);
        assertEquals(2, test2.getWeeks());
        assertEquals(3, test3.getWeeks());
        assertEquals(-1, result.getWeeks());

        assertEquals(1, Weeks.ONE.minus(Weeks.ZERO).getWeeks());
        assertEquals(1, Weeks.ONE.minus((Weeks) null).getWeeks());

        try {
            Weeks.MIN_VALUE.minus(Weeks.ONE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    public void testMultipliedByInt() {
        Weeks test = Weeks.weeks(2);
        assertEquals(6, test.multipliedBy(3).getWeeks());
        assertEquals(2, test.getWeeks());
        assertEquals(-6, test.multipliedBy(-3).getWeeks());
        assertSame(test, test.multipliedBy(1));

        Weeks halfMax = Weeks.weeks(Integer.MAX_VALUE / 2 + 1);
        try {
            halfMax.multipliedBy(2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    public void testDividedByInt() {
        Weeks test = Weeks.weeks(12);
        assertEquals(6, test.dividedBy(2).getWeeks());
        assertEquals(12, test.getWeeks());
        assertEquals(4, test.dividedBy(3).getWeeks());
        assertEquals(3, test.dividedBy(4).getWeeks());
        assertEquals(2, test.dividedBy(5).getWeeks());
        assertEquals(2, test.dividedBy(6).getWeeks());
        assertSame(test, test.dividedBy(1));

        try {
            Weeks.ONE.dividedBy(0);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    public void testNegated() {
        Weeks test = Weeks.weeks(12);
        assertEquals(-12, test.negated().getWeeks());
        assertEquals(12, test.getWeeks());

        try {
            Weeks.MIN_VALUE.negated();
            fail("Expected ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    // Test adding weeks to LocalDate
    public void testAddWeeksToLocalDate() {
        Weeks test = Weeks.weeks(3);
        LocalDate date = new LocalDate(2006, 6, 1);
        LocalDate expected = new LocalDate(2006, 6, 22);
        assertEquals(expected, date.plus(test));
    }
}