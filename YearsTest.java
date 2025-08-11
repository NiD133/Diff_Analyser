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
 * Unit tests for the Years class in Joda Time.
 * Tests all factory methods, mathematical operations, comparisons, and utility methods.
 *
 * @author Stephen Colebourne
 */
public class TestYears extends TestCase {
    
    // Using Paris timezone for consistent test results across different environments
    private static final DateTimeZone PARIS_TIMEZONE = DateTimeZone.forID("Europe/Paris");
    
    // Test data constants for better readability
    private static final int NEGATIVE_YEARS = -1;
    private static final int FOUR_YEARS = 4;
    private static final int TWENTY_YEARS = 20;
    
    // Date constants for interval testing
    private static final DateTime JUNE_2006 = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS_TIMEZONE);
    private static final DateTime JUNE_2009 = new DateTime(2009, 6, 9, 12, 0, 0, 0, PARIS_TIMEZONE);
    private static final DateTime JUNE_2012 = new DateTime(2012, 6, 9, 12, 0, 0, 0, PARIS_TIMEZONE);

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestYears.class);
    }

    public TestYears(String name) {
        super(name);
    }

    // Test predefined constants
    public void testPredefinedConstants() {
        assertEquals("ZERO constant should be 0 years", 0, Years.ZERO.getYears());
        assertEquals("ONE constant should be 1 year", 1, Years.ONE.getYears());
        assertEquals("TWO constant should be 2 years", 2, Years.TWO.getYears());
        assertEquals("THREE constant should be 3 years", 3, Years.THREE.getYears());
        assertEquals("MAX_VALUE should be Integer.MAX_VALUE", Integer.MAX_VALUE, Years.MAX_VALUE.getYears());
        assertEquals("MIN_VALUE should be Integer.MIN_VALUE", Integer.MIN_VALUE, Years.MIN_VALUE.getYears());
    }

    // Test factory method Years.years(int)
    public void testFactoryMethod_years() {
        // Test that constants are reused for efficiency
        assertSame("Should reuse ZERO constant", Years.ZERO, Years.years(0));
        assertSame("Should reuse ONE constant", Years.ONE, Years.years(1));
        assertSame("Should reuse TWO constant", Years.TWO, Years.years(2));
        assertSame("Should reuse THREE constant", Years.THREE, Years.years(3));
        assertSame("Should reuse MAX_VALUE constant", Years.MAX_VALUE, Years.years(Integer.MAX_VALUE));
        assertSame("Should reuse MIN_VALUE constant", Years.MIN_VALUE, Years.years(Integer.MIN_VALUE));
        
        // Test non-constant values
        assertEquals("Should create Years with -1", NEGATIVE_YEARS, Years.years(NEGATIVE_YEARS).getYears());
        assertEquals("Should create Years with 4", FOUR_YEARS, Years.years(FOUR_YEARS).getYears());
    }

    // Test Years.yearsBetween() with DateTime instances
    public void testFactoryMethod_yearsBetween_withDateTimes() {
        assertEquals("Should calculate 3 years between June 2006 and June 2009", 
                    3, Years.yearsBetween(JUNE_2006, JUNE_2009).getYears());
        assertEquals("Should calculate 0 years between same dates", 
                    0, Years.yearsBetween(JUNE_2006, JUNE_2006).getYears());
        assertEquals("Should calculate 0 years between same end dates", 
                    0, Years.yearsBetween(JUNE_2009, JUNE_2009).getYears());
        assertEquals("Should calculate -3 years when end is before start", 
                    -3, Years.yearsBetween(JUNE_2009, JUNE_2006).getYears());
        assertEquals("Should calculate 6 years between June 2006 and June 2012", 
                    6, Years.yearsBetween(JUNE_2006, JUNE_2012).getYears());
    }

    // Test Years.yearsBetween() with LocalDate instances
    @SuppressWarnings("deprecation")
    public void testFactoryMethod_yearsBetween_withLocalDates() {
        LocalDate startDate = new LocalDate(2006, 6, 9);
        LocalDate endDate2009 = new LocalDate(2009, 6, 9);
        YearMonthDay endDate2012 = new YearMonthDay(2012, 6, 9); // Testing deprecated class
        
        assertEquals("Should calculate 3 years between local dates", 
                    3, Years.yearsBetween(startDate, endDate2009).getYears());
        assertEquals("Should calculate 0 years between same local dates", 
                    0, Years.yearsBetween(startDate, startDate).getYears());
        assertEquals("Should calculate 0 years between same end dates", 
                    0, Years.yearsBetween(endDate2009, endDate2009).getYears());
        assertEquals("Should calculate -3 years when end is before start", 
                    -3, Years.yearsBetween(endDate2009, startDate).getYears());
        assertEquals("Should calculate 6 years with YearMonthDay", 
                    6, Years.yearsBetween(startDate, endDate2012).getYears());
    }

    // Test Years.yearsIn() with intervals
    public void testFactoryMethod_yearsIn_withIntervals() {
        assertEquals("Should return 0 years for null interval", 
                    0, Years.yearsIn((ReadableInterval) null).getYears());
        assertEquals("Should calculate 3 years in interval", 
                    3, Years.yearsIn(new Interval(JUNE_2006, JUNE_2009)).getYears());
        assertEquals("Should return 0 years for zero-length interval", 
                    0, Years.yearsIn(new Interval(JUNE_2006, JUNE_2006)).getYears());
        assertEquals("Should return 0 years for zero-length end interval", 
                    0, Years.yearsIn(new Interval(JUNE_2009, JUNE_2009)).getYears());
        assertEquals("Should calculate 6 years in longer interval", 
                    6, Years.yearsIn(new Interval(JUNE_2006, JUNE_2012)).getYears());
    }

    // Test Years.parseYears() with ISO8601 strings
    public void testFactoryMethod_parseYears() {
        // Valid parsing cases
        assertEquals("Should parse null as 0 years", 0, Years.parseYears(null).getYears());
        assertEquals("Should parse P0Y as 0 years", 0, Years.parseYears("P0Y").getYears());
        assertEquals("Should parse P1Y as 1 year", 1, Years.parseYears("P1Y").getYears());
        assertEquals("Should parse P-3Y as -3 years", -3, Years.parseYears("P-3Y").getYears());
        assertEquals("Should parse P2Y0M as 2 years (ignoring months)", 2, Years.parseYears("P2Y0M").getYears());
        assertEquals("Should parse P2YT0H0M as 2 years (ignoring time)", 2, Years.parseYears("P2YT0H0M").getYears());
        
        // Invalid parsing cases - should throw exceptions
        assertParsingThrowsException("P1M1D", "Should reject string with non-zero months and days");
        assertParsingThrowsException("P1YT1H", "Should reject string with non-zero hours");
    }

    private void assertParsingThrowsException(String periodString, String message) {
        try {
            Years.parseYears(periodString);
            fail(message + " - expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // This is the expected behavior
        }
    }

    // Test getter methods
    public void testGetterMethods() {
        Years twentyYears = Years.years(TWENTY_YEARS);
        
        assertEquals("getYears() should return the year value", TWENTY_YEARS, twentyYears.getYears());
        assertEquals("getFieldType() should return years field type", 
                    DurationFieldType.years(), twentyYears.getFieldType());
        assertEquals("getPeriodType() should return years period type", 
                    PeriodType.years(), twentyYears.getPeriodType());
    }

    // Test comparison methods
    public void testComparison_isGreaterThan() {
        assertTrue("THREE should be greater than TWO", Years.THREE.isGreaterThan(Years.TWO));
        assertFalse("THREE should not be greater than THREE", Years.THREE.isGreaterThan(Years.THREE));
        assertFalse("TWO should not be greater than THREE", Years.TWO.isGreaterThan(Years.THREE));
        assertTrue("Positive years should be greater than null", Years.ONE.isGreaterThan(null));
        assertFalse("Negative years should not be greater than null", Years.years(NEGATIVE_YEARS).isGreaterThan(null));
    }

    public void testComparison_isLessThan() {
        assertFalse("THREE should not be less than TWO", Years.THREE.isLessThan(Years.TWO));
        assertFalse("THREE should not be less than THREE", Years.THREE.isLessThan(Years.THREE));
        assertTrue("TWO should be less than THREE", Years.TWO.isLessThan(Years.THREE));
        assertFalse("Positive years should not be less than null", Years.ONE.isLessThan(null));
        assertTrue("Negative years should be less than null", Years.years(NEGATIVE_YEARS).isLessThan(null));
    }

    // Test string representation
    public void testStringRepresentation() {
        assertEquals("Positive years should format correctly", "P20Y", Years.years(TWENTY_YEARS).toString());
        assertEquals("Negative years should format correctly", "P-20Y", Years.years(-TWENTY_YEARS).toString());
    }

    // Test serialization/deserialization
    public void testSerialization() throws Exception {
        Years originalYears = Years.THREE;
        
        // Serialize the object
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);
        objectOutput.writeObject(originalYears);
        objectOutput.close();
        byte[] serializedData = outputStream.toByteArray();
        
        // Deserialize the object
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
        ObjectInputStream objectInput = new ObjectInputStream(inputStream);
        Years deserializedYears = (Years) objectInput.readObject();
        objectInput.close();
        
        assertSame("Deserialized object should be the same instance (singleton)", 
                  originalYears, deserializedYears);
    }

    // Test addition operations
    public void testArithmetic_plus_int() {
        Years twoYears = Years.years(2);
        Years result = twoYears.plus(3);
        
        assertEquals("Original object should be unchanged", 2, twoYears.getYears());
        assertEquals("Result should be sum of original and added years", 5, result.getYears());
        assertEquals("Adding zero should not change value", 1, Years.ONE.plus(0).getYears());
        
        assertArithmeticOverflowException(() -> Years.MAX_VALUE.plus(1), 
                                        "Adding to MAX_VALUE should cause overflow");
    }

    public void testArithmetic_plus_Years() {
        Years twoYears = Years.years(2);
        Years threeYears = Years.years(3);
        Years result = twoYears.plus(threeYears);
        
        assertEquals("First operand should be unchanged", 2, twoYears.getYears());
        assertEquals("Second operand should be unchanged", 3, threeYears.getYears());
        assertEquals("Result should be sum of both operands", 5, result.getYears());
        assertEquals("Adding ZERO should not change value", 1, Years.ONE.plus(Years.ZERO).getYears());
        assertEquals("Adding null should not change value", 1, Years.ONE.plus((Years) null).getYears());
        
        assertArithmeticOverflowException(() -> Years.MAX_VALUE.plus(Years.ONE), 
                                        "Adding to MAX_VALUE should cause overflow");
    }

    // Test subtraction operations
    public void testArithmetic_minus_int() {
        Years twoYears = Years.years(2);
        Years result = twoYears.minus(3);
        
        assertEquals("Original object should be unchanged", 2, twoYears.getYears());
        assertEquals("Result should be difference", -1, result.getYears());
        assertEquals("Subtracting zero should not change value", 1, Years.ONE.minus(0).getYears());
        
        assertArithmeticOverflowException(() -> Years.MIN_VALUE.minus(1), 
                                        "Subtracting from MIN_VALUE should cause overflow");
    }

    public void testArithmetic_minus_Years() {
        Years twoYears = Years.years(2);
        Years threeYears = Years.years(3);
        Years result = twoYears.minus(threeYears);
        
        assertEquals("First operand should be unchanged", 2, twoYears.getYears());
        assertEquals("Second operand should be unchanged", 3, threeYears.getYears());
        assertEquals("Result should be difference", -1, result.getYears());
        assertEquals("Subtracting ZERO should not change value", 1, Years.ONE.minus(Years.ZERO).getYears());
        assertEquals("Subtracting null should not change value", 1, Years.ONE.minus((Years) null).getYears());
        
        assertArithmeticOverflowException(() -> Years.MIN_VALUE.minus(Years.ONE), 
                                        "Subtracting from MIN_VALUE should cause overflow");
    }

    // Test multiplication operations
    public void testArithmetic_multipliedBy() {
        Years twoYears = Years.years(2);
        
        assertEquals("Should multiply correctly", 6, twoYears.multipliedBy(3).getYears());
        assertEquals("Original should be unchanged", 2, twoYears.getYears());
        assertEquals("Should handle negative multiplier", -6, twoYears.multipliedBy(-3).getYears());
        assertSame("Multiplying by 1 should return same instance", twoYears, twoYears.multipliedBy(1));
        
        Years halfMaxPlus1 = Years.years(Integer.MAX_VALUE / 2 + 1);
        assertArithmeticOverflowException(() -> halfMaxPlus1.multipliedBy(2), 
                                        "Multiplication causing overflow should throw exception");
    }

    // Test division operations
    public void testArithmetic_dividedBy() {
        Years twelveYears = Years.years(12);
        
        assertEquals("12 ÷ 2 should equal 6", 6, twelveYears.dividedBy(2).getYears());
        assertEquals("Original should be unchanged", 12, twelveYears.getYears());
        assertEquals("12 ÷ 3 should equal 4", 4, twelveYears.dividedBy(3).getYears());
        assertEquals("12 ÷ 4 should equal 3", 3, twelveYears.dividedBy(4).getYears());
        assertEquals("12 ÷ 5 should equal 2 (integer division)", 2, twelveYears.dividedBy(5).getYears());
        assertEquals("12 ÷ 6 should equal 2", 2, twelveYears.dividedBy(6).getYears());
        assertSame("Dividing by 1 should return same instance", twelveYears, twelveYears.dividedBy(1));
        
        assertArithmeticOverflowException(() -> Years.ONE.dividedBy(0), 
                                        "Division by zero should throw exception");
    }

    // Test negation operation
    public void testArithmetic_negated() {
        Years twelveYears = Years.years(12);
        
        assertEquals("Negation should flip sign", -12, twelveYears.negated().getYears());
        assertEquals("Original should be unchanged", 12, twelveYears.getYears());
        
        assertArithmeticOverflowException(() -> Years.MIN_VALUE.negated(), 
                                        "Negating MIN_VALUE should cause overflow");
    }

    // Test integration with LocalDate
    public void testIntegration_addToLocalDate() {
        Years threeYears = Years.years(3);
        LocalDate startDate = new LocalDate(2006, 6, 1);
        LocalDate expectedDate = new LocalDate(2009, 6, 1);
        
        assertEquals("Adding years to LocalDate should work correctly", 
                    expectedDate, startDate.plus(threeYears));
    }

    // Helper method for testing arithmetic overflow exceptions
    private void assertArithmeticOverflowException(Runnable operation, String message) {
        try {
            operation.run();
            fail(message + " - expected ArithmeticException");
        } catch (ArithmeticException expected) {
            // This is the expected behavior
        }
    }

    @Override
    protected void setUp() throws Exception {
        // No setup required for these tests
    }

    @Override
    protected void tearDown() throws Exception {
        // No cleanup required for these tests
    }
}