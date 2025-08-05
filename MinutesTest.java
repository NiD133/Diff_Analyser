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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class TestMinutes extends TestCase {
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

    //-----------------------------------------------------------------------
    // Test constants
    //-----------------------------------------------------------------------

    public void testConstants_haveCorrectValues() {
        assertEquals(0, Minutes.ZERO.getMinutes());
        assertEquals(1, Minutes.ONE.getMinutes());
        assertEquals(2, Minutes.TWO.getMinutes());
        assertEquals(3, Minutes.THREE.getMinutes());
        assertEquals(Integer.MAX_VALUE, Minutes.MAX_VALUE.getMinutes());
        assertEquals(Integer.MIN_VALUE, Minutes.MIN_VALUE.getMinutes());
    }

    //-----------------------------------------------------------------------
    // Test factory: minutes(int)
    //-----------------------------------------------------------------------

    public void testFactoryMinutesInt_returnsCachedInstancesForCommonValues() {
        assertSame(Minutes.ZERO, Minutes.minutes(0));
        assertSame(Minutes.ONE, Minutes.minutes(1));
        assertSame(Minutes.TWO, Minutes.minutes(2));
        assertSame(Minutes.THREE, Minutes.minutes(3));
        assertSame(Minutes.MAX_VALUE, Minutes.minutes(Integer.MAX_VALUE));
        assertSame(Minutes.MIN_VALUE, Minutes.minutes(Integer.MIN_VALUE));
    }

    public void testFactoryMinutesInt_createsNewInstanceForUncommonValues() {
        assertEquals(-1, Minutes.minutes(-1).getMinutes());
        assertEquals(4, Minutes.minutes(4).getMinutes());
    }

    //-----------------------------------------------------------------------
    // Test factory: minutesBetween()
    //-----------------------------------------------------------------------

    public void testFactoryMinutesBetweenReadableInstants_calculatesCorrectly() {
        DateTime start = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);
        DateTime end1 = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS);
        DateTime end2 = new DateTime(2006, 6, 9, 12, 9, 0, 0, PARIS);
        
        assertEquals(3, Minutes.minutesBetween(start, end1).getMinutes());
        assertEquals(0, Minutes.minutesBetween(start, start).getMinutes());
        assertEquals(0, Minutes.minutesBetween(end1, end1).getMinutes());
        assertEquals(-3, Minutes.minutesBetween(end1, start).getMinutes());
        assertEquals(6, Minutes.minutesBetween(start, end2).getMinutes());
    }

    public void testFactoryMinutesBetweenReadablePartials_calculatesCorrectly() {
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

    //-----------------------------------------------------------------------
    // Test factory: minutesIn()
    //-----------------------------------------------------------------------

    public void testFactoryMinutesInReadableInterval_handlesNull() {
        assertEquals(0, Minutes.minutesIn((ReadableInterval) null).getMinutes());
    }

    public void testFactoryMinutesInReadableInterval_calculatesCorrectly() {
        DateTime start = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);
        DateTime end1 = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS);
        DateTime end2 = new DateTime(2006, 6, 9, 12, 9, 0, 0, PARIS);
        
        assertEquals(3, Minutes.minutesIn(new Interval(start, end1)).getMinutes());
        assertEquals(0, Minutes.minutesIn(new Interval(start, start)).getMinutes());
        assertEquals(0, Minutes.minutesIn(new Interval(end1, end1)).getMinutes());
        assertEquals(6, Minutes.minutesIn(new Interval(start, end2)).getMinutes());
    }

    //-----------------------------------------------------------------------
    // Test factory: standardMinutesIn()
    //-----------------------------------------------------------------------

    public void testFactoryStandardMinutesInReadablePeriod_handlesNullOrZero() {
        assertEquals(0, Minutes.standardMinutesIn((ReadablePeriod) null).getMinutes());
        assertEquals(0, Minutes.standardMinutesIn(Period.ZERO).getMinutes());
    }

    public void testFactoryStandardMinutesInReadablePeriod_convertsMinutesCorrectly() {
        assertEquals(123, Minutes.standardMinutesIn(Period.minutes(123)).getMinutes());
        assertEquals(-987, Minutes.standardMinutesIn(Period.minutes(-987)).getMinutes());
    }

    public void testFactoryStandardMinutesInReadablePeriod_convertsSecondsCorrectly() {
        assertEquals(1, Minutes.standardMinutesIn(Period.seconds(119)).getMinutes());
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(120)).getMinutes());
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(121)).getMinutes());
    }

    public void testFactoryStandardMinutesInReadablePeriod_convertsHoursCorrectly() {
        assertEquals(120, Minutes.standardMinutesIn(Period.hours(2)).getMinutes());
    }

    public void testFactoryStandardMinutesInReadablePeriod_throwsForUnsupportedUnits() {
        try {
            Minutes.standardMinutesIn(Period.months(1));
            fail("Expected IllegalArgumentException for unsupported unit");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    //-----------------------------------------------------------------------
    // Test factory: parseMinutes()
    //-----------------------------------------------------------------------

    public void testParseMinutes_handlesNullOrEmpty() {
        assertEquals(0, Minutes.parseMinutes((String) null).getMinutes());
        assertEquals(0, Minutes.parseMinutes("PT0M").getMinutes());
    }

    public void testParseMinutes_parsesValidStrings() {
        assertEquals(1, Minutes.parseMinutes("PT1M").getMinutes());
        assertEquals(-3, Minutes.parseMinutes("PT-3M").getMinutes());
        assertEquals(2, Minutes.parseMinutes("P0Y0M0DT2M").getMinutes());
        assertEquals(2, Minutes.parseMinutes("PT0H2M").getMinutes());
    }

    public void testParseMinutes_throwsForInvalidFormats() {
        try {
            Minutes.parseMinutes("P1Y1D");
            fail("Expected IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException ex) {
            // Expected
        }

        try {
            Minutes.parseMinutes("P1DT1M");
            fail("Expected IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    //-----------------------------------------------------------------------
    // Test get* methods
    //-----------------------------------------------------------------------

    public void testGetMinutes_returnsCorrectValue() {
        Minutes test = Minutes.minutes(20);
        assertEquals(20, test.getMinutes());
    }

    public void testGetFieldType_returnsMinutesType() {
        Minutes test = Minutes.minutes(20);
        assertEquals(DurationFieldType.minutes(), test.getFieldType());
    }

    public void testGetPeriodType_returnsMinutesPeriodType() {
        Minutes test = Minutes.minutes(20);
        assertEquals(PeriodType.minutes(), test.getPeriodType());
    }

    //-----------------------------------------------------------------------
    // Test comparison methods
    //-----------------------------------------------------------------------

    public void testIsGreaterThan_comparesCorrectly() {
        assertTrue(Minutes.THREE.isGreaterThan(Minutes.TWO));
        assertFalse(Minutes.THREE.isGreaterThan(Minutes.THREE));
        assertFalse(Minutes.TWO.isGreaterThan(Minutes.THREE));
    }

    public void testIsGreaterThan_handlesNull() {
        assertTrue(Minutes.ONE.isGreaterThan(null));
        assertFalse(Minutes.minutes(-1).isGreaterThan(null));
    }

    public void testIsLessThan_comparesCorrectly() {
        assertFalse(Minutes.THREE.isLessThan(Minutes.TWO));
        assertFalse(Minutes.THREE.isLessThan(Minutes.THREE));
        assertTrue(Minutes.TWO.isLessThan(Minutes.THREE));
    }

    public void testIsLessThan_handlesNull() {
        assertFalse(Minutes.ONE.isLessThan(null));
        assertTrue(Minutes.minutes(-1).isLessThan(null));
    }

    //-----------------------------------------------------------------------
    // Test toString()
    //-----------------------------------------------------------------------

    public void testToString_positiveMinutes() {
        Minutes test = Minutes.minutes(20);
        assertEquals("PT20M", test.toString());
    }

    public void testToString_negativeMinutes() {
        Minutes test = Minutes.minutes(-20);
        assertEquals("PT-20M", test.toString());
    }

    //-----------------------------------------------------------------------
    // Test serialization
    //-----------------------------------------------------------------------

    public void testSerialization_deserializesToSameInstance() throws Exception {
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

    //-----------------------------------------------------------------------
    // Test conversions to other durations
    //-----------------------------------------------------------------------

    public void testToStandardWeeks_convertsCorrectly() {
        Minutes test = Minutes.minutes(60 * 24 * 7 * 2); // 2 weeks in minutes
        Weeks expected = Weeks.weeks(2);
        assertEquals(expected, test.toStandardWeeks());
    }

    public void testToStandardDays_convertsCorrectly() {
        Minutes test = Minutes.minutes(60 * 24 * 2); // 2 days in minutes
        Days expected = Days.days(2);
        assertEquals(expected, test.toStandardDays());
    }

    public void testToStandardHours_convertsCorrectly() {
        Minutes test = Minutes.minutes(3 * 60); // 3 hours in minutes
        Hours expected = Hours.hours(3);
        assertEquals(expected, test.toStandardHours());
    }

    public void testToStandardSeconds_convertsCorrectly() {
        Minutes test = Minutes.minutes(3);
        Seconds expected = Seconds.seconds(3 * 60);
        assertEquals(expected, test.toStandardSeconds());
    }

    public void testToStandardSeconds_throwsForOverflow() {
        try {
            Minutes.MAX_VALUE.toStandardSeconds();
            fail("Expected ArithmeticException for overflow");
        } catch (ArithmeticException ex) {
            // Expected
        }
    }

    public void testToStandardDuration_convertsCorrectly() {
        Minutes test = Minutes.minutes(20);
        Duration expected = new Duration(20L * DateTimeConstants.MILLIS_PER_MINUTE);
        assertEquals(expected, test.toStandardDuration());
    }

    public void testToStandardDuration_maxValue_convertsCorrectly() {
        Duration expected = new Duration(((long) Integer.MAX_VALUE) * DateTimeConstants.MILLIS_PER_MINUTE);
        assertEquals(expected, Minutes.MAX_VALUE.toStandardDuration());
    }

    //-----------------------------------------------------------------------
    // Test arithmetic operations
    //-----------------------------------------------------------------------

    public void testPlus_int_returnsNewInstance() {
        Minutes test = Minutes.minutes(2);
        Minutes result = test.plus(3);
        assertEquals(2, test.getMinutes()); // Original unchanged
        assertEquals(5, result.getMinutes());
    }

    public void testPlus_int_handlesZero() {
        assertSame(Minutes.ONE, Minutes.ONE.plus(0));
    }

    public void testPlus_int_throwsForOverflow() {
        try {
            Minutes.MAX_VALUE.plus(1);
            fail("Expected ArithmeticException for overflow");
        } catch (ArithmeticException ex) {
            // Expected
        }
    }

    public void testPlus_Minutes_returnsNewInstance() {
        Minutes test2 = Minutes.minutes(2);
        Minutes test3 = Minutes.minutes(3);
        Minutes result = test2.plus(test3);
        assertEquals(2, test2.getMinutes()); // Original unchanged
        assertEquals(3, test3.getMinutes()); // Original unchanged
        assertEquals(5, result.getMinutes());
    }

    public void testPlus_Minutes_handlesZeroOrNull() {
        assertSame(Minutes.ONE, Minutes.ONE.plus(Minutes.ZERO));
        assertEquals(1, Minutes.ONE.plus((Minutes) null).getMinutes());
    }

    public void testPlus_Minutes_throwsForOverflow() {
        try {
            Minutes.MAX_VALUE.plus(Minutes.ONE);
            fail("Expected ArithmeticException for overflow");
        } catch (ArithmeticException ex) {
            // Expected
        }
    }

    public void testMinus_int_returnsNewInstance() {
        Minutes test = Minutes.minutes(2);
        Minutes result = test.minus(3);
        assertEquals(2, test.getMinutes()); // Original unchanged
        assertEquals(-1, result.getMinutes());
    }

    public void testMinus_int_handlesZero() {
        assertSame(Minutes.ONE, Minutes.ONE.minus(0));
    }

    public void testMinus_int_throwsForUnderflow() {
        try {
            Minutes.MIN_VALUE.minus(1);
            fail("Expected ArithmeticException for underflow");
        } catch (ArithmeticException ex) {
            // Expected
        }
    }

    public void testMinus_Minutes_returnsNewInstance() {
        Minutes test2 = Minutes.minutes(2);
        Minutes test3 = Minutes.minutes(3);
        Minutes result = test2.minus(test3);
        assertEquals(2, test2.getMinutes()); // Original unchanged
        assertEquals(3, test3.getMinutes()); // Original unchanged
        assertEquals(-1, result.getMinutes());
    }

    public void testMinus_Minutes_handlesZeroOrNull() {
        assertSame(Minutes.ONE, Minutes.ONE.minus(Minutes.ZERO));
        assertEquals(1, Minutes.ONE.minus((Minutes) null).getMinutes());
    }

    public void testMinus_Minutes_throwsForUnderflow() {
        try {
            Minutes.MIN_VALUE.minus(Minutes.ONE);
            fail("Expected ArithmeticException for underflow");
        } catch (ArithmeticException ex) {
            // Expected
        }
    }

    public void testMultipliedBy_int_returnsNewInstance() {
        Minutes test = Minutes.minutes(2);
        assertEquals(6, test.multipliedBy(3).getMinutes());
        assertEquals(2, test.getMinutes()); // Original unchanged
        assertEquals(-6, test.multipliedBy(-3).getMinutes());
    }

    public void testMultipliedBy_int_handlesMultiplyByOne() {
        Minutes test = Minutes.minutes(2);
        assertSame(test, test.multipliedBy(1));
    }

    public void testMultipliedBy_int_throwsForOverflow() {
        Minutes halfMax = Minutes.minutes(Integer.MAX_VALUE / 2 + 1);
        try {
            halfMax.multipliedBy(2);
            fail("Expected ArithmeticException for overflow");
        } catch (ArithmeticException ex) {
            // Expected
        }
    }

    public void testDividedBy_int_returnsNewInstance() {
        Minutes test = Minutes.minutes(12);
        assertEquals(6, test.dividedBy(2).getMinutes());
        assertEquals(12, test.getMinutes()); // Original unchanged
        assertEquals(4, test.dividedBy(3).getMinutes());
        assertEquals(3, test.dividedBy(4).getMinutes());
        assertEquals(2, test.dividedBy(5).getMinutes());
        assertEquals(2, test.dividedBy(6).getMinutes());
    }

    public void testDividedBy_int_handlesDivideByOne() {
        Minutes test = Minutes.minutes(12);
        assertSame(test, test.dividedBy(1));
    }

    public void testDividedBy_int_throwsForDivideByZero() {
        try {
            Minutes.ONE.dividedBy(0);
            fail("Expected ArithmeticException for division by zero");
        } catch (ArithmeticException ex) {
            // Expected
        }
    }

    public void testNegated_returnsNewInstance() {
        Minutes test = Minutes.minutes(12);
        assertEquals(-12, test.negated().getMinutes());
        assertEquals(12, test.getMinutes()); // Original unchanged
    }

    public void testNegated_throwsForMinValue() {
        try {
            Minutes.MIN_VALUE.negated();
            fail("Expected ArithmeticException for negation overflow");
        } catch (ArithmeticException ex) {
            // Expected
        }
    }

    //-----------------------------------------------------------------------
    // Test integration with LocalDateTime
    //-----------------------------------------------------------------------

    public void testAddToLocalDateTime_addsCorrectNumberOfMinutes() {
        Minutes test = Minutes.minutes(26);
        LocalDateTime date = new LocalDateTime(2006, 6, 1, 0, 0, 0, 0);
        LocalDateTime expected = new LocalDateTime(2006, 6, 1, 0, 26, 0, 0);
        assertEquals(expected, date.plus(test));
    }
}