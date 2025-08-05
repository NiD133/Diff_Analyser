/*
 *  Copyright 2001-2013 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link Years} class.
 */
public class TestYears {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTime DATE_2006 = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
    private static final DateTime DATE_2009 = new DateTime(2009, 6, 9, 12, 0, 0, 0, PARIS);
    private static final DateTime DATE_2012 = new DateTime(2012, 6, 9, 12, 0, 0, 0, PARIS);
    private static final LocalDate LOCAL_DATE_2006 = new LocalDate(2006, 6, 9);
    private static final LocalDate LOCAL_DATE_2009 = new LocalDate(2009, 6, 9);
    private static final LocalDate LOCAL_DATE_2012 = new LocalDate(2012, 6, 9);

    //-----------------------------------------------------------------------
    // Test constants
    //-----------------------------------------------------------------------
    @Test
    public void testConstantValues() {
        assertEquals("ZERO", 0, Years.ZERO.getYears());
        assertEquals("ONE", 1, Years.ONE.getYears());
        assertEquals("TWO", 2, Years.TWO.getYears());
        assertEquals("THREE", 3, Years.THREE.getYears());
        assertEquals("MAX_VALUE", Integer.MAX_VALUE, Years.MAX_VALUE.getYears());
        assertEquals("MIN_VALUE", Integer.MIN_VALUE, Years.MIN_VALUE.getYears());
    }

    //-----------------------------------------------------------------------
    // Factory method: years(int)
    //-----------------------------------------------------------------------
    @Test
    public void testFactory_yearsInt() {
        assertSame("Zero should be constant", Years.ZERO, Years.years(0));
        assertSame("One should be constant", Years.ONE, Years.years(1));
        assertSame("Two should be constant", Years.TWO, Years.years(2));
        assertSame("Three should be constant", Years.THREE, Years.years(3));
        assertSame("Max should be constant", Years.MAX_VALUE, Years.years(Integer.MAX_VALUE));
        assertSame("Min should be constant", Years.MIN_VALUE, Years.years(Integer.MIN_VALUE));
        assertEquals("Negative value", -1, Years.years(-1).getYears());
        assertEquals("Arbitrary value", 4, Years.years(4).getYears());
    }

    //-----------------------------------------------------------------------
    // Factory method: yearsBetween()
    //-----------------------------------------------------------------------
    @Test
    public void testFactory_yearsBetweenReadableInstants() {
        assertEquals("Same start and end", 0, Years.yearsBetween(DATE_2006, DATE_2006).getYears());
        assertEquals("Regular gap", 3, Years.yearsBetween(DATE_2006, DATE_2009).getYears());
        assertEquals("Reverse order", -3, Years.yearsBetween(DATE_2009, DATE_2006).getYears());
        assertEquals("Longer gap", 6, Years.yearsBetween(DATE_2006, DATE_2012).getYears());
    }

    @Test
    public void testFactory_yearsBetweenReadablePartials() {
        assertEquals("Same start and end", 0, Years.yearsBetween(LOCAL_DATE_2006, LOCAL_DATE_2006).getYears());
        assertEquals("Regular gap", 3, Years.yearsBetween(LOCAL_DATE_2006, LOCAL_DATE_2009).getYears());
        assertEquals("Reverse order", -3, Years.yearsBetween(LOCAL_DATE_2009, LOCAL_DATE_2006).getYears());
        assertEquals("Longer gap", 6, Years.yearsBetween(LOCAL_DATE_2006, LOCAL_DATE_2012).getYears());
    }

    @Test
    public void testFactory_yearsInReadableInterval() {
        assertEquals("Null interval", 0, Years.yearsIn((ReadableInterval) null).getYears());
        assertEquals("Zero length", 0, Years.yearsIn(new Interval(DATE_2006, DATE_2006)).getYears());
        assertEquals("Regular interval", 3, Years.yearsIn(new Interval(DATE_2006, DATE_2009)).getYears());
        assertEquals("Longer interval", 6, Years.yearsIn(new Interval(DATE_2006, DATE_2012)).getYears());
    }

    //-----------------------------------------------------------------------
    // Factory method: parseYears()
    //-----------------------------------------------------------------------
    @Test
    public void testFactory_parseYears() {
        assertEquals("Null input", 0, Years.parseYears(null).getYears());
        assertEquals("Zero years", 0, Years.parseYears("P0Y").getYears());
        assertEquals("Positive years", 1, Years.parseYears("P1Y").getYears());
        assertEquals("Negative years", -3, Years.parseYears("P-3Y").getYears());
        assertEquals("With months", 2, Years.parseYears("P2Y0M").getYears());
        assertEquals("With time", 2, Years.parseYears("P2YT0H0M").getYears());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactory_parseYears_invalidMonths() {
        Years.parseYears("P1M1D");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactory_parseYears_invalidTime() {
        Years.parseYears("P1YT1H");
    }

    //-----------------------------------------------------------------------
    // Accessors
    //-----------------------------------------------------------------------
    @Test
    public void testGetYears() {
        Years test = Years.years(20);
        assertEquals(20, test.getYears());
    }

    @Test
    public void testGetFieldType() {
        Years test = Years.years(20);
        assertSame(DurationFieldType.years(), test.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        Years test = Years.years(20);
        assertSame(PeriodType.years(), test.getPeriodType());
    }

    //-----------------------------------------------------------------------
    // Comparisons
    //-----------------------------------------------------------------------
    @Test
    public void testIsGreaterThan() {
        assertThat("3 > 2", Years.THREE.isGreaterThan(Years.TWO), is(true));
        assertThat("3 > 3", Years.THREE.isGreaterThan(Years.THREE), is(false));
        assertThat("2 > 3", Years.TWO.isGreaterThan(Years.THREE), is(false));
        assertThat("1 > null", Years.ONE.isGreaterThan(null), is(true));
        assertThat("-1 > null", Years.years(-1).isGreaterThan(null), is(false));
    }

    @Test
    public void testIsLessThan() {
        assertThat("3 < 2", Years.THREE.isLessThan(Years.TWO), is(false));
        assertThat("3 < 3", Years.THREE.isLessThan(Years.THREE), is(false));
        assertThat("2 < 3", Years.TWO.isLessThan(Years.THREE), is(true));
        assertThat("1 < null", Years.ONE.isLessThan(null), is(false));
        assertThat("-1 < null", Years.years(-1).isLessThan(null), is(true));
    }

    //-----------------------------------------------------------------------
    // Serialization
    //-----------------------------------------------------------------------
    @Test
    public void testSerialization() throws Exception {
        Years original = Years.THREE;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();
        
        ObjectInputStream ois = new ObjectInputStream(
            new ByteArrayInputStream(baos.toByteArray()));
        Years deserialized = (Years) ois.readObject();
        ois.close();
        
        assertSame("Deserialized instance should be same as constant", original, deserialized);
    }

    //-----------------------------------------------------------------------
    // Arithmetic operations
    //-----------------------------------------------------------------------
    @Test
    public void testPlus_int() {
        Years twoYears = Years.years(2);
        Years result = twoYears.plus(3);
        assertEquals("Original should remain unchanged", 2, twoYears.getYears());
        assertEquals("2 + 3 = 5", 5, result.getYears());
        assertEquals("Plus zero should return same", Years.ONE, Years.ONE.plus(0));
    }

    @Test(expected = ArithmeticException.class)
    public void testPlus_int_overflow() {
        Years.MAX_VALUE.plus(1);
    }

    @Test
    public void testPlus_Years() {
        Years twoYears = Years.years(2);
        Years threeYears = Years.years(3);
        Years result = twoYears.plus(threeYears);
        assertEquals("2 + 3 = 5", 5, result.getYears());
        assertEquals("Plus zero should return same", Years.ONE, Years.ONE.plus(Years.ZERO));
        assertEquals("Plus null should be same as plus zero", Years.ONE, Years.ONE.plus(null));
    }

    @Test(expected = ArithmeticException.class)
    public void testPlus_Years_overflow() {
        Years.MAX_VALUE.plus(Years.ONE);
    }

    @Test
    public void testMinus_int() {
        Years twoYears = Years.years(2);
        Years result = twoYears.minus(3);
        assertEquals("Original should remain unchanged", 2, twoYears.getYears());
        assertEquals("2 - 3 = -1", -1, result.getYears());
        assertEquals("Minus zero should return same", Years.ONE, Years.ONE.minus(0));
    }

    @Test(expected = ArithmeticException.class)
    public void testMinus_int_underflow() {
        Years.MIN_VALUE.minus(1);
    }

    @Test
    public void testMinus_Years() {
        Years twoYears = Years.years(2);
        Years threeYears = Years.years(3);
        Years result = twoYears.minus(threeYears);
        assertEquals("2 - 3 = -1", -1, result.getYears());
        assertEquals("Minus zero should return same", Years.ONE, Years.ONE.minus(Years.ZERO));
        assertEquals("Minus null should be same as minus zero", Years.ONE, Years.ONE.minus(null));
    }

    @Test(expected = ArithmeticException.class)
    public void testMinus_Years_underflow() {
        Years.MIN_VALUE.minus(Years.ONE);
    }

    @Test
    public void testMultipliedBy_int() {
        Years twoYears = Years.years(2);
        Years result = twoYears.multipliedBy(3);
        assertEquals("2 * 3 = 6", 6, result.getYears());
        assertEquals("Original should remain unchanged", 2, twoYears.getYears());
        assertEquals("Negative multiplication", -6, twoYears.multipliedBy(-3).getYears());
        assertSame("Multiply by 1 should return same", twoYears, twoYears.multipliedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void testMultipliedBy_overflow() {
        Years.halfMax.multipliedBy(2);
    }

    @Test
    public void testDividedBy_int() {
        Years twelveYears = Years.years(12);
        assertEquals("12 / 2 = 6", 6, twelveYears.dividedBy(2).getYears());
        assertEquals("Original should remain unchanged", 12, twelveYears.getYears());
        assertEquals("12 / 3 = 4", 4, twelveYears.dividedBy(3).getYears());
        assertEquals("12 / 4 = 3", 3, twelveYears.dividedBy(4).getYears());
        assertEquals("12 / 5 = 2", 2, twelveYears.dividedBy(5).getYears());
        assertEquals("12 / 6 = 2", 2, twelveYears.dividedBy(6).getYears());
        assertSame("Divide by 1 should return same", twelveYears, twelveYears.dividedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void testDividedBy_zero() {
        Years.ONE.dividedBy(0);
    }

    @Test
    public void testNegated() {
        Years twelveYears = Years.years(12);
        assertEquals("Negated 12", -12, twelveYears.negated().getYears());
        assertEquals("Original should remain unchanged", 12, twelveYears.getYears());
    }

    @Test(expected = ArithmeticException.class)
    public void testNegated_underflow() {
        Years.MIN_VALUE.negated();
    }

    //-----------------------------------------------------------------------
    // Integration with LocalDate
    //-----------------------------------------------------------------------
    @Test
    public void testAddToLocalDate() {
        Years threeYears = Years.years(3);
        LocalDate result = new LocalDate(2006, 6, 1).plus(threeYears);
        assertEquals("2006-06-01 plus 3 years", new LocalDate(2009, 6, 1), result);
    }

    // Helper for overflow tests
    private static final Years halfMax = Years.years(Integer.MAX_VALUE / 2 + 1);
}