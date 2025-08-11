package org.joda.time;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.*;

/**
 * A comprehensive test suite for the {@link Years} class.
 * This suite focuses on verifying the correctness of factory methods, arithmetic operations,
 * comparisons, and object behavior like serialization and string representation.
 */
public class YearsTest {

    //-----------------------------------------------------------------------
    // Factory Methods
    //-----------------------------------------------------------------------

    @Test
    public void years_factory_cachesConstants() {
        assertSame(Years.ZERO, Years.years(0));
        assertSame(Years.ONE, Years.years(1));
        assertSame(Years.TWO, Years.years(2));
        assertSame(Years.THREE, Years.years(3));
        assertSame(Years.MAX_VALUE, Years.years(Integer.MAX_VALUE));
        assertSame(Years.MIN_VALUE, Years.years(Integer.MIN_VALUE));
    }

    @Test
    public void years_factory_createsNewInstanceForOtherValues() {
        final int value = 5;
        Years fiveYears = Years.years(value);
        assertEquals(value, fiveYears.getYears());
        // The factory is not required to cache non-constant values
        assertNotSame(Years.years(value), Years.years(value));
    }

    @Test
    public void yearsBetween_twoInstants_calculatesDifference() {
        DateTime start = new DateTime(2000, 6, 1, 0, 0, 0);
        DateTime end = new DateTime(2005, 6, 1, 0, 0, 0);
        assertEquals(Years.years(5), Years.yearsBetween(start, end));
    }

    @Test
    public void yearsBetween_sameInstant_returnsZero() {
        Instant now = Instant.now();
        assertEquals(Years.ZERO, Years.yearsBetween(now, now));
    }

    @Test(expected = IllegalArgumentException.class)
    public void yearsBetween_withNullInstants_throwsException() {
        Years.yearsBetween((ReadableInstant) null, (ReadableInstant) null);
    }

    @Test
    public void yearsBetween_twoPartials_calculatesDifference() {
        ReadablePartial start = new LocalDate(2020, 1, 1);
        ReadablePartial end = new LocalDate(2025, 1, 1);
        assertEquals(Years.years(5), Years.yearsBetween(start, end));
    }

    @Test(expected = IllegalArgumentException.class)
    public void yearsBetween_withNullPartials_throwsException() {
        Years.yearsBetween((ReadablePartial) null, (ReadablePartial) null);
    }

    @Test
    public void yearsIn_interval_calculatesYears() {
        DateTime start = new DateTime(2000, 1, 1, 0, 0);
        DateTime end = new DateTime(2005, 1, 1, 0, 0);
        Interval interval = new Interval(start, end);
        assertEquals(Years.years(5), Years.yearsIn(interval));
    }

    @Test
    public void yearsIn_nullInterval_returnsZero() {
        assertEquals(Years.ZERO, Years.yearsIn(null));
    }

    @Test
    public void parseYears_withValidString_returnsCorrectYears() {
        assertEquals(Years.years(5), Years.parseYears("P5Y"));
        assertEquals(Years.years(-3), Years.parseYears("P-3Y"));
        assertEquals(Years.ZERO, Years.parseYears("P0Y"));
    }

    @Test
    public void parseYears_withNullString_returnsZero() {
        assertEquals(Years.ZERO, Years.parseYears(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseYears_withEmptyString_throwsException() {
        Years.parseYears("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseYears_withNonYearComponent_throwsException() {
        // The parser only allows the years component to be non-zero.
        Years.parseYears("P1M");
    }

    //-----------------------------------------------------------------------
    // Getters
    //-----------------------------------------------------------------------

    @Test
    public void getters_returnCorrectProperties() {
        Years fiveYears = Years.years(5);
        assertEquals(5, fiveYears.getYears());
        assertEquals(DurationFieldType.years(), fiveYears.getFieldType());
        assertEquals(PeriodType.years(), fiveYears.getPeriodType());
    }

    //-----------------------------------------------------------------------
    // Arithmetic Operations
    //-----------------------------------------------------------------------

    @Test
    public void plus_withYears_addsCorrectly() {
        assertEquals(Years.years(5), Years.TWO.plus(Years.THREE));
        assertEquals(Years.TWO, Years.TWO.plus(Years.ZERO));
        assertEquals(Years.TWO, Years.TWO.plus((Years) null)); // null is treated as zero
    }

    @Test
    public void plus_withIntValue_addsCorrectly() {
        assertEquals(Years.years(5), Years.TWO.plus(3));
        assertEquals(Years.years(-1), Years.TWO.plus(-3));
        assertEquals(Years.TWO, Years.TWO.plus(0));
    }

    @Test(expected = ArithmeticException.class)
    public void plus_whenResultOverflows_throwsException() {
        Years.MAX_VALUE.plus(1);
    }

    @Test
    public void minus_withYears_subtractsCorrectly() {
        assertEquals(Years.years(-1), Years.TWO.minus(Years.THREE));
        assertEquals(Years.TWO, Years.TWO.minus(Years.ZERO));
        assertEquals(Years.TWO, Years.TWO.minus((Years) null)); // null is treated as zero
    }

    @Test
    public void minus_withIntValue_subtractsCorrectly() {
        assertEquals(Years.years(-1), Years.TWO.minus(3));
        assertEquals(Years.years(5), Years.TWO.minus(-3));
        assertEquals(Years.TWO, Years.TWO.minus(0));
    }

    @Test(expected = ArithmeticException.class)
    public void minus_whenResultOverflows_throwsException() {
        Years.MIN_VALUE.minus(1);
    }

    @Test(expected = ArithmeticException.class)
    public void minus_whenNegatingMinValue_throwsException() {
        // This is equivalent to MIN_VALUE - MIN_VALUE, which is MIN_VALUE + (-MIN_VALUE).
        // Integer.MIN_VALUE cannot be negated, causing an overflow.
        Years.MIN_VALUE.minus(Years.MIN_VALUE);
    }

    @Test
    public void multipliedBy_scalesValueCorrectly() {
        assertEquals(Years.years(6), Years.TWO.multipliedBy(3));
        assertEquals(Years.years(-6), Years.TWO.multipliedBy(-3));
        assertEquals(Years.ZERO, Years.TWO.multipliedBy(0));
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_whenResultOverflows_throwsException() {
        Years.MAX_VALUE.multipliedBy(2);
    }

    @Test
    public void dividedBy_scalesValueCorrectly() {
        assertEquals(Years.years(3), Years.years(10).dividedBy(3));
        assertEquals(Years.years(-3), Years.years(10).dividedBy(-3));
        assertEquals(Years.years(10), Years.years(10).dividedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_zero_throwsException() {
        Years.ONE.dividedBy(0);
    }

    @Test
    public void negated_reversesSign() {
        assertEquals(Years.years(-2), Years.TWO.negated());
        assertEquals(Years.ZERO, Years.ZERO.negated());
        assertEquals(Years.TWO, Years.years(-2).negated());
    }

    @Test(expected = ArithmeticException.class)
    public void negated_whenValueIsMinValue_throwsException() {
        Years.MIN_VALUE.negated();
    }

    //-----------------------------------------------------------------------
    // Comparison Methods
    //-----------------------------------------------------------------------

    @Test
    public void isGreaterThan_returnsCorrectBoolean() {
        assertTrue(Years.TWO.isGreaterThan(Years.ONE));
        assertFalse(Years.TWO.isGreaterThan(Years.TWO));
        assertFalse(Years.TWO.isGreaterThan(Years.THREE));
    }

    @Test
    public void isGreaterThan_withNull_treatsNullAsZero() {
        assertTrue(Years.ONE.isGreaterThan(null));
        assertFalse(Years.ZERO.isGreaterThan(null));
        assertFalse(Years.years(-1).isGreaterThan(null));
    }

    @Test
    public void isLessThan_returnsCorrectBoolean() {
        assertTrue(Years.TWO.isLessThan(Years.THREE));
        assertFalse(Years.TWO.isLessThan(Years.TWO));
        assertFalse(Years.TWO.isLessThan(Years.ONE));
    }

    @Test
    public void isLessThan_withNull_treatsNullAsZero() {
        assertTrue(Years.years(-1).isLessThan(null));
        assertFalse(Years.ZERO.isLessThan(null));
        assertFalse(Years.ONE.isLessThan(null));
    }

    //-----------------------------------------------------------------------
    // Object Methods (toString, equals, hashCode, serialization)
    //-----------------------------------------------------------------------

    @Test
    public void toString_returnsISO8601Format() {
        assertEquals("P2Y", Years.TWO.toString());
        assertEquals("P0Y", Years.ZERO.toString());
        assertEquals("P-5Y", Years.years(-5).toString());
    }

    @Test
    public void equals_and_hashCode_workCorrectly() {
        Years fiveYears1 = Years.years(5);
        Years fiveYears2 = Years.years(5);
        Years sixYears = Years.years(6);

        // equals
        assertTrue(fiveYears1.equals(fiveYears2));
        assertFalse(fiveYears1.equals(sixYears));
        assertFalse(fiveYears1.equals(null));
        assertFalse(fiveYears1.equals(new Object()));

        // hashCode
        assertEquals(fiveYears1.hashCode(), fiveYears2.hashCode());
    }

    @Test
    public void serialization_preservesSingletonInstances() throws Exception {
        // Serialize and deserialize one of the singleton constants
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(Years.ONE);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Years deserialized = (Years) ois.readObject();
        ois.close();

        // Check that the deserialized object is the same instance
        assertSame(Years.ONE, deserialized);
    }
}