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
 * Test suite for the Minutes class in Joda Time library.
 * Tests all factory methods, arithmetic operations, conversions, and edge cases.
 *
 * @author Stephen Colebourne
 */
public class TestMinutes extends TestCase {
    
    // Using Paris timezone for consistent test results (well-defined DST rules post-2000)
    private static final DateTimeZone PARIS_TIMEZONE = DateTimeZone.forID("Europe/Paris");
    
    // Test data constants
    private static final int SAMPLE_MINUTES = 20;
    private static final int NEGATIVE_MINUTES = -20;

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
        // No setup required
    }

    @Override
    protected void tearDown() throws Exception {
        // No cleanup required
    }

    // ========================================================================
    // CONSTANT TESTS
    // ========================================================================
    
    public void testPredefinedConstants_ShouldHaveCorrectValues() {
        assertEquals("ZERO constant should be 0", 0, Minutes.ZERO.getMinutes());
        assertEquals("ONE constant should be 1", 1, Minutes.ONE.getMinutes());
        assertEquals("TWO constant should be 2", 2, Minutes.TWO.getMinutes());
        assertEquals("THREE constant should be 3", 3, Minutes.THREE.getMinutes());
        assertEquals("MAX_VALUE should be Integer.MAX_VALUE", Integer.MAX_VALUE, Minutes.MAX_VALUE.getMinutes());
        assertEquals("MIN_VALUE should be Integer.MIN_VALUE", Integer.MIN_VALUE, Minutes.MIN_VALUE.getMinutes());
    }

    // ========================================================================
    // FACTORY METHOD TESTS
    // ========================================================================
    
    public void testFactoryMethod_minutes_ShouldReturnCachedInstancesForCommonValues() {
        // Test that factory method returns singleton instances for common values
        assertSame("Should return cached ZERO instance", Minutes.ZERO, Minutes.minutes(0));
        assertSame("Should return cached ONE instance", Minutes.ONE, Minutes.minutes(1));
        assertSame("Should return cached TWO instance", Minutes.TWO, Minutes.minutes(2));
        assertSame("Should return cached THREE instance", Minutes.THREE, Minutes.minutes(3));
        assertSame("Should return cached MAX_VALUE instance", Minutes.MAX_VALUE, Minutes.minutes(Integer.MAX_VALUE));
        assertSame("Should return cached MIN_VALUE instance", Minutes.MIN_VALUE, Minutes.minutes(Integer.MIN_VALUE));
        
        // Test non-cached values
        assertEquals("Should create instance for -1", -1, Minutes.minutes(-1).getMinutes());
        assertEquals("Should create instance for 4", 4, Minutes.minutes(4).getMinutes());
    }

    public void testFactoryMethod_minutesBetween_WithInstants_ShouldCalculateCorrectDifference() {
        DateTime startTime = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS_TIMEZONE);
        DateTime threeMinutesLater = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS_TIMEZONE);
        DateTime sixMinutesLater = new DateTime(2006, 6, 9, 12, 9, 0, 0, PARIS_TIMEZONE);
        
        assertEquals("Should calculate 3 minutes difference", 3, 
            Minutes.minutesBetween(startTime, threeMinutesLater).getMinutes());
        assertEquals("Same time should give 0 minutes", 0, 
            Minutes.minutesBetween(startTime, startTime).getMinutes());
        assertEquals("Same end time should give 0 minutes", 0, 
            Minutes.minutesBetween(threeMinutesLater, threeMinutesLater).getMinutes());
        assertEquals("Reversed order should give negative result", -3, 
            Minutes.minutesBetween(threeMinutesLater, startTime).getMinutes());
        assertEquals("Should calculate 6 minutes difference", 6, 
            Minutes.minutesBetween(startTime, sixMinutesLater).getMinutes());
    }

    public void testFactoryMethod_minutesBetween_WithPartials_ShouldCalculateCorrectDifference() {
        LocalTime startTime = new LocalTime(12, 3);
        LocalTime threeMinutesLater = new LocalTime(12, 6);
        @SuppressWarnings("deprecation")
        TimeOfDay sixMinutesLater = new TimeOfDay(12, 9);
        
        assertEquals("Should calculate 3 minutes between LocalTime objects", 3, 
            Minutes.minutesBetween(startTime, threeMinutesLater).getMinutes());
        assertEquals("Same time should give 0 minutes", 0, 
            Minutes.minutesBetween(startTime, startTime).getMinutes());
        assertEquals("Same end time should give 0 minutes", 0, 
            Minutes.minutesBetween(threeMinutesLater, threeMinutesLater).getMinutes());
        assertEquals("Reversed order should give negative result", -3, 
            Minutes.minutesBetween(threeMinutesLater, startTime).getMinutes());
        assertEquals("Should work with different partial types", 6, 
            Minutes.minutesBetween(startTime, sixMinutesLater).getMinutes());
    }

    public void testFactoryMethod_minutesIn_WithInterval_ShouldCalculateIntervalDuration() {
        DateTime startTime = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS_TIMEZONE);
        DateTime threeMinutesLater = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS_TIMEZONE);
        DateTime sixMinutesLater = new DateTime(2006, 6, 9, 12, 9, 0, 0, PARIS_TIMEZONE);
        
        assertEquals("Null interval should return 0", 0, 
            Minutes.minutesIn((ReadableInterval) null).getMinutes());
        assertEquals("3-minute interval should return 3", 3, 
            Minutes.minutesIn(new Interval(startTime, threeMinutesLater)).getMinutes());
        assertEquals("Zero-length interval should return 0", 0, 
            Minutes.minutesIn(new Interval(startTime, startTime)).getMinutes());
        assertEquals("Zero-length interval at end should return 0", 0, 
            Minutes.minutesIn(new Interval(threeMinutesLater, threeMinutesLater)).getMinutes());
        assertEquals("6-minute interval should return 6", 6, 
            Minutes.minutesIn(new Interval(startTime, sixMinutesLater)).getMinutes());
    }

    public void testFactoryMethod_standardMinutesIn_WithPeriod_ShouldConvertToMinutes() {
        assertEquals("Null period should return 0", 0, 
            Minutes.standardMinutesIn((ReadablePeriod) null).getMinutes());
        assertEquals("Zero period should return 0", 0, 
            Minutes.standardMinutesIn(Period.ZERO).getMinutes());
        assertEquals("1-minute period should return 1", 1, 
            Minutes.standardMinutesIn(new Period(0, 0, 0, 0, 0, 1, 0, 0)).getMinutes());
        assertEquals("123-minute period should return 123", 123, 
            Minutes.standardMinutesIn(Period.minutes(123)).getMinutes());
        assertEquals("Negative minutes should work", -987, 
            Minutes.standardMinutesIn(Period.minutes(-987)).getMinutes());
        
        // Test seconds conversion (119 seconds = 1 minute, 59 seconds)
        assertEquals("119 seconds should convert to 1 minute", 1, 
            Minutes.standardMinutesIn(Period.seconds(119)).getMinutes());
        assertEquals("120 seconds should convert to 2 minutes", 2, 
            Minutes.standardMinutesIn(Period.seconds(120)).getMinutes());
        assertEquals("121 seconds should convert to 2 minutes", 2, 
            Minutes.standardMinutesIn(Period.seconds(121)).getMinutes());
        
        // Test hours conversion (2 hours = 120 minutes)
        assertEquals("2 hours should convert to 120 minutes", 120, 
            Minutes.standardMinutesIn(Period.hours(2)).getMinutes());
        
        // Test that imprecise periods throw exception
        try {
            Minutes.standardMinutesIn(Period.months(1));
            fail("Should throw IllegalArgumentException for imprecise period (months)");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    public void testFactoryMethod_parseMinutes_WithISOString_ShouldParseCorrectly() {
        assertEquals("Null string should return 0", 0, 
            Minutes.parseMinutes((String) null).getMinutes());
        assertEquals("PT0M should parse to 0", 0, 
            Minutes.parseMinutes("PT0M").getMinutes());
        assertEquals("PT1M should parse to 1", 1, 
            Minutes.parseMinutes("PT1M").getMinutes());
        assertEquals("PT-3M should parse to -3", -3, 
            Minutes.parseMinutes("PT-3M").getMinutes());
        assertEquals("Full ISO format should work", 2, 
            Minutes.parseMinutes("P0Y0M0DT2M").getMinutes());
        assertEquals("Hours and minutes format should work", 2, 
            Minutes.parseMinutes("PT0H2M").getMinutes());
        
        // Test invalid formats
        try {
            Minutes.parseMinutes("P1Y1D");
            fail("Should reject period with years and days");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
        
        try {
            Minutes.parseMinutes("P1DT1M");
            fail("Should reject period with days and minutes");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    // ========================================================================
    // GETTER METHOD TESTS
    // ========================================================================
    
    public void testGetMinutes_ShouldReturnStoredValue() {
        Minutes testMinutes = Minutes.minutes(SAMPLE_MINUTES);
        assertEquals("Should return the stored minutes value", SAMPLE_MINUTES, testMinutes.getMinutes());
    }

    public void testGetFieldType_ShouldReturnMinutesFieldType() {
        Minutes testMinutes = Minutes.minutes(SAMPLE_MINUTES);
        assertEquals("Should return minutes field type", DurationFieldType.minutes(), testMinutes.getFieldType());
    }

    public void testGetPeriodType_ShouldReturnMinutesPeriodType() {
        Minutes testMinutes = Minutes.minutes(SAMPLE_MINUTES);
        assertEquals("Should return minutes period type", PeriodType.minutes(), testMinutes.getPeriodType());
    }

    // ========================================================================
    // COMPARISON METHOD TESTS
    // ========================================================================
    
    public void testIsGreaterThan_ShouldCompareCorrectly() {
        assertTrue("THREE should be greater than TWO", Minutes.THREE.isGreaterThan(Minutes.TWO));
        assertFalse("THREE should not be greater than THREE", Minutes.THREE.isGreaterThan(Minutes.THREE));
        assertFalse("TWO should not be greater than THREE", Minutes.TWO.isGreaterThan(Minutes.THREE));
        assertTrue("Positive value should be greater than null", Minutes.ONE.isGreaterThan(null));
        assertFalse("Negative value should not be greater than null", Minutes.minutes(-1).isGreaterThan(null));
    }

    public void testIsLessThan_ShouldCompareCorrectly() {
        assertFalse("THREE should not be less than TWO", Minutes.THREE.isLessThan(Minutes.TWO));
        assertFalse("THREE should not be less than THREE", Minutes.THREE.isLessThan(Minutes.THREE));
        assertTrue("TWO should be less than THREE", Minutes.TWO.isLessThan(Minutes.THREE));
        assertFalse("Positive value should not be less than null", Minutes.ONE.isLessThan(null));
        assertTrue("Negative value should be less than null", Minutes.minutes(-1).isLessThan(null));
    }

    // ========================================================================
    // STRING REPRESENTATION TESTS
    // ========================================================================
    
    public void testToString_ShouldReturnISOFormat() {
        Minutes positiveMinutes = Minutes.minutes(SAMPLE_MINUTES);
        assertEquals("Positive minutes should format correctly", "PT20M", positiveMinutes.toString());
        
        Minutes negativeMinutes = Minutes.minutes(NEGATIVE_MINUTES);
        assertEquals("Negative minutes should format correctly", "PT-20M", negativeMinutes.toString());
    }

    // ========================================================================
    // SERIALIZATION TESTS
    // ========================================================================
    
    public void testSerialization_ShouldPreserveSingletonInstances() throws Exception {
        Minutes originalMinutes = Minutes.THREE;
        
        // Serialize the object
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
        objectOutput.writeObject(originalMinutes);
        objectOutput.close();
        byte[] serializedData = byteOutput.toByteArray();
        
        // Deserialize the object
        ByteArrayInputStream byteInput = new ByteArrayInputStream(serializedData);
        ObjectInputStream objectInput = new ObjectInputStream(byteInput);
        Minutes deserializedMinutes = (Minutes) objectInput.readObject();
        objectInput.close();
        
        assertSame("Deserialized instance should be same singleton", originalMinutes, deserializedMinutes);
    }

    // ========================================================================
    // CONVERSION METHOD TESTS
    // ========================================================================
    
    public void testToStandardWeeks_ShouldConvertCorrectly() {
        int minutesInTwoWeeks = 60 * 24 * 7 * 2; // 2 weeks in minutes
        Minutes testMinutes = Minutes.minutes(minutesInTwoWeeks);
        Weeks expectedWeeks = Weeks.weeks(2);
        assertEquals("Should convert to 2 weeks", expectedWeeks, testMinutes.toStandardWeeks());
    }

    public void testToStandardDays_ShouldConvertCorrectly() {
        int minutesInTwoDays = 60 * 24 * 2; // 2 days in minutes
        Minutes testMinutes = Minutes.minutes(minutesInTwoDays);
        Days expectedDays = Days.days(2);
        assertEquals("Should convert to 2 days", expectedDays, testMinutes.toStandardDays());
    }

    public void testToStandardHours_ShouldConvertCorrectly() {
        int minutesInThreeHours = 3 * 60; // 3 hours in minutes
        Minutes testMinutes = Minutes.minutes(minutesInThreeHours);
        Hours expectedHours = Hours.hours(3);
        assertEquals("Should convert to 3 hours", expectedHours, testMinutes.toStandardHours());
    }

    public void testToStandardSeconds_ShouldConvertCorrectly() {
        Minutes testMinutes = Minutes.minutes(3);
        Seconds expectedSeconds = Seconds.seconds(3 * 60);
        assertEquals("Should convert to 180 seconds", expectedSeconds, testMinutes.toStandardSeconds());
        
        try {
            Minutes.MAX_VALUE.toStandardSeconds();
            fail("Should throw ArithmeticException for overflow");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testToStandardDuration_ShouldConvertCorrectly() {
        Minutes testMinutes = Minutes.minutes(SAMPLE_MINUTES);
        Duration expectedDuration = new Duration(SAMPLE_MINUTES * DateTimeConstants.MILLIS_PER_MINUTE);
        assertEquals("Should convert to correct duration", expectedDuration, testMinutes.toStandardDuration());
        
        Duration maxExpectedDuration = new Duration(((long) Integer.MAX_VALUE) * DateTimeConstants.MILLIS_PER_MINUTE);
        assertEquals("Should handle MAX_VALUE conversion", maxExpectedDuration, Minutes.MAX_VALUE.toStandardDuration());
    }

    // ========================================================================
    // ARITHMETIC OPERATION TESTS
    // ========================================================================
    
    public void testPlus_WithInt_ShouldAddCorrectly() {
        Minutes originalMinutes = Minutes.minutes(2);
        Minutes result = originalMinutes.plus(3);
        
        assertEquals("Original should be unchanged", 2, originalMinutes.getMinutes());
        assertEquals("Result should be sum", 5, result.getMinutes());
        assertEquals("Adding zero should work", 1, Minutes.ONE.plus(0).getMinutes());
        
        try {
            Minutes.MAX_VALUE.plus(1);
            fail("Should throw ArithmeticException for overflow");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testPlus_WithMinutes_ShouldAddCorrectly() {
        Minutes firstMinutes = Minutes.minutes(2);
        Minutes secondMinutes = Minutes.minutes(3);
        Minutes result = firstMinutes.plus(secondMinutes);
        
        assertEquals("First operand should be unchanged", 2, firstMinutes.getMinutes());
        assertEquals("Second operand should be unchanged", 3, secondMinutes.getMinutes());
        assertEquals("Result should be sum", 5, result.getMinutes());
        assertEquals("Adding ZERO should work", 1, Minutes.ONE.plus(Minutes.ZERO).getMinutes());
        assertEquals("Adding null should work like zero", 1, Minutes.ONE.plus((Minutes) null).getMinutes());
        
        try {
            Minutes.MAX_VALUE.plus(Minutes.ONE);
            fail("Should throw ArithmeticException for overflow");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testMinus_WithInt_ShouldSubtractCorrectly() {
        Minutes originalMinutes = Minutes.minutes(2);
        Minutes result = originalMinutes.minus(3);
        
        assertEquals("Original should be unchanged", 2, originalMinutes.getMinutes());
        assertEquals("Result should be difference", -1, result.getMinutes());
        assertEquals("Subtracting zero should work", 1, Minutes.ONE.minus(0).getMinutes());
        
        try {
            Minutes.MIN_VALUE.minus(1);
            fail("Should throw ArithmeticException for underflow");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testMinus_WithMinutes_ShouldSubtractCorrectly() {
        Minutes firstMinutes = Minutes.minutes(2);
        Minutes secondMinutes = Minutes.minutes(3);
        Minutes result = firstMinutes.minus(secondMinutes);
        
        assertEquals("First operand should be unchanged", 2, firstMinutes.getMinutes());
        assertEquals("Second operand should be unchanged", 3, secondMinutes.getMinutes());
        assertEquals("Result should be difference", -1, result.getMinutes());
        assertEquals("Subtracting ZERO should work", 1, Minutes.ONE.minus(Minutes.ZERO).getMinutes());
        assertEquals("Subtracting null should work like zero", 1, Minutes.ONE.minus((Minutes) null).getMinutes());
        
        try {
            Minutes.MIN_VALUE.minus(Minutes.ONE);
            fail("Should throw ArithmeticException for underflow");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testMultipliedBy_ShouldMultiplyCorrectly() {
        Minutes originalMinutes = Minutes.minutes(2);
        
        assertEquals("Should multiply correctly", 6, originalMinutes.multipliedBy(3).getMinutes());
        assertEquals("Original should be unchanged", 2, originalMinutes.getMinutes());
        assertEquals("Should handle negative multiplier", -6, originalMinutes.multipliedBy(-3).getMinutes());
        assertSame("Multiplying by 1 should return same instance", originalMinutes, originalMinutes.multipliedBy(1));
        
        Minutes halfMaxValue = Minutes.minutes(Integer.MAX_VALUE / 2 + 1);
        try {
            halfMaxValue.multipliedBy(2);
            fail("Should throw ArithmeticException for overflow");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testDividedBy_ShouldDivideCorrectly() {
        Minutes originalMinutes = Minutes.minutes(12);
        
        assertEquals("12 / 2 should equal 6", 6, originalMinutes.dividedBy(2).getMinutes());
        assertEquals("Original should be unchanged", 12, originalMinutes.getMinutes());
        assertEquals("12 / 3 should equal 4", 4, originalMinutes.dividedBy(3).getMinutes());
        assertEquals("12 / 4 should equal 3", 3, originalMinutes.dividedBy(4).getMinutes());
        assertEquals("12 / 5 should equal 2 (integer division)", 2, originalMinutes.dividedBy(5).getMinutes());
        assertEquals("12 / 6 should equal 2", 2, originalMinutes.dividedBy(6).getMinutes());
        assertSame("Dividing by 1 should return same instance", originalMinutes, originalMinutes.dividedBy(1));
        
        try {
            Minutes.ONE.dividedBy(0);
            fail("Should throw ArithmeticException for division by zero");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testNegated_ShouldNegateCorrectly() {
        Minutes originalMinutes = Minutes.minutes(12);
        Minutes negatedMinutes = originalMinutes.negated();
        
        assertEquals("Should negate correctly", -12, negatedMinutes.getMinutes());
        assertEquals("Original should be unchanged", 12, originalMinutes.getMinutes());
        
        try {
            Minutes.MIN_VALUE.negated();
            fail("Should throw ArithmeticException when negating MIN_VALUE");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    // ========================================================================
    // INTEGRATION TESTS
    // ========================================================================
    
    public void testAddToLocalDate_ShouldAddMinutesCorrectly() {
        Minutes minutesToAdd = Minutes.minutes(26);
        LocalDateTime originalDateTime = new LocalDateTime(2006, 6, 1, 0, 0, 0, 0);
        LocalDateTime expectedDateTime = new LocalDateTime(2006, 6, 1, 0, 26, 0, 0);
        
        assertEquals("Should add 26 minutes to LocalDateTime", expectedDateTime, originalDateTime.plus(minutesToAdd));
    }
}