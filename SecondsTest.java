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
 * Unit tests for the Seconds class in Joda Time library.
 * Tests all factory methods, arithmetic operations, conversions, and utility methods.
 *
 * @author Stephen Colebourne
 */
public class TestSeconds extends TestCase {
    
    // Using a stable timezone for consistent test results across different environments
    private static final DateTimeZone PARIS_TIMEZONE = DateTimeZone.forID("Europe/Paris");
    
    // Test data constants for better readability
    private static final int TWENTY_SECONDS = 20;
    private static final int NEGATIVE_TWENTY_SECONDS = -20;
    private static final int SECONDS_IN_TWO_WEEKS = 60 * 60 * 24 * 7 * 2; // 1,209,600 seconds
    private static final int SECONDS_IN_TWO_DAYS = 60 * 60 * 24 * 2; // 172,800 seconds
    private static final int SECONDS_IN_TWO_HOURS = 60 * 60 * 2; // 7,200 seconds
    private static final int SECONDS_IN_TWO_MINUTES = 60 * 2; // 120 seconds

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestSeconds.class);
    }

    public TestSeconds(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        // No setup required for these tests
    }

    @Override
    protected void tearDown() throws Exception {
        // No cleanup required for these tests
    }

    // ========================================================================
    // Tests for predefined constants
    // ========================================================================
    
    public void testPredefinedConstants_ShouldReturnCorrectValues() {
        assertEquals("ZERO constant should be 0", 0, Seconds.ZERO.getSeconds());
        assertEquals("ONE constant should be 1", 1, Seconds.ONE.getSeconds());
        assertEquals("TWO constant should be 2", 2, Seconds.TWO.getSeconds());
        assertEquals("THREE constant should be 3", 3, Seconds.THREE.getSeconds());
        assertEquals("MAX_VALUE should be Integer.MAX_VALUE", Integer.MAX_VALUE, Seconds.MAX_VALUE.getSeconds());
        assertEquals("MIN_VALUE should be Integer.MIN_VALUE", Integer.MIN_VALUE, Seconds.MIN_VALUE.getSeconds());
    }

    // ========================================================================
    // Tests for factory methods
    // ========================================================================
    
    public void testFactoryMethod_seconds_ShouldReturnCachedInstancesForCommonValues() {
        // Test that common values return cached instances (same object reference)
        assertSame("Should return cached ZERO instance", Seconds.ZERO, Seconds.seconds(0));
        assertSame("Should return cached ONE instance", Seconds.ONE, Seconds.seconds(1));
        assertSame("Should return cached TWO instance", Seconds.TWO, Seconds.seconds(2));
        assertSame("Should return cached THREE instance", Seconds.THREE, Seconds.seconds(3));
        assertSame("Should return cached MAX_VALUE instance", Seconds.MAX_VALUE, Seconds.seconds(Integer.MAX_VALUE));
        assertSame("Should return cached MIN_VALUE instance", Seconds.MIN_VALUE, Seconds.seconds(Integer.MIN_VALUE));
        
        // Test that non-cached values return correct instances
        assertEquals("Should create instance with -1 seconds", -1, Seconds.seconds(-1).getSeconds());
        assertEquals("Should create instance with 4 seconds", 4, Seconds.seconds(4).getSeconds());
    }

    public void testFactoryMethod_secondsBetween_WithInstants_ShouldCalculateCorrectDifference() {
        DateTime baseTime = new DateTime(2006, 6, 9, 12, 0, 3, 0, PARIS_TIMEZONE);
        DateTime threeSecondsLater = new DateTime(2006, 6, 9, 12, 0, 6, 0, PARIS_TIMEZONE);
        DateTime sixSecondsLater = new DateTime(2006, 6, 9, 12, 0, 9, 0, PARIS_TIMEZONE);
        
        assertEquals("Should calculate 3 seconds between base and +3s", 
                     3, Seconds.secondsBetween(baseTime, threeSecondsLater).getSeconds());
        assertEquals("Should return 0 for same instant", 
                     0, Seconds.secondsBetween(baseTime, baseTime).getSeconds());
        assertEquals("Should return 0 for same instant (different reference)", 
                     0, Seconds.secondsBetween(threeSecondsLater, threeSecondsLater).getSeconds());
        assertEquals("Should return negative value for reverse order", 
                     -3, Seconds.secondsBetween(threeSecondsLater, baseTime).getSeconds());
        assertEquals("Should calculate 6 seconds between base and +6s", 
                     6, Seconds.secondsBetween(baseTime, sixSecondsLater).getSeconds());
    }

    public void testFactoryMethod_secondsBetween_WithPartials_ShouldCalculateCorrectDifference() {
        LocalTime startTime = new LocalTime(12, 0, 3);
        LocalTime threeSecondsLater = new LocalTime(12, 0, 6);
        @SuppressWarnings("deprecation")
        TimeOfDay sixSecondsLater = new TimeOfDay(12, 0, 9);
        
        assertEquals("Should calculate 3 seconds between LocalTime instances", 
                     3, Seconds.secondsBetween(startTime, threeSecondsLater).getSeconds());
        assertEquals("Should return 0 for same time", 
                     0, Seconds.secondsBetween(startTime, startTime).getSeconds());
        assertEquals("Should return 0 for same time (different reference)", 
                     0, Seconds.secondsBetween(threeSecondsLater, threeSecondsLater).getSeconds());
        assertEquals("Should return negative value for reverse order", 
                     -3, Seconds.secondsBetween(threeSecondsLater, startTime).getSeconds());
        assertEquals("Should calculate 6 seconds between different partial types", 
                     6, Seconds.secondsBetween(startTime, sixSecondsLater).getSeconds());
    }

    public void testFactoryMethod_secondsIn_WithInterval_ShouldCalculateIntervalDuration() {
        DateTime startTime = new DateTime(2006, 6, 9, 12, 0, 3, 0, PARIS_TIMEZONE);
        DateTime threeSecondsLater = new DateTime(2006, 6, 9, 12, 0, 6, 0, PARIS_TIMEZONE);
        DateTime sixSecondsLater = new DateTime(2006, 6, 9, 12, 0, 9, 0, PARIS_TIMEZONE);
        
        assertEquals("Should return 0 for null interval", 
                     0, Seconds.secondsIn((ReadableInterval) null).getSeconds());
        assertEquals("Should calculate 3 seconds for 3-second interval", 
                     3, Seconds.secondsIn(new Interval(startTime, threeSecondsLater)).getSeconds());
        assertEquals("Should return 0 for zero-length interval", 
                     0, Seconds.secondsIn(new Interval(startTime, startTime)).getSeconds());
        assertEquals("Should return 0 for zero-length interval (different reference)", 
                     0, Seconds.secondsIn(new Interval(threeSecondsLater, threeSecondsLater)).getSeconds());
        assertEquals("Should calculate 6 seconds for 6-second interval", 
                     6, Seconds.secondsIn(new Interval(startTime, sixSecondsLater)).getSeconds());
    }

    public void testFactoryMethod_standardSecondsIn_WithValidPeriods_ShouldConvertCorrectly() {
        assertEquals("Should return 0 for null period", 
                     0, Seconds.standardSecondsIn((ReadablePeriod) null).getSeconds());
        assertEquals("Should return 0 for zero period", 
                     0, Seconds.standardSecondsIn(Period.ZERO).getSeconds());
        assertEquals("Should convert 1-second period", 
                     1, Seconds.standardSecondsIn(new Period(0, 0, 0, 0, 0, 0, 1, 0)).getSeconds());
        assertEquals("Should convert 123-second period", 
                     123, Seconds.standardSecondsIn(Period.seconds(123)).getSeconds());
        assertEquals("Should convert negative seconds period", 
                     -987, Seconds.standardSecondsIn(Period.seconds(-987)).getSeconds());
        assertEquals("Should convert 2 days to seconds", 
                     SECONDS_IN_TWO_DAYS, Seconds.standardSecondsIn(Period.days(2)).getSeconds());
    }
    
    public void testFactoryMethod_standardSecondsIn_WithInvalidPeriods_ShouldThrowException() {
        try {
            Seconds.standardSecondsIn(Period.months(1));
            fail("Should throw IllegalArgumentException for period with months");
        } catch (IllegalArgumentException expected) {
            // Expected behavior - months cannot be converted to exact seconds
        }
    }

    public void testFactoryMethod_parseSeconds_WithValidStrings_ShouldParseCorrectly() {
        assertEquals("Should return 0 for null string", 
                     0, Seconds.parseSeconds((String) null).getSeconds());
        assertEquals("Should parse PT0S as 0 seconds", 
                     0, Seconds.parseSeconds("PT0S").getSeconds());
        assertEquals("Should parse PT1S as 1 second", 
                     1, Seconds.parseSeconds("PT1S").getSeconds());
        assertEquals("Should parse PT-3S as -3 seconds", 
                     -3, Seconds.parseSeconds("PT-3S").getSeconds());
        assertEquals("Should parse full ISO format with only seconds", 
                     2, Seconds.parseSeconds("P0Y0M0DT2S").getSeconds());
        assertEquals("Should parse format with hours and seconds", 
                     2, Seconds.parseSeconds("PT0H2S").getSeconds());
    }
    
    public void testFactoryMethod_parseSeconds_WithInvalidStrings_ShouldThrowException() {
        try {
            Seconds.parseSeconds("P1Y1D");
            fail("Should throw IllegalArgumentException for period with years and days");
        } catch (IllegalArgumentException expected) {
            // Expected behavior - only seconds component should be non-zero
        }
        
        try {
            Seconds.parseSeconds("P1DT1S");
            fail("Should throw IllegalArgumentException for period with days and seconds");
        } catch (IllegalArgumentException expected) {
            // Expected behavior - only seconds component should be non-zero
        }
    }

    // ========================================================================
    // Tests for getter methods
    // ========================================================================
    
    public void testGetMethods_ShouldReturnCorrectValues() {
        Seconds twentySeconds = Seconds.seconds(TWENTY_SECONDS);
        
        assertEquals("getSeconds() should return the stored value", 
                     TWENTY_SECONDS, twentySeconds.getSeconds());
        assertEquals("getFieldType() should return seconds field type", 
                     DurationFieldType.seconds(), twentySeconds.getFieldType());
        assertEquals("getPeriodType() should return seconds period type", 
                     PeriodType.seconds(), twentySeconds.getPeriodType());
    }

    // ========================================================================
    // Tests for comparison methods
    // ========================================================================
    
    public void testComparison_isGreaterThan_ShouldCompareCorrectly() {
        assertTrue("THREE should be greater than TWO", 
                   Seconds.THREE.isGreaterThan(Seconds.TWO));
        assertFalse("THREE should not be greater than THREE", 
                    Seconds.THREE.isGreaterThan(Seconds.THREE));
        assertFalse("TWO should not be greater than THREE", 
                    Seconds.TWO.isGreaterThan(Seconds.THREE));
        assertTrue("Positive value should be greater than null", 
                   Seconds.ONE.isGreaterThan(null));
        assertFalse("Negative value should not be greater than null", 
                    Seconds.seconds(-1).isGreaterThan(null));
    }

    public void testComparison_isLessThan_ShouldCompareCorrectly() {
        assertFalse("THREE should not be less than TWO", 
                    Seconds.THREE.isLessThan(Seconds.TWO));
        assertFalse("THREE should not be less than THREE", 
                    Seconds.THREE.isLessThan(Seconds.THREE));
        assertTrue("TWO should be less than THREE", 
                   Seconds.TWO.isLessThan(Seconds.THREE));
        assertFalse("Positive value should not be less than null", 
                    Seconds.ONE.isLessThan(null));
        assertTrue("Negative value should be less than null", 
                   Seconds.seconds(-1).isLessThan(null));
    }

    // ========================================================================
    // Tests for string representation
    // ========================================================================
    
    public void testToString_ShouldReturnISO8601Format() {
        Seconds positiveSeconds = Seconds.seconds(TWENTY_SECONDS);
        assertEquals("Positive seconds should format correctly", 
                     "PT20S", positiveSeconds.toString());
        
        Seconds negativeSeconds = Seconds.seconds(NEGATIVE_TWENTY_SECONDS);
        assertEquals("Negative seconds should format correctly", 
                     "PT-20S", negativeSeconds.toString());
    }

    // ========================================================================
    // Tests for serialization
    // ========================================================================
    
    public void testSerialization_ShouldPreserveSingletonInstances() throws Exception {
        Seconds originalSeconds = Seconds.THREE;
        
        // Serialize the object
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
        objectOutput.writeObject(originalSeconds);
        objectOutput.close();
        byte[] serializedData = byteOutput.toByteArray();
        
        // Deserialize the object
        ByteArrayInputStream byteInput = new ByteArrayInputStream(serializedData);
        ObjectInputStream objectInput = new ObjectInputStream(byteInput);
        Seconds deserializedSeconds = (Seconds) objectInput.readObject();
        objectInput.close();
        
        assertSame("Deserialized instance should be the same singleton", 
                   originalSeconds, deserializedSeconds);
    }

    // ========================================================================
    // Tests for conversion methods
    // ========================================================================
    
    public void testConversion_toStandardWeeks_ShouldCalculateCorrectly() {
        Seconds twoWeeksInSeconds = Seconds.seconds(SECONDS_IN_TWO_WEEKS);
        Weeks expectedWeeks = Weeks.weeks(2);
        assertEquals("Should convert seconds to weeks correctly", 
                     expectedWeeks, twoWeeksInSeconds.toStandardWeeks());
    }

    public void testConversion_toStandardDays_ShouldCalculateCorrectly() {
        Seconds twoDaysInSeconds = Seconds.seconds(SECONDS_IN_TWO_DAYS);
        Days expectedDays = Days.days(2);
        assertEquals("Should convert seconds to days correctly", 
                     expectedDays, twoDaysInSeconds.toStandardDays());
    }

    public void testConversion_toStandardHours_ShouldCalculateCorrectly() {
        Seconds twoHoursInSeconds = Seconds.seconds(SECONDS_IN_TWO_HOURS);
        Hours expectedHours = Hours.hours(2);
        assertEquals("Should convert seconds to hours correctly", 
                     expectedHours, twoHoursInSeconds.toStandardHours());
    }

    public void testConversion_toStandardMinutes_ShouldCalculateCorrectly() {
        Seconds twoMinutesInSeconds = Seconds.seconds(SECONDS_IN_TWO_MINUTES);
        Minutes expectedMinutes = Minutes.minutes(2);
        assertEquals("Should convert seconds to minutes correctly", 
                     expectedMinutes, twoMinutesInSeconds.toStandardMinutes());
    }

    public void testConversion_toStandardDuration_ShouldCalculateCorrectly() {
        Seconds twentySeconds = Seconds.seconds(TWENTY_SECONDS);
        Duration expectedDuration = new Duration(TWENTY_SECONDS * DateTimeConstants.MILLIS_PER_SECOND);
        assertEquals("Should convert seconds to duration correctly", 
                     expectedDuration, twentySeconds.toStandardDuration());
        
        Duration maxValueDuration = new Duration(((long) Integer.MAX_VALUE) * DateTimeConstants.MILLIS_PER_SECOND);
        assertEquals("Should convert MAX_VALUE to duration correctly", 
                     maxValueDuration, Seconds.MAX_VALUE.toStandardDuration());
    }

    // ========================================================================
    // Tests for arithmetic operations
    // ========================================================================
    
    public void testArithmetic_plus_WithInt_ShouldAddCorrectly() {
        Seconds twoSeconds = Seconds.seconds(2);
        Seconds result = twoSeconds.plus(3);
        
        assertEquals("Original instance should be unchanged", 2, twoSeconds.getSeconds());
        assertEquals("Result should be sum of original and added value", 5, result.getSeconds());
        assertEquals("Adding zero should return same value", 1, Seconds.ONE.plus(0).getSeconds());
    }
    
    public void testArithmetic_plus_WithInt_ShouldThrowOnOverflow() {
        try {
            Seconds.MAX_VALUE.plus(1);
            fail("Should throw ArithmeticException on overflow");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testArithmetic_plus_WithSeconds_ShouldAddCorrectly() {
        Seconds twoSeconds = Seconds.seconds(2);
        Seconds threeSeconds = Seconds.seconds(3);
        Seconds result = twoSeconds.plus(threeSeconds);
        
        assertEquals("First operand should be unchanged", 2, twoSeconds.getSeconds());
        assertEquals("Second operand should be unchanged", 3, threeSeconds.getSeconds());
        assertEquals("Result should be sum of both operands", 5, result.getSeconds());
        assertEquals("Adding ZERO should return same value", 1, Seconds.ONE.plus(Seconds.ZERO).getSeconds());
        assertEquals("Adding null should return same value", 1, Seconds.ONE.plus((Seconds) null).getSeconds());
    }
    
    public void testArithmetic_plus_WithSeconds_ShouldThrowOnOverflow() {
        try {
            Seconds.MAX_VALUE.plus(Seconds.ONE);
            fail("Should throw ArithmeticException on overflow");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testArithmetic_minus_WithInt_ShouldSubtractCorrectly() {
        Seconds twoSeconds = Seconds.seconds(2);
        Seconds result = twoSeconds.minus(3);
        
        assertEquals("Original instance should be unchanged", 2, twoSeconds.getSeconds());
        assertEquals("Result should be difference", -1, result.getSeconds());
        assertEquals("Subtracting zero should return same value", 1, Seconds.ONE.minus(0).getSeconds());
    }
    
    public void testArithmetic_minus_WithInt_ShouldThrowOnUnderflow() {
        try {
            Seconds.MIN_VALUE.minus(1);
            fail("Should throw ArithmeticException on underflow");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testArithmetic_minus_WithSeconds_ShouldSubtractCorrectly() {
        Seconds twoSeconds = Seconds.seconds(2);
        Seconds threeSeconds = Seconds.seconds(3);
        Seconds result = twoSeconds.minus(threeSeconds);
        
        assertEquals("First operand should be unchanged", 2, twoSeconds.getSeconds());
        assertEquals("Second operand should be unchanged", 3, threeSeconds.getSeconds());
        assertEquals("Result should be difference", -1, result.getSeconds());
        assertEquals("Subtracting ZERO should return same value", 1, Seconds.ONE.minus(Seconds.ZERO).getSeconds());
        assertEquals("Subtracting null should return same value", 1, Seconds.ONE.minus((Seconds) null).getSeconds());
    }
    
    public void testArithmetic_minus_WithSeconds_ShouldThrowOnUnderflow() {
        try {
            Seconds.MIN_VALUE.minus(Seconds.ONE);
            fail("Should throw ArithmeticException on underflow");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testArithmetic_multipliedBy_ShouldMultiplyCorrectly() {
        Seconds twoSeconds = Seconds.seconds(2);
        
        assertEquals("Should multiply by positive number", 6, twoSeconds.multipliedBy(3).getSeconds());
        assertEquals("Original should be unchanged", 2, twoSeconds.getSeconds());
        assertEquals("Should multiply by negative number", -6, twoSeconds.multipliedBy(-3).getSeconds());
        assertSame("Multiplying by 1 should return same instance", twoSeconds, twoSeconds.multipliedBy(1));
    }
    
    public void testArithmetic_multipliedBy_ShouldThrowOnOverflow() {
        Seconds halfMax = Seconds.seconds(Integer.MAX_VALUE / 2 + 1);
        try {
            halfMax.multipliedBy(2);
            fail("Should throw ArithmeticException on overflow");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testArithmetic_dividedBy_ShouldDivideCorrectly() {
        Seconds twelveSeconds = Seconds.seconds(12);
        
        assertEquals("12 / 2 should equal 6", 6, twelveSeconds.dividedBy(2).getSeconds());
        assertEquals("Original should be unchanged", 12, twelveSeconds.getSeconds());
        assertEquals("12 / 3 should equal 4", 4, twelveSeconds.dividedBy(3).getSeconds());
        assertEquals("12 / 4 should equal 3 (integer division)", 3, twelveSeconds.dividedBy(4).getSeconds());
        assertEquals("12 / 5 should equal 2 (integer division)", 2, twelveSeconds.dividedBy(5).getSeconds());
        assertEquals("12 / 6 should equal 2", 2, twelveSeconds.dividedBy(6).getSeconds());
        assertSame("Dividing by 1 should return same instance", twelveSeconds, twelveSeconds.dividedBy(1));
    }
    
    public void testArithmetic_dividedBy_ShouldThrowOnDivisionByZero() {
        try {
            Seconds.ONE.dividedBy(0);
            fail("Should throw ArithmeticException on division by zero");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testArithmetic_negated_ShouldNegateCorrectly() {
        Seconds twelveSeconds = Seconds.seconds(12);
        
        assertEquals("Should negate positive value", -12, twelveSeconds.negated().getSeconds());
        assertEquals("Original should be unchanged", 12, twelveSeconds.getSeconds());
    }
    
    public void testArithmetic_negated_ShouldThrowOnMinValueOverflow() {
        try {
            Seconds.MIN_VALUE.negated();
            fail("Should throw ArithmeticException when negating MIN_VALUE");
        } catch (ArithmeticException expected) {
            // Expected behavior - negating Integer.MIN_VALUE would overflow
        }
    }

    // ========================================================================
    // Tests for integration with other Joda Time classes
    // ========================================================================
    
    public void testIntegration_addToLocalDateTime_ShouldAddSecondsCorrectly() {
        Seconds twentySixSeconds = Seconds.seconds(26);
        LocalDateTime baseDateTime = new LocalDateTime(2006, 6, 1, 0, 0, 0, 0);
        LocalDateTime expectedDateTime = new LocalDateTime(2006, 6, 1, 0, 0, 26, 0);
        
        assertEquals("Should add seconds to LocalDateTime correctly", 
                     expectedDateTime, baseDateTime.plus(twentySixSeconds));
    }
}