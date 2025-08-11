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
 * Readable, focused tests for Years.
 *
 * Notes:
 * - Uses Paris timezone and midday values to avoid DST surprises.
 * - Uses assertThrows for clearer exception expectations.
 * - Keeps tests small and intention-revealing with descriptive names.
 */
public class YearsTest {

    // Use a well-known zone; keep time at midday to avoid DST edges.
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    // Helpers -----------------------------------------------------------------

    private static DateTime dt(int year, int month, int day) {
        return new DateTime(year, month, day, 12, 0, 0, 0, PARIS);
    }

    private static Years serializeAndDeserialize(Years input) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(input);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            return (Years) ois.readObject();
        }
    }

    // Constants & factories ---------------------------------------------------

    @Test
    public void constants_exposeExpectedValues() {
        assertEquals(0, Years.ZERO.getYears());
        assertEquals(1, Years.ONE.getYears());
        assertEquals(2, Years.TWO.getYears());
        assertEquals(3, Years.THREE.getYears());
        assertEquals(Integer.MAX_VALUE, Years.MAX_VALUE.getYears());
        assertEquals(Integer.MIN_VALUE, Years.MIN_VALUE.getYears());
    }

    @Test
    public void years_factory_returnsCachedInstancesForCommonValues() {
        assertSame(Years.ZERO, Years.years(0));
        assertSame(Years.ONE, Years.years(1));
        assertSame(Years.TWO, Years.years(2));
        assertSame(Years.THREE, Years.years(3));
        assertSame(Years.MAX_VALUE, Years.years(Integer.MAX_VALUE));
        assertSame(Years.MIN_VALUE, Years.years(Integer.MIN_VALUE));

        assertEquals(-1, Years.years(-1).getYears());
        assertEquals(4, Years.years(4).getYears());
    }

    // Between/interval factories ---------------------------------------------

    @Test
    public void yearsBetween_instant_handlesDSTAndOrder() {
        DateTime start = dt(2006, 6, 9);
        DateTime end2009 = dt(2009, 6, 9);
        DateTime end2012 = dt(2012, 6, 9);

        assertEquals(3, Years.yearsBetween(start, end2009).getYears());
        assertEquals(0, Years.yearsBetween(start, start).getYears());
        assertEquals(0, Years.yearsBetween(end2009, end2009).getYears());
        assertEquals(-3, Years.yearsBetween(end2009, start).getYears());
        assertEquals(6, Years.yearsBetween(start, end2012).getYears());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void yearsBetween_partial_supportsMatchingFieldSets() {
        LocalDate start = new LocalDate(2006, 6, 9);
        LocalDate end2009 = new LocalDate(2009, 6, 9);
        YearMonthDay end2012 = new YearMonthDay(2012, 6, 9); // same fields as LocalDate

        assertEquals(3, Years.yearsBetween(start, end2009).getYears());
        assertEquals(0, Years.yearsBetween(start, start).getYears());
        assertEquals(0, Years.yearsBetween(end2009, end2009).getYears());
        assertEquals(-3, Years.yearsBetween(end2009, start).getYears());
        assertEquals(6, Years.yearsBetween(start, end2012).getYears());
    }

    @Test
    public void yearsIn_interval_countsWholeYearsOnly() {
        DateTime start = dt(2006, 6, 9);
        DateTime end2009 = dt(2009, 6, 9);
        DateTime end2012 = dt(2012, 6, 9);

        assertEquals(0, Years.yearsIn((ReadableInterval) null).getYears());
        assertEquals(3, Years.yearsIn(new Interval(start, end2009)).getYears());
        assertEquals(0, Years.yearsIn(new Interval(start, start)).getYears());
        assertEquals(0, Years.yearsIn(new Interval(end2009, end2009)).getYears());
        assertEquals(6, Years.yearsIn(new Interval(start, end2012)).getYears());
    }

    // Parsing -----------------------------------------------------------------

    @Test
    public void parseYears_acceptsISO() {
        assertEquals(0, Years.parseYears((String) null).getYears());
        assertEquals(0, Years.parseYears("P0Y").getYears());
        assertEquals(1, Years.parseYears("P1Y").getYears());
        assertEquals(-3, Years.parseYears("P-3Y").getYears());
        assertEquals(2, Years.parseYears("P2Y0M").getYears());
        assertEquals(2, Years.parseYears("P2YT0H0M").getYears());
    }

    @Test
    public void parseYears_rejectsNonYearComponents() {
        assertThrows(IllegalArgumentException.class, () -> Years.parseYears("P1M1D"));
        assertThrows(IllegalArgumentException.class, () -> Years.parseYears("P1YT1H"));
    }

    // Simple getters ----------------------------------------------------------

    @Test
    public void getters_returnExpectedValues() {
        Years y = Years.years(20);
        assertEquals(20, y.getYears());
        assertEquals(DurationFieldType.years(), y.getFieldType());
        assertEquals(PeriodType.years(), y.getPeriodType());
    }

    // Comparisons -------------------------------------------------------------

    @Test
    public void comparisons_isGreaterThan() {
        assertTrue(Years.THREE.isGreaterThan(Years.TWO));
        assertFalse(Years.THREE.isGreaterThan(Years.THREE));
        assertFalse(Years.TWO.isGreaterThan(Years.THREE));
        assertTrue(Years.ONE.isGreaterThan(null));
        assertFalse(Years.years(-1).isGreaterThan(null));
    }

    @Test
    public void comparisons_isLessThan() {
        assertFalse(Years.THREE.isLessThan(Years.TWO));
        assertFalse(Years.THREE.isLessThan(Years.THREE));
        assertTrue(Years.TWO.isLessThan(Years.THREE));
        assertFalse(Years.ONE.isLessThan(null));
        assertTrue(Years.years(-1).isLessThan(null));
    }

    // String form -------------------------------------------------------------

    @Test
    public void toString_outputsISO8601() {
        assertEquals("P20Y", Years.years(20).toString());
        assertEquals("P-20Y", Years.years(-20).toString());
    }

    // Serialization -----------------------------------------------------------

    @Test
    public void serialization_preservesSingletons() throws Exception {
        Years original = Years.THREE;
        Years roundTripped = serializeAndDeserialize(original);
        assertSame(original, roundTripped);
    }

    // Arithmetic: plus/minus --------------------------------------------------

    @Test
    public void plus_withInt_addsYears_andProtectsOverflow() {
        Years two = Years.years(2);
        Years sum = two.plus(3);

        assertEquals(2, two.getYears());
        assertEquals(5, sum.getYears());
        assertEquals(1, Years.ONE.plus(0).getYears());

        assertThrows(ArithmeticException.class, () -> Years.MAX_VALUE.plus(1));
    }

    @Test
    public void plus_withYears_addsYears_andProtectsOverflow() {
        Years two = Years.years(2);
        Years three = Years.years(3);
        Years sum = two.plus(three);

        assertEquals(2, two.getYears());
        assertEquals(3, three.getYears());
        assertEquals(5, sum.getYears());

        assertEquals(1, Years.ONE.plus(Years.ZERO).getYears());
        assertEquals(1, Years.ONE.plus((Years) null).getYears());

        assertThrows(ArithmeticException.class, () -> Years.MAX_VALUE.plus(Years.ONE));
    }

    @Test
    public void minus_withInt_subtractsYears_andProtectsOverflow() {
        Years two = Years.years(2);
        Years diff = two.minus(3);

        assertEquals(2, two.getYears());
        assertEquals(-1, diff.getYears());
        assertEquals(1, Years.ONE.minus(0).getYears());

        assertThrows(ArithmeticException.class, () -> Years.MIN_VALUE.minus(1));
    }

    @Test
    public void minus_withYears_subtractsYears_andProtectsOverflow() {
        Years two = Years.years(2);
        Years three = Years.years(3);
        Years diff = two.minus(three);

        assertEquals(2, two.getYears());
        assertEquals(3, three.getYears());
        assertEquals(-1, diff.getYears());

        assertEquals(1, Years.ONE.minus(Years.ZERO).getYears());
        assertEquals(1, Years.ONE.minus((Years) null).getYears());

        assertThrows(ArithmeticException.class, () -> Years.MIN_VALUE.minus(Years.ONE));
    }

    // Arithmetic: multiplied/divided/negated ---------------------------------

    @Test
    public void multipliedBy_scalesYears_andProtectsOverflow() {
        Years two = Years.years(2);

        assertEquals(6, two.multipliedBy(3).getYears());
        assertEquals(2, two.getYears()); // immutability
        assertEquals(-6, two.multipliedBy(-3).getYears());
        assertSame(two, two.multipliedBy(1)); // identity

        Years halfMaxPlusOne = Years.years(Integer.MAX_VALUE / 2 + 1);
        assertThrows(ArithmeticException.class, () -> halfMaxPlusOne.multipliedBy(2));
    }

    @Test
    public void dividedBy_usesIntegerDivision_andRejectsZeroDivisor() {
        Years twelve = Years.years(12);

        assertEquals(6, twelve.dividedBy(2).getYears());
        assertEquals(12, twelve.getYears()); // immutability
        assertEquals(4, twelve.dividedBy(3).getYears());
        assertEquals(3, twelve.dividedBy(4).getYears());
        assertEquals(2, twelve.dividedBy(5).getYears());
        assertEquals(2, twelve.dividedBy(6).getYears());
        assertSame(twelve, twelve.dividedBy(1)); // identity

        assertThrows(ArithmeticException.class, () -> Years.ONE.dividedBy(0));
    }

    @Test
    public void negated_invertsSign_andProtectsOverflow() {
        Years twelve = Years.years(12);

        assertEquals(-12, twelve.negated().getYears());
        assertEquals(12, twelve.getYears()); // immutability

        assertThrows(ArithmeticException.class, () -> Years.MIN_VALUE.negated());
    }

    // Interaction with LocalDate ---------------------------------------------

    @Test
    public void addToLocalDate_addsWholeYears() {
        Years three = Years.years(3);
        LocalDate date = new LocalDate(2006, 6, 1);
        LocalDate expected = new LocalDate(2009, 6, 1);

        assertEquals(expected, date.plus(three));
    }
}