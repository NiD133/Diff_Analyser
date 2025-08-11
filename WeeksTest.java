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
 * Unit tests for the Weeks class in Joda Time library.
 * Tests all factory methods, arithmetic operations, conversions, and utility methods.
 *
 * @author Stephen Colebourne
 */
public class TestWeeks extends TestCase {
    
    // Using a well-known timezone for consistent test results
    private static final DateTimeZone PARIS_TIMEZONE = DateTimeZone.forID("Europe/Paris");
    
    // Test data constants
    private static final int TWENTY_WEEKS = 20;
    private static final int NEGATIVE_ONE_WEEK = -1;
    private static final int FOUR_WEEKS = 4;
    private static final int TWELVE_WEEKS = 12;
    
    // Date constants for interval testing
    private static final DateTime JUNE_9_2006 = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS_TIMEZONE);
    private static final DateTime JUNE_30_2006 = new DateTime(2006, 6, 30, 12, 0, 0, 0, PARIS_TIMEZONE);
    private static final DateTime JULY_21_2006 = new DateTime(2006, 7, 21, 12, 0, 0, 0, PARIS_TIMEZONE);

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
        // No setup required
    }

    @Override
    protected void tearDown() throws Exception {
        // No cleanup required
    }

    // ========================================================================
    // CONSTANT TESTS
    // ========================================================================
    
    public void testPredefinedConstants_ShouldReturnCorrectValues() {
        assertEquals("ZERO constant should be 0", 0, Weeks.ZERO.getWeeks());
        assertEquals("ONE constant should be 1", 1, Weeks.ONE.getWeeks());
        assertEquals("TWO constant should be 2", 2, Weeks.TWO.getWeeks());
        assertEquals("THREE constant should be 3", 3, Weeks.THREE.getWeeks());
        assertEquals("MAX_VALUE should be Integer.MAX_VALUE", Integer.MAX_VALUE, Weeks.MAX_VALUE.getWeeks());
        assertEquals("MIN_VALUE should be Integer.MIN_VALUE", Integer.MIN_VALUE, Weeks.MIN_VALUE.getWeeks());
    }

    // ========================================================================
    // FACTORY METHOD TESTS
    // ========================================================================
    
    public void testWeeksFactory_WithIntegerValues_ShouldReturnCorrectInstances() {
        // Test that factory method returns singleton instances for common values
        assertSame("weeks(0) should return ZERO singleton", Weeks.ZERO, Weeks.weeks(0));
        assertSame("weeks(1) should return ONE singleton", Weeks.ONE, Weeks.weeks(1));
        assertSame("weeks(2) should return TWO singleton", Weeks.TWO, Weeks.weeks(2));
        assertSame("weeks(3) should return THREE singleton", Weeks.THREE, Weeks.weeks(3));
        assertSame("weeks(MAX_VALUE) should return MAX_VALUE singleton", Weeks.MAX_VALUE, Weeks.weeks(Integer.MAX_VALUE));
        assertSame("weeks(MIN_VALUE) should return MIN_VALUE singleton", Weeks.MIN_VALUE, Weeks.weeks(Integer.MIN_VALUE));
        
        // Test non-singleton values
        assertEquals("weeks(-1) should create instance with -1", NEGATIVE_ONE_WEEK, Weeks.weeks(-1).getWeeks());
        assertEquals("weeks(4) should create instance with 4", FOUR_WEEKS, Weeks.weeks(4).getWeeks());
    }

    public void testWeeksBetween_WithInstants_ShouldCalculateCorrectDifference() {
        DateTime startDate = JUNE_9_2006;
        DateTime endDate1 = JUNE_30_2006;  // 3 weeks later
        DateTime endDate2 = JULY_21_2006;  // 6 weeks later
        
        assertEquals("3 weeks between June 9 and June 30", 3, Weeks.weeksBetween(startDate, endDate1).getWeeks());
        assertEquals("Same dates should return 0 weeks", 0, Weeks.weeksBetween(startDate, startDate).getWeeks());
        assertEquals("Same end dates should return 0 weeks", 0, Weeks.weeksBetween(endDate1, endDate1).getWeeks());
        assertEquals("Reversed dates should return negative weeks", -3, Weeks.weeksBetween(endDate1, startDate).getWeeks());
        assertEquals("6 weeks between June 9 and July 21", 6, Weeks.weeksBetween(startDate, endDate2).getWeeks());
    }

    @SuppressWarnings("deprecation")
    public void testWeeksBetween_WithPartialDates_ShouldCalculateCorrectDifference() {
        LocalDate startDate = new LocalDate(2006, 6, 9);
        LocalDate endDate1 = new LocalDate(2006, 6, 30);
        YearMonthDay endDate2 = new YearMonthDay(2006, 7, 21);
        
        assertEquals("3 weeks between partial dates", 3, Weeks.weeksBetween(startDate, endDate1).getWeeks());
        assertEquals("Same partial dates should return 0", 0, Weeks.weeksBetween(startDate, startDate).getWeeks());
        assertEquals("Same end partial dates should return 0", 0, Weeks.weeksBetween(endDate1, endDate1).getWeeks());
        assertEquals("Reversed partial dates should be negative", -3, Weeks.weeksBetween(endDate1, startDate).getWeeks());
        assertEquals("6 weeks with mixed partial types", 6, Weeks.weeksBetween(startDate, endDate2).getWeeks());
    }

    public void testWeeksIn_WithIntervals_ShouldCalculateCorrectDuration() {
        DateTime startDate = JUNE_9_2006;
        DateTime endDate1 = JUNE_30_2006;
        DateTime endDate2 = JULY_21_2006;
        
        assertEquals("Null interval should return 0 weeks", 0, Weeks.weeksIn((ReadableInterval) null).getWeeks());
        assertEquals("3-week interval should return 3", 3, Weeks.weeksIn(new Interval(startDate, endDate1)).getWeeks());
        assertEquals("Zero-length interval should return 0", 0, Weeks.weeksIn(new Interval(startDate, startDate)).getWeeks());
        assertEquals("Zero-length end interval should return 0", 0, Weeks.weeksIn(new Interval(endDate1, endDate1)).getWeeks());
        assertEquals("6-week interval should return 6", 6, Weeks.weeksIn(new Interval(startDate, endDate2)).getWeeks());
    }

    public void testStandardWeeksIn_WithPeriods_ShouldConvertCorrectly() {
        assertEquals("Null period should return 0", 0, Weeks.standardWeeksIn((ReadablePeriod) null).getWeeks());
        assertEquals("Zero period should return 0", 0, Weeks.standardWeeksIn(Period.ZERO).getWeeks());
        assertEquals("1-week period should return 1", 1, Weeks.standardWeeksIn(new Period(0, 0, 1, 0, 0, 0, 0, 0)).getWeeks());
        assertEquals("123-week period should return 123", 123, Weeks.standardWeeksIn(Period.weeks(123)).getWeeks());
        assertEquals("Negative weeks should work", -987, Weeks.standardWeeksIn(Period.weeks(-987)).getWeeks());
        
        // Test day-to-week conversion (7 days = 1 week)
        assertEquals("13 days should convert to 1 week", 1, Weeks.standardWeeksIn(Period.days(13)).getWeeks());
        assertEquals("14 days should convert to 2 weeks", 2, Weeks.standardWeeksIn(Period.days(14)).getWeeks());
        assertEquals("15 days should convert to 2 weeks", 2, Weeks.standardWeeksIn(Period.days(15)).getWeeks());
        
        // Test that imprecise periods throw exception
        try {
            Weeks.standardWeeksIn(Period.months(1));
            fail("Should throw IllegalArgumentException for imprecise period (months)");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    public void testParseWeeks_WithISOStrings_ShouldParseCorrectly() {
        assertEquals("Null string should return 0", 0, Weeks.parseWeeks((String) null).getWeeks());
        assertEquals("P0W should parse to 0", 0, Weeks.parseWeeks("P0W").getWeeks());
        assertEquals("P1W should parse to 1", 1, Weeks.parseWeeks("P1W").getWeeks());
        assertEquals("P-3W should parse to -3", -3, Weeks.parseWeeks("P-3W").getWeeks());
        assertEquals("Complex format P0Y0M2W should parse to 2", 2, Weeks.parseWeeks("P0Y0M2W").getWeeks());
        assertEquals("Complex format P2WT0H0M should parse to 2", 2, Weeks.parseWeeks("P2WT0H0M").getWeeks());
        
        // Test invalid formats
        try {
            Weeks.parseWeeks("P1Y1D");
            fail("Should reject format with non-week components");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
        
        try {
            Weeks.parseWeeks("P1WT1H");
            fail("Should reject format with time components");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    // ========================================================================
    // GETTER METHOD TESTS
    // ========================================================================
    
    public void testGetWeeks_ShouldReturnStoredValue() {
        Weeks twentyWeeks = Weeks.weeks(TWENTY_WEEKS);
        assertEquals("getWeeks() should return stored value", TWENTY_WEEKS, twentyWeeks.getWeeks());
    }

    public void testGetFieldType_ShouldReturnWeeksFieldType() {
        Weeks twentyWeeks = Weeks.weeks(TWENTY_WEEKS);
        assertEquals("Field type should be weeks", DurationFieldType.weeks(), twentyWeeks.getFieldType());
    }

    public void testGetPeriodType_ShouldReturnWeeksPeriodType() {
        Weeks twentyWeeks = Weeks.weeks(TWENTY_WEEKS);
        assertEquals("Period type should be weeks", PeriodType.weeks(), twentyWeeks.getPeriodType());
    }

    // ========================================================================
    // COMPARISON METHOD TESTS
    // ========================================================================
    
    public void testIsGreaterThan_ShouldCompareCorrectly() {
        assertTrue("THREE should be greater than TWO", Weeks.THREE.isGreaterThan(Weeks.TWO));
        assertFalse("THREE should not be greater than THREE", Weeks.THREE.isGreaterThan(Weeks.THREE));
        assertFalse("TWO should not be greater than THREE", Weeks.TWO.isGreaterThan(Weeks.THREE));
        assertTrue("Positive weeks should be greater than null", Weeks.ONE.isGreaterThan(null));
        assertFalse("Negative weeks should not be greater than null", Weeks.weeks(-1).isGreaterThan(null));
    }

    public void testIsLessThan_ShouldCompareCorrectly() {
        assertFalse("THREE should not be less than TWO", Weeks.THREE.isLessThan(Weeks.TWO));
        assertFalse("THREE should not be less than THREE", Weeks.THREE.isLessThan(Weeks.THREE));
        assertTrue("TWO should be less than THREE", Weeks.TWO.isLessThan(Weeks.THREE));
        assertFalse("Positive weeks should not be less than null", Weeks.ONE.isLessThan(null));
        assertTrue("Negative weeks should be less than null", Weeks.weeks(-1).isLessThan(null));
    }

    // ========================================================================
    // STRING REPRESENTATION TESTS
    // ========================================================================
    
    public void testToString_ShouldReturnISOFormat() {
        Weeks twentyWeeks = Weeks.weeks(TWENTY_WEEKS);
        assertEquals("Positive weeks should format correctly", "P20W", twentyWeeks.toString());
        
        Weeks negativeWeeks = Weeks.weeks(-TWENTY_WEEKS);
        assertEquals("Negative weeks should format correctly", "P-20W", negativeWeeks.toString());
    }

    // ========================================================================
    // SERIALIZATION TESTS
    // ========================================================================
    
    public void testSerialization_ShouldPreserveSingletonInstances() throws Exception {
        Weeks originalWeeks = Weeks.THREE;
        
        // Serialize the object
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
        objectOutput.writeObject(originalWeeks);
        objectOutput.close();
        byte[] serializedData = byteOutput.toByteArray();
        
        // Deserialize the object
        ByteArrayInputStream byteInput = new ByteArrayInputStream(serializedData);
        ObjectInputStream objectInput = new ObjectInputStream(byteInput);
        Weeks deserializedWeeks = (Weeks) objectInput.readObject();
        objectInput.close();
        
        assertSame("Deserialized instance should be same singleton", originalWeeks, deserializedWeeks);
    }

    // ========================================================================
    // CONVERSION METHOD TESTS
    // ========================================================================
    
    public void testToStandardDays_ShouldConvertCorrectly() {
        Weeks twoWeeks = Weeks.weeks(2);
        Days expectedDays = Days.days(14); // 2 weeks * 7 days/week = 14 days
        assertEquals("2 weeks should convert to 14 days", expectedDays, twoWeeks.toStandardDays());
        
        // Test overflow protection
        try {
            Weeks.MAX_VALUE.toStandardDays();
            fail("MAX_VALUE conversion should throw ArithmeticException");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testToStandardHours_ShouldConvertCorrectly() {
        Weeks twoWeeks = Weeks.weeks(2);
        Hours expectedHours = Hours.hours(2 * 7 * 24); // 2 weeks * 7 days * 24 hours = 336 hours
        assertEquals("2 weeks should convert to 336 hours", expectedHours, twoWeeks.toStandardHours());
        
        // Test overflow protection
        try {
            Weeks.MAX_VALUE.toStandardHours();
            fail("MAX_VALUE conversion should throw ArithmeticException");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testToStandardMinutes_ShouldConvertCorrectly() {
        Weeks twoWeeks = Weeks.weeks(2);
        Minutes expectedMinutes = Minutes.minutes(2 * 7 * 24 * 60); // 2 weeks * 7 * 24 * 60 = 20,160 minutes
        assertEquals("2 weeks should convert to 20,160 minutes", expectedMinutes, twoWeeks.toStandardMinutes());
        
        // Test overflow protection
        try {
            Weeks.MAX_VALUE.toStandardMinutes();
            fail("MAX_VALUE conversion should throw ArithmeticException");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testToStandardSeconds_ShouldConvertCorrectly() {
        Weeks twoWeeks = Weeks.weeks(2);
        Seconds expectedSeconds = Seconds.seconds(2 * 7 * 24 * 60 * 60); // 2 weeks in seconds
        assertEquals("2 weeks should convert correctly to seconds", expectedSeconds, twoWeeks.toStandardSeconds());
        
        // Test overflow protection
        try {
            Weeks.MAX_VALUE.toStandardSeconds();
            fail("MAX_VALUE conversion should throw ArithmeticException");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testToStandardDuration_ShouldConvertCorrectly() {
        Weeks twentyWeeks = Weeks.weeks(TWENTY_WEEKS);
        Duration expectedDuration = new Duration(20L * DateTimeConstants.MILLIS_PER_WEEK);
        assertEquals("20 weeks should convert to correct duration", expectedDuration, twentyWeeks.toStandardDuration());
        
        // Test MAX_VALUE conversion (should not overflow for Duration)
        Duration maxDuration = new Duration(((long) Integer.MAX_VALUE) * DateTimeConstants.MILLIS_PER_WEEK);
        assertEquals("MAX_VALUE should convert to correct duration", maxDuration, Weeks.MAX_VALUE.toStandardDuration());
    }

    // ========================================================================
    // ARITHMETIC OPERATION TESTS
    // ========================================================================
    
    public void testPlus_WithInteger_ShouldAddCorrectly() {
        Weeks twoWeeks = Weeks.weeks(2);
        Weeks result = twoWeeks.plus(3);
        
        assertEquals("Original should be unchanged", 2, twoWeeks.getWeeks());
        assertEquals("Result should be sum", 5, result.getWeeks());
        assertEquals("Adding zero should return same value", 1, Weeks.ONE.plus(0).getWeeks());
        
        // Test overflow protection
        try {
            Weeks.MAX_VALUE.plus(1);
            fail("Adding to MAX_VALUE should throw ArithmeticException");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testPlus_WithWeeks_ShouldAddCorrectly() {
        Weeks twoWeeks = Weeks.weeks(2);
        Weeks threeWeeks = Weeks.weeks(3);
        Weeks result = twoWeeks.plus(threeWeeks);
        
        assertEquals("First operand should be unchanged", 2, twoWeeks.getWeeks());
        assertEquals("Second operand should be unchanged", 3, threeWeeks.getWeeks());
        assertEquals("Result should be sum", 5, result.getWeeks());
        assertEquals("Adding ZERO should return same value", 1, Weeks.ONE.plus(Weeks.ZERO).getWeeks());
        assertEquals("Adding null should return same value", 1, Weeks.ONE.plus((Weeks) null).getWeeks());
        
        // Test overflow protection
        try {
            Weeks.MAX_VALUE.plus(Weeks.ONE);
            fail("Adding to MAX_VALUE should throw ArithmeticException");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testMinus_WithInteger_ShouldSubtractCorrectly() {
        Weeks twoWeeks = Weeks.weeks(2);
        Weeks result = twoWeeks.minus(3);
        
        assertEquals("Original should be unchanged", 2, twoWeeks.getWeeks());
        assertEquals("Result should be difference", -1, result.getWeeks());
        assertEquals("Subtracting zero should return same value", 1, Weeks.ONE.minus(0).getWeeks());
        
        // Test underflow protection
        try {
            Weeks.MIN_VALUE.minus(1);
            fail("Subtracting from MIN_VALUE should throw ArithmeticException");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testMinus_WithWeeks_ShouldSubtractCorrectly() {
        Weeks twoWeeks = Weeks.weeks(2);
        Weeks threeWeeks = Weeks.weeks(3);
        Weeks result = twoWeeks.minus(threeWeeks);
        
        assertEquals("First operand should be unchanged", 2, twoWeeks.getWeeks());
        assertEquals("Second operand should be unchanged", 3, threeWeeks.getWeeks());
        assertEquals("Result should be difference", -1, result.getWeeks());
        assertEquals("Subtracting ZERO should return same value", 1, Weeks.ONE.minus(Weeks.ZERO).getWeeks());
        assertEquals("Subtracting null should return same value", 1, Weeks.ONE.minus((Weeks) null).getWeeks());
        
        // Test underflow protection
        try {
            Weeks.MIN_VALUE.minus(Weeks.ONE);
            fail("Subtracting from MIN_VALUE should throw ArithmeticException");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testMultipliedBy_ShouldMultiplyCorrectly() {
        Weeks twoWeeks = Weeks.weeks(2);
        
        assertEquals("Multiply by 3 should work", 6, twoWeeks.multipliedBy(3).getWeeks());
        assertEquals("Original should be unchanged", 2, twoWeeks.getWeeks());
        assertEquals("Multiply by negative should work", -6, twoWeeks.multipliedBy(-3).getWeeks());
        assertSame("Multiply by 1 should return same instance", twoWeeks, twoWeeks.multipliedBy(1));
        
        // Test overflow protection
        Weeks halfMaxWeeks = Weeks.weeks(Integer.MAX_VALUE / 2 + 1);
        try {
            halfMaxWeeks.multipliedBy(2);
            fail("Multiplication causing overflow should throw ArithmeticException");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testDividedBy_ShouldDivideCorrectly() {
        Weeks twelveWeeks = Weeks.weeks(TWELVE_WEEKS);
        
        assertEquals("12 / 2 should equal 6", 6, twelveWeeks.dividedBy(2).getWeeks());
        assertEquals("Original should be unchanged", TWELVE_WEEKS, twelveWeeks.getWeeks());
        assertEquals("12 / 3 should equal 4", 4, twelveWeeks.dividedBy(3).getWeeks());
        assertEquals("12 / 4 should equal 3", 3, twelveWeeks.dividedBy(4).getWeeks());
        assertEquals("12 / 5 should equal 2 (integer division)", 2, twelveWeeks.dividedBy(5).getWeeks());
        assertEquals("12 / 6 should equal 2", 2, twelveWeeks.dividedBy(6).getWeeks());
        assertSame("Divide by 1 should return same instance", twelveWeeks, twelveWeeks.dividedBy(1));
        
        // Test division by zero protection
        try {
            Weeks.ONE.dividedBy(0);
            fail("Division by zero should throw ArithmeticException");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    public void testNegated_ShouldNegateCorrectly() {
        Weeks twelveWeeks = Weeks.weeks(TWELVE_WEEKS);
        
        assertEquals("Negation should flip sign", -TWELVE_WEEKS, twelveWeeks.negated().getWeeks());
        assertEquals("Original should be unchanged", TWELVE_WEEKS, twelveWeeks.getWeeks());
        
        // Test overflow protection
        try {
            Weeks.MIN_VALUE.negated();
            fail("Negating MIN_VALUE should throw ArithmeticException");
        } catch (ArithmeticException expected) {
            // Expected behavior
        }
    }

    // ========================================================================
    // INTEGRATION TESTS
    // ========================================================================
    
    public void testAddToLocalDate_ShouldAddWeeksCorrectly() {
        Weeks threeWeeks = Weeks.weeks(3);
        LocalDate startDate = new LocalDate(2006, 6, 1);
        LocalDate expectedDate = new LocalDate(2006, 6, 22); // 3 weeks = 21 days later
        
        assertEquals("Adding 3 weeks to June 1 should give June 22", expectedDate, startDate.plus(threeWeeks));
    }
}