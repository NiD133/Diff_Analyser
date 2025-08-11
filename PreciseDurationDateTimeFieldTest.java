/*
 *  Copyright 2001-2005 Stephen Colebourne
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
package org.joda.time.field;

import java.util.Arrays;
import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;

/**
 * Unit tests for PreciseDurationDateTimeField.
 * Tests the behavior of a datetime field that has a precise unit duration.
 *
 * @author Stephen Colebourne
 */
public class TestPreciseDurationDateTimeField extends TestCase {

    // Test constants
    private static final long SECONDS_TO_MILLIS = 60L;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MAX_SECOND_VALUE = 59;
    
    // Sample time values for testing: 12:30:40.050
    private static final TimeOfDay SAMPLE_TIME = new TimeOfDay(12, 30, 40, 50);

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestPreciseDurationDateTimeField.class);
    }

    public TestPreciseDurationDateTimeField(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        // Reset mock field counters before each test
        MockCountingDurationField.resetCounters();
    }

    @Override
    protected void tearDown() throws Exception {
        // No cleanup needed
    }

    //-----------------------------------------------------------------------
    // Constructor Tests
    //-----------------------------------------------------------------------
    
    public void testConstructor_WithValidParameters_ShouldSucceed() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        assertEquals("Field type should be secondOfMinute", 
                     DateTimeFieldType.secondOfMinute(), field.getType());
    }

    public void testConstructor_WithNullParameters_ShouldThrowException() {
        try {
            new TestableSecondOfMinuteField(null, null);
            fail("Should throw IllegalArgumentException for null parameters");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    public void testConstructor_WithImpreciseDurationField_ShouldThrowException() {
        try {
            new TestableSecondOfMinuteField(
                DateTimeFieldType.minuteOfHour(),
                new MockImpreciseDurationField(DurationFieldType.minutes()));
            fail("Should throw IllegalArgumentException for imprecise duration field");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    public void testConstructor_WithZeroDurationField_ShouldThrowException() {
        try {
            new TestableSecondOfMinuteField(
                DateTimeFieldType.minuteOfHour(),
                new MockZeroDurationField(DurationFieldType.minutes()));
            fail("Should throw IllegalArgumentException for zero duration field");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    //-----------------------------------------------------------------------
    // Basic Property Tests
    //-----------------------------------------------------------------------

    public void testGetType_ShouldReturnCorrectFieldType() {
        BaseDateTimeField field = new TestableSecondOfMinuteField(
            DateTimeFieldType.secondOfDay(), 
            new MockCountingDurationField(DurationFieldType.minutes()));
        
        assertEquals("Field type should match constructor parameter",
                     DateTimeFieldType.secondOfDay(), field.getType());
    }

    public void testGetName_ShouldReturnFieldTypeName() {
        BaseDateTimeField field = new TestableSecondOfMinuteField(
            DateTimeFieldType.secondOfDay(), 
            new MockCountingDurationField(DurationFieldType.minutes()));
        
        assertEquals("Field name should match field type name",
                     "secondOfDay", field.getName());
    }

    public void testToString_ShouldReturnFormattedString() {
        BaseDateTimeField field = new TestableSecondOfMinuteField(
            DateTimeFieldType.secondOfDay(), 
            new MockCountingDurationField(DurationFieldType.minutes()));
        
        assertEquals("toString should return formatted field name",
                     "DateTimeField[secondOfDay]", field.toString());
    }

    public void testIsSupported_ShouldReturnTrue() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        assertTrue("Field should be supported", field.isSupported());
    }

    public void testIsLenient_ShouldReturnFalse() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        assertFalse("Field should not be lenient", field.isLenient());
    }

    //-----------------------------------------------------------------------
    // Value Extraction Tests
    //-----------------------------------------------------------------------

    public void testGet_WithVariousInstants_ShouldReturnCorrectValues() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Zero instant should return 0", 0, field.get(0));
        assertEquals("60ms instant should return 1", 1, field.get(SECONDS_TO_MILLIS));
        assertEquals("123ms instant should return 2", 2, field.get(123));
    }

    //-----------------------------------------------------------------------
    // Text Representation Tests
    //-----------------------------------------------------------------------

    public void testGetAsText_WithLongAndLocale_ShouldReturnNumericString() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        long testInstant = SECONDS_TO_MILLIS * 29; // 29 seconds
        
        assertEquals("Should return numeric string with English locale",
                     "29", field.getAsText(testInstant, Locale.ENGLISH));
        assertEquals("Should return numeric string with null locale",
                     "29", field.getAsText(testInstant, null));
    }

    public void testGetAsText_WithLongOnly_ShouldReturnNumericString() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        long testInstant = SECONDS_TO_MILLIS * 29;
        
        assertEquals("Should return numeric string", "29", field.getAsText(testInstant));
    }

    public void testGetAsText_WithReadablePartialAndValue_ShouldReturnValueAsString() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Should return provided value as string",
                     "20", field.getAsText(SAMPLE_TIME, 20, Locale.ENGLISH));
        assertEquals("Should work with null locale",
                     "20", field.getAsText(SAMPLE_TIME, 20, null));
    }

    public void testGetAsText_WithReadablePartial_ShouldReturnFieldValueAsString() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Should return field value from ReadablePartial",
                     "40", field.getAsText(SAMPLE_TIME, Locale.ENGLISH));
        assertEquals("Should work with null locale",
                     "40", field.getAsText(SAMPLE_TIME, null));
    }

    public void testGetAsText_WithIntAndLocale_ShouldReturnIntAsString() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Should return int value as string",
                     "80", field.getAsText(80, Locale.ENGLISH));
        assertEquals("Should work with null locale",
                     "80", field.getAsText(80, null));
    }

    //-----------------------------------------------------------------------
    // Short Text Representation Tests (same behavior as regular text)
    //-----------------------------------------------------------------------

    public void testGetAsShortText_WithLongAndLocale_ShouldReturnNumericString() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        long testInstant = SECONDS_TO_MILLIS * 29;
        
        assertEquals("Short text should match regular text",
                     "29", field.getAsShortText(testInstant, Locale.ENGLISH));
        assertEquals("Should work with null locale",
                     "29", field.getAsShortText(testInstant, null));
    }

    public void testGetAsShortText_WithLongOnly_ShouldReturnNumericString() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        long testInstant = SECONDS_TO_MILLIS * 29;
        
        assertEquals("Short text should match regular text", 
                     "29", field.getAsShortText(testInstant));
    }

    public void testGetAsShortText_WithReadablePartialAndValue_ShouldReturnValueAsString() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Short text should match regular text",
                     "20", field.getAsShortText(SAMPLE_TIME, 20, Locale.ENGLISH));
        assertEquals("Should work with null locale",
                     "20", field.getAsShortText(SAMPLE_TIME, 20, null));
    }

    public void testGetAsShortText_WithReadablePartial_ShouldReturnFieldValueAsString() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Short text should match regular text",
                     "40", field.getAsShortText(SAMPLE_TIME, Locale.ENGLISH));
        assertEquals("Should work with null locale",
                     "40", field.getAsShortText(SAMPLE_TIME, null));
    }

    public void testGetAsShortText_WithIntAndLocale_ShouldReturnIntAsString() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Short text should match regular text",
                     "80", field.getAsShortText(80, Locale.ENGLISH));
        assertEquals("Should work with null locale",
                     "80", field.getAsShortText(80, null));
    }

    //-----------------------------------------------------------------------
    // Addition Operation Tests
    //-----------------------------------------------------------------------

    public void testAdd_WithLongAndInt_ShouldDelegateToUnitField() {
        MockCountingDurationField.resetCounters();
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        long result = field.add(1L, 1);
        
        assertEquals("Should add 1 second (60ms) to 1ms", 61, result);
        assertEquals("Should call unit field add method once", 
                     1, MockCountingDurationField.add_int);
    }

    public void testAdd_WithLongAndLong_ShouldDelegateToUnitField() {
        MockCountingDurationField.resetCounters();
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        long result = field.add(1L, 1L);
        
        assertEquals("Should add 1 second (60ms) to 1ms", 61, result);
        assertEquals("Should call unit field add method once", 
                     1, MockCountingDurationField.add_long);
    }

    public void testAdd_WithReadablePartialAndArray_ShouldHandleOverflow() {
        BaseDateTimeField field = new TestableStandardField();
        
        // Test no change
        int[] values = {10, 20, 30, 40};
        int[] expected = {10, 20, 30, 40};
        int[] result = field.add(new TimeOfDay(), 2, values, 0);
        assertTrue("No change should preserve values", Arrays.equals(expected, result));
        
        // Test simple addition
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 20, 31, 40};
        result = field.add(new TimeOfDay(), 2, values, 1);
        assertTrue("Should add 1 to field at index 2", Arrays.equals(expected, result));
        
        // Test overflow to next field
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 21, 0, 40};
        result = field.add(new TimeOfDay(), 2, values, 30);
        assertTrue("Should overflow seconds to minutes", Arrays.equals(expected, result));
        
        // Test overflow beyond maximum
        values = new int[]{23, 59, 30, 40};
        try {
            field.add(new TimeOfDay(), 2, values, 30);
            fail("Should throw exception when overflow exceeds maximum time");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
        
        // Test subtraction
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 20, 29, 40};
        result = field.add(new TimeOfDay(), 2, values, -1);
        assertTrue("Should subtract 1 from field", Arrays.equals(expected, result));
        
        // Test underflow to previous field
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 19, 59, 40};
        result = field.add(new TimeOfDay(), 2, values, -31);
        assertTrue("Should underflow seconds to minutes", Arrays.equals(expected, result));
        
        // Test underflow beyond minimum
        values = new int[]{0, 0, 30, 40};
        try {
            field.add(new TimeOfDay(), 2, values, -31);
            fail("Should throw exception when underflow exceeds minimum time");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    //-----------------------------------------------------------------------
    // Wrap Field Tests
    //-----------------------------------------------------------------------

    public void testAddWrapField_WithLongAndInt_ShouldWrapWithinFieldRange() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        long baseTime = SECONDS_TO_MILLIS * 29; // 29 seconds
        
        assertEquals("No change should preserve value", 
                     baseTime, field.addWrapField(baseTime, 0));
        assertEquals("Should add and stay within range", 
                     SECONDS_TO_MILLIS * 59, field.addWrapField(baseTime, 30));
        assertEquals("Should wrap to beginning of range", 
                     0L, field.addWrapField(baseTime, 31));
    }

    public void testAddWrapField_WithReadablePartialAndArray_ShouldWrapWithinFieldRange() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        // Test no change
        int[] values = {10, 20, 30, 40};
        int[] expected = {10, 20, 30, 40};
        int[] result = field.addWrapField(new TimeOfDay(), 2, values, 0);
        assertTrue("No change should preserve values", Arrays.equals(expected, result));
        
        // Test addition within range
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 20, 59, 40};
        result = field.addWrapField(new TimeOfDay(), 2, values, 29);
        assertTrue("Should add within field range", Arrays.equals(expected, result));
        
        // Test wrap to minimum
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 20, 0, 40};
        result = field.addWrapField(new TimeOfDay(), 2, values, 30);
        assertTrue("Should wrap to field minimum", Arrays.equals(expected, result));
        
        // Test wrap beyond minimum
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 20, 1, 40};
        result = field.addWrapField(new TimeOfDay(), 2, values, 31);
        assertTrue("Should wrap beyond minimum", Arrays.equals(expected, result));
    }

    //-----------------------------------------------------------------------
    // Difference Calculation Tests
    //-----------------------------------------------------------------------

    public void testGetDifference_ShouldDelegateToUnitField() {
        MockCountingDurationField.resetCounters();
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        int result = field.getDifference(0L, 0L);
        
        assertEquals("Should return mocked difference value", 30, result);
        assertEquals("Should call unit field difference method once", 
                     1, MockCountingDurationField.difference_long);
    }

    public void testGetDifferenceAsLong_ShouldDelegateToUnitField() {
        MockCountingDurationField.resetCounters();
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        long result = field.getDifferenceAsLong(0L, 0L);
        
        assertEquals("Should return mocked difference value", 30L, result);
        assertEquals("Should call unit field difference method once", 
                     1, MockCountingDurationField.difference_long);
    }

    //-----------------------------------------------------------------------
    // Set Value Tests
    //-----------------------------------------------------------------------

    public void testSet_WithLongAndInt_ShouldSetFieldValue() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Setting to 0 should return 0", 0, field.set(120L, 0));
        assertEquals("Setting to 29 should return 29 seconds in millis", 
                     29 * SECONDS_TO_MILLIS, field.set(120L, 29));
    }

    public void testSet_WithReadablePartialAndArray_ShouldUpdateFieldInArray() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        // Test setting same value
        int[] values = {10, 20, 30, 40};
        int[] expected = {10, 20, 30, 40};
        int[] result = field.set(new TimeOfDay(), 2, values, 30);
        assertTrue("Setting same value should not change array", Arrays.equals(expected, result));
        
        // Test setting different value
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 20, 29, 40};
        result = field.set(new TimeOfDay(), 2, values, 29);
        assertTrue("Should update field at specified index", Arrays.equals(expected, result));
        
        // Test setting invalid high value
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 20, 30, 40}; // Should remain unchanged
        try {
            field.set(new TimeOfDay(), 2, values, 60);
            fail("Should throw exception for value above maximum");
        } catch (IllegalArgumentException expectedException) {
            // Expected behavior
        }
        assertTrue("Array should remain unchanged after exception", Arrays.equals(expected, values));
        
        // Test setting invalid low value
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 20, 30, 40}; // Should remain unchanged
        try {
            field.set(new TimeOfDay(), 2, values, -1);
            fail("Should throw exception for negative value");
        } catch (IllegalArgumentException expectedException) {
            // Expected behavior
        }
        assertTrue("Array should remain unchanged after exception", Arrays.equals(expected, values));
    }

    public void testSet_WithStringValue_ShouldParseAndSetValue() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Setting '0' should return 0", 0, field.set(0L, "0", null));
        assertEquals("Setting '29' should return 29 seconds in millis", 
                     29 * SECONDS_TO_MILLIS, field.set(0L, "29", Locale.ENGLISH));
    }

    public void testSet_WithStringValueNoLocale_ShouldParseAndSetValue() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Setting '0' should return 0", 0, field.set(0L, "0"));
        assertEquals("Setting '29' should return 29 seconds in millis", 
                     29 * SECONDS_TO_MILLIS, field.set(0L, "29"));
    }

    public void testSet_WithReadablePartialAndStringArray_ShouldParseAndUpdateField() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        // Test setting same value as string
        int[] values = {10, 20, 30, 40};
        int[] expected = {10, 20, 30, 40};
        int[] result = field.set(new TimeOfDay(), 2, values, "30", null);
        assertTrue("Setting same value should not change array", Arrays.equals(expected, result));
        
        // Test setting different value as string
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 20, 29, 40};
        result = field.set(new TimeOfDay(), 2, values, "29", Locale.ENGLISH);
        assertTrue("Should parse and update field", Arrays.equals(expected, result));
        
        // Test setting invalid high value as string
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 20, 30, 40}; // Should remain unchanged
        try {
            field.set(new TimeOfDay(), 2, values, "60", null);
            fail("Should throw exception for string value above maximum");
        } catch (IllegalArgumentException expectedException) {
            // Expected behavior
        }
        assertTrue("Array should remain unchanged after exception", Arrays.equals(expected, values));
        
        // Test setting invalid low value as string
        values = new int[]{10, 20, 30, 40};
        expected = new int[]{10, 20, 30, 40}; // Should remain unchanged
        try {
            field.set(new TimeOfDay(), 2, values, "-1", null);
            fail("Should throw exception for negative string value");
        } catch (IllegalArgumentException expectedException) {
            // Expected behavior
        }
        assertTrue("Array should remain unchanged after exception", Arrays.equals(expected, values));
    }

    //-----------------------------------------------------------------------
    // Text Conversion Tests
    //-----------------------------------------------------------------------

    public void testConvertText_WithValidStrings_ShouldReturnParsedValues() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Should parse '0' to 0", 0, field.convertText("0", null));
        assertEquals("Should parse '29' to 29", 29, field.convertText("29", null));
    }

    public void testConvertText_WithInvalidString_ShouldThrowException() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        try {
            field.convertText("2A", null);
            fail("Should throw exception for non-numeric string");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    public void testConvertText_WithNullString_ShouldThrowException() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        try {
            field.convertText(null, null);
            fail("Should throw exception for null string");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    //-----------------------------------------------------------------------
    // Leap Year Tests
    //-----------------------------------------------------------------------

    public void testIsLeap_ShouldAlwaysReturnFalse() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        assertFalse("Field should never be leap", field.isLeap(0L));
    }

    public void testGetLeapAmount_ShouldAlwaysReturnZero() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        assertEquals("Leap amount should always be zero", 0, field.getLeapAmount(0L));
    }

    public void testGetLeapDurationField_ShouldReturnNull() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        assertNull("Leap duration field should be null", field.getLeapDurationField());
    }

    //-----------------------------------------------------------------------
    // Min/Max Value Tests
    //-----------------------------------------------------------------------

    public void testGetMinimumValue_ShouldReturnZero() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Minimum value should be 0", 0, field.getMinimumValue());
        assertEquals("Minimum value with instant should be 0", 0, field.getMinimumValue(0L));
        assertEquals("Minimum value with ReadablePartial should be 0", 
                     0, field.getMinimumValue(new TimeOfDay()));
        assertEquals("Minimum value with ReadablePartial and array should be 0", 
                     0, field.getMinimumValue(new TimeOfDay(), new int[4]));
    }

    public void testGetMaximumValue_ShouldReturnFiftyNine() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Maximum value should be 59", MAX_SECOND_VALUE, field.getMaximumValue());
        assertEquals("Maximum value with instant should be 59", 
                     MAX_SECOND_VALUE, field.getMaximumValue(0L));
        assertEquals("Maximum value with ReadablePartial should be 59", 
                     MAX_SECOND_VALUE, field.getMaximumValue(new TimeOfDay()));
        assertEquals("Maximum value with ReadablePartial and array should be 59", 
                     MAX_SECOND_VALUE, field.getMaximumValue(new TimeOfDay(), new int[4]));
    }

    //-----------------------------------------------------------------------
    // Text Length Tests
    //-----------------------------------------------------------------------

    public void testGetMaximumTextLength_WithDifferentMaxValues_ShouldReturnCorrectLength() {
        // Test default field (max value 59)
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        assertEquals("Two-digit max value should have length 2", 
                     2, field.getMaximumTextLength(Locale.ENGLISH));

        // Test single digit max value
        field = new TestableSecondOfMinuteField() {
            @Override
            public int getMaximumValue() { return 5; }
        };
        assertEquals("Single-digit max value should have length 1", 
                     1, field.getMaximumTextLength(Locale.ENGLISH));
        
        // Test three digit max value
        field = new TestableSecondOfMinuteField() {
            @Override
            public int getMaximumValue() { return 555; }
        };
        assertEquals("Three-digit max value should have length 3", 
                     3, field.getMaximumTextLength(Locale.ENGLISH));
        
        // Test four digit max value
        field = new TestableSecondOfMinuteField() {
            @Override
            public int getMaximumValue() { return 5555; }
        };
        assertEquals("Four-digit max value should have length 4", 
                     4, field.getMaximumTextLength(Locale.ENGLISH));
        
        // Test negative max value (edge case)
        field = new TestableSecondOfMinuteField() {
            @Override
            public int getMaximumValue() { return -1; }
        };
        assertEquals("Negative max value should default to length 2", 
                     2, field.getMaximumTextLength(Locale.ENGLISH));
    }

    public void testGetMaximumShortTextLength_ShouldMatchRegularTextLength() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        assertEquals("Short text length should match regular text length", 
                     2, field.getMaximumShortTextLength(Locale.ENGLISH));
    }

    //-----------------------------------------------------------------------
    // Rounding Tests
    //-----------------------------------------------------------------------

    public void testRoundFloor_ShouldRoundDownToNearestUnit() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        // Test negative values
        assertEquals("Should round -61 down to -120", -120L, field.roundFloor(-61L));
        assertEquals("Should keep -60 as -60", -60L, field.roundFloor(-60L));
        assertEquals("Should round -59 down to -60", -60L, field.roundFloor(-59L));
        assertEquals("Should round -1 down to -60", -60L, field.roundFloor(-1L));
        
        // Test positive values
        assertEquals("Should keep 0 as 0", 0L, field.roundFloor(0L));
        assertEquals("Should round 1 down to 0", 0L, field.roundFloor(1L));
        assertEquals("Should round 29 down to 0", 0L, field.roundFloor(29L));
        assertEquals("Should round 30 down to 0", 0L, field.roundFloor(30L));
        assertEquals("Should round 31 down to 0", 0L, field.roundFloor(31L));
        assertEquals("Should keep 60 as 60", 60L, field.roundFloor(60L));
    }

    public void testRoundCeiling_ShouldRoundUpToNearestUnit() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        // Test negative values
        assertEquals("Should round -61 up to -60", -60L, field.roundCeiling(-61L));
        assertEquals("Should keep -60 as -60", -60L, field.roundCeiling(-60L));
        assertEquals("Should round -59 up to 0", 0L, field.roundCeiling(-59L));
        assertEquals("Should round -1 up to 0", 0L, field.roundCeiling(-1L));
        
        // Test positive values
        assertEquals("Should keep 0 as 0", 0L, field.roundCeiling(0L));
        assertEquals("Should round 1 up to 60", 60L, field.roundCeiling(1L));
        assertEquals("Should round 29 up to 60", 60L, field.roundCeiling(29L));
        assertEquals("Should round 30 up to 60", 60L, field.roundCeiling(30L));
        assertEquals("Should round 31 up to 60", 60L, field.roundCeiling(31L));
        assertEquals("Should keep 60 as 60", 60L, field.roundCeiling(60L));
    }

    public void testRoundHalfFloor_ShouldRoundHalfValuesDown() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Should keep 0 as 0", 0L, field.roundHalfFloor(0L));
        assertEquals("Should round 29 down to 0", 0L, field.roundHalfFloor(29L));
        assertEquals("Should round 30 (half) down to 0", 0L, field.roundHalfFloor(30L));
        assertEquals("Should round 31 up to 60", 60L, field.roundHalfFloor(31L));
        assertEquals("Should keep 60 as 60", 60L, field.roundHalfFloor(60L));
    }

    public void testRoundHalfCeiling_ShouldRoundHalfValuesUp() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Should keep 0 as 0", 0L, field.roundHalfCeiling(0L));
        assertEquals("Should round 29 down to 0", 0L, field.roundHalfCeiling(29L));
        assertEquals("Should round 30 (half) up to 60", 60L, field.roundHalfCeiling(30L));
        assertEquals("Should round 31 up to 60", 60L, field.roundHalfCeiling(31L));
        assertEquals("Should keep 60 as 60", 60L, field.roundHalfCeiling(60L));
    }

    public void testRoundHalfEven_ShouldRoundHalfValuesToEvenUnits() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Should keep 0 as 0", 0L, field.roundHalfEven(0L));
        assertEquals("Should round 29 down to 0", 0L, field.roundHalfEven(29L));
        assertEquals("Should round 30 to even (0)", 0L, field.roundHalfEven(30L));
        assertEquals("Should round 31 up to 60", 60L, field.roundHalfEven(31L));
        assertEquals("Should keep 60 as 60", 60L, field.roundHalfEven(60L));
        assertEquals("Should round 89 down to 60", 60L, field.roundHalfEven(89L));
        assertEquals("Should round 90 to even (120)", 120L, field.roundHalfEven(90L));
        assertEquals("Should round 91 up to 120", 120L, field.roundHalfEven(91L));
    }

    public void testRemainder_ShouldReturnRemainderAfterRounding() {
        BaseDateTimeField field = new TestableSecondOfMinuteField();
        
        assertEquals("Remainder of 0 should be 0", 0L, field.remainder(0L));
        assertEquals("Remainder of 29 should be 29", 29L, field.remainder(29L));
        assertEquals("Remainder of 30 should be 30", 30L, field.remainder(30L));
        assertEquals("Remainder of 31 should be 31", 31L, field.remainder(31L));
        assertEquals("Remainder of 60 should be 0", 0L, field.remainder(60L));
    }

    //-----------------------------------------------------------------------
    // Mock Classes for Testing
    //-----------------------------------------------------------------------
    
    /**
     * Test implementation of PreciseDurationDateTimeField representing seconds of minute.
     * Uses a 60ms unit duration for easier testing (normally would be 1000ms).
     */
    static class TestableSecondOfMinuteField extends PreciseDurationDateTimeField {
        
        protected TestableSecondOfMinuteField() {
            super(DateTimeFieldType.secondOfMinute(),
                new MockCountingDurationField(DurationFieldType.seconds()));
        }
        
        protected TestableSecondOfMinuteField(DateTimeFieldType type, DurationField duration) {
            super(type, duration);
        }
        
        @Override
        public int get(long instant) {
            // Convert milliseconds to seconds using our test unit (60ms = 1 second)
            return (int) (instant / SECONDS_TO_MILLIS);
        }
        
        @Override
        public DurationField getRangeDurationField() {
            return new MockCountingDurationField(DurationFieldType.minutes());
        }
        
        @Override
        public int getMaximumValue() {
            return MAX_SECOND_VALUE;
        }
    }

    /**
     * Test implementation using real ISO chronology for more realistic testing.
     */
    static class TestableStandardField extends TestableSecondOfMinuteField {
        
        @Override
        public DurationField getDurationField() {
            return ISOChronology.getInstanceUTC().seconds();
        }
        
        @Override
        public DurationField getRangeDurationField() {
            return ISOChronology.getInstanceUTC().minutes();
        }
    }

    /**
     * Mock duration field that counts method calls and provides predictable behavior.
     */
    static class MockCountingDurationField extends BaseDurationField {
        static int add_int = 0;
        static int add_long = 0;
        static int difference_long = 0;
        
        protected MockCountingDurationField(DurationFieldType type) {
            super(type);
        }
        
        public static void resetCounters() {
            add_int = 0;
            add_long = 0;
            difference_long = 0;
        }
        
        @Override
        public boolean isPrecise() {
            return true;
        }
        
        @Override
        public long getUnitMillis() {
            return SECONDS_TO_MILLIS; // 60ms per unit for testing
        }
        
        @Override
        public long getValueAsLong(long duration, long instant) {
            return 0;
        }
        
        @Override
        public long getMillis(int value, long instant) {
            return 0;
        }
        
        @Override
        public long getMillis(long value, long instant) {
            return 0;
        }
        
        @Override
        public long add(long instant, int value) {
            add_int++;
            return instant + (value * SECONDS_TO_MILLIS);
        }
        
        @Override
        public long add(long instant, long value) {
            add_long++;
            return instant + (value * SECONDS_TO_MILLIS);
        }
        
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            difference_long++;
            return 30; // Fixed return value for testing
        }
    }

    /**
     * Mock duration field with zero unit millis (invalid for PreciseDurationDateTimeField).
     */
    static class MockZeroDurationField extends BaseDurationField {
        
        protected MockZeroDurationField(DurationFieldType type) {
            super(type);
        }
        
        @Override
        public boolean isPrecise() {
            return true;
        }
        
        @Override
        public long getUnitMillis() {
            return 0;  // Invalid: zero unit millis
        }
        
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
        @Override
        public long add(long instant, int value) { return 0; }
        @Override
        public long add(long instant, long value) { return 0; }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }

    /**
     * Mock duration field that is imprecise (invalid for PreciseDurationDateTimeField).
     */
    static class MockImpreciseDurationField extends BaseDurationField {
        
        protected MockImpreciseDurationField(DurationFieldType type) {
            super(type);
        }
        
        @Override
        public boolean isPrecise() {
            return false;  // Invalid: not precise
        }
        
        @Override
        public long getUnitMillis() { return 0; }
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
        @Override
        public long add(long instant, int value) { return 0; }
        @Override
        public long add(long instant, long value) { return 0; }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }
}