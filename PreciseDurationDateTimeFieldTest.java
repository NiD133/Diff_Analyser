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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 */
public class TestPreciseDurationDateTimeField {

    @Before
    public void resetStaticCounters() {
        MockCountingDurationField.resetCounters();
    }

    // Constructor Tests -----------------------------------------------------
    @Test
    public void testConstructor_ValidParameters_Success() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(DateTimeFieldType.secondOfMinute(), field.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullType_ThrowsException() {
        new MockPreciseDurationDateTimeField(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_ImpreciseDurationField_ThrowsException() {
        new MockPreciseDurationDateTimeField(
            DateTimeFieldType.minuteOfHour(),
            new MockImpreciseDurationField(DurationFieldType.minutes())
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_ZeroDurationField_ThrowsException() {
        new MockPreciseDurationDateTimeField(
            DateTimeFieldType.minuteOfHour(),
            new MockZeroDurationField(DurationFieldType.minutes())
        );
    }

    // Basic Property Tests --------------------------------------------------
    @Test
    public void testGetType_ReturnsCorrectType() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField(
            DateTimeFieldType.secondOfDay(), new MockCountingDurationField(DurationFieldType.minutes()));
        assertEquals(DateTimeFieldType.secondOfDay(), field.getType());
    }

    @Test
    public void testGetName_ReturnsCorrectName() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField(
            DateTimeFieldType.secondOfDay(), new MockCountingDurationField(DurationFieldType.minutes()));
        assertEquals("secondOfDay", field.getName());
    }

    @Test
    public void testToString_ReturnsDescriptiveString() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField(
            DateTimeFieldType.secondOfDay(), new MockCountingDurationField(DurationFieldType.minutes()));
        assertEquals("DateTimeField[secondOfDay]", field.toString());
    }

    @Test
    public void testIsSupported_AlwaysReturnsTrue() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertTrue(field.isSupported());
    }

    @Test
    public void testIsLenient_AlwaysReturnsFalse() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertFalse(field.isLenient());
    }

    // Value Retrieval Tests -------------------------------------------------
    @Test
    public void testGetValue_ReturnsCorrectValue() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.get(0));
        assertEquals(1, field.get(60));
        assertEquals(2, field.get(123));
    }

    // Text Representation Tests ---------------------------------------------
    @Test
    public void testGetAsText_WithLongAndLocale_ReturnsCorrectText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsText(60L * 29, Locale.ENGLISH));
        assertEquals("29", field.getAsText(60L * 29, null));
    }

    @Test
    public void testGetAsText_WithLong_ReturnsCorrectText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsText(60L * 29));
    }

    @Test
    public void testGetAsText_WithPartialAndValueAndLocale_ReturnsCorrectText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        TimeOfDay time = new TimeOfDay(12, 30, 40, 50);
        assertEquals("20", field.getAsText(time, 20, Locale.ENGLISH));
        assertEquals("20", field.getAsText(time, 20, null));
    }

    @Test
    public void testGetAsText_WithPartialAndLocale_ReturnsCorrectText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        TimeOfDay time = new TimeOfDay(12, 30, 40, 50);
        assertEquals("40", field.getAsText(time, Locale.ENGLISH));
        assertEquals("40", field.getAsText(time, null));
    }

    @Test
    public void testGetAsText_WithIntAndLocale_ReturnsCorrectText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("80", field.getAsText(80, Locale.ENGLISH));
        assertEquals("80", field.getAsText(80, null));
    }

    // Short Text Representation Tests ---------------------------------------
    @Test
    public void testGetAsShortText_WithLongAndLocale_ReturnsCorrectText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsShortText(60L * 29, Locale.ENGLISH));
        assertEquals("29", field.getAsShortText(60L * 29, null));
    }

    @Test
    public void testGetAsShortText_WithLong_ReturnsCorrectText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsShortText(60L * 29));
    }

    @Test
    public void testGetAsShortText_WithPartialAndValueAndLocale_ReturnsCorrectText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        TimeOfDay time = new TimeOfDay(12, 30, 40, 50);
        assertEquals("20", field.getAsShortText(time, 20, Locale.ENGLISH));
        assertEquals("20", field.getAsShortText(time, 20, null));
    }

    @Test
    public void testGetAsShortText_WithPartialAndLocale_ReturnsCorrectText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        TimeOfDay time = new TimeOfDay(12, 30, 40, 50);
        assertEquals("40", field.getAsShortText(time, Locale.ENGLISH));
        assertEquals("40", field.getAsShortText(time, null));
    }

    @Test
    public void testGetAsShortText_WithIntAndLocale_ReturnsCorrectText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("80", field.getAsShortText(80, Locale.ENGLISH));
        assertEquals("80", field.getAsShortText(80, null));
    }

    // Arithmetic Operations Tests -------------------------------------------
    @Test
    public void testAddLongInt_DelegatesToDurationField() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(61, field.add(1L, 1));
        assertEquals(1, MockCountingDurationField.add_int);
    }

    @Test
    public void testAddLongLong_DelegatesToDurationField() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(61, field.add(1L, 1L));
        assertEquals(1, MockCountingDurationField.add_long);
    }

    @Test
    public void testAddToPartial_WithValidValue_UpdatesCorrectly() {
        BaseDateTimeField field = new MockStandardBaseDateTimeField();
        TimeOfDay time = new TimeOfDay();
        
        // No change
        assertArrayEquals(new int[] {10, 20, 30, 40}, 
            field.add(time, 2, new int[] {10, 20, 30, 40}, 0));
        
        // Increment within range
        assertArrayEquals(new int[] {10, 20, 31, 40}, 
            field.add(time, 2, new int[] {10, 20, 30, 40}, 1));
        
        // Increment with rollover
        assertArrayEquals(new int[] {10, 21, 0, 40}, 
            field.add(time, 2, new int[] {10, 20, 30, 40}, 30));
        
        // Decrement within range
        assertArrayEquals(new int[] {10, 20, 29, 40}, 
            field.add(time, 2, new int[] {10, 20, 30, 40}, -1));
        
        // Decrement with rollover
        assertArrayEquals(new int[] {10, 19, 59, 40}, 
            field.add(time, 2, new int[] {10, 20, 30, 40}, -31));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToPartial_WithOverflow_ThrowsException() {
        BaseDateTimeField field = new MockStandardBaseDateTimeField();
        field.add(new TimeOfDay(), 2, new int[] {23, 59, 30, 40}, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToPartial_WithUnderflow_ThrowsException() {
        BaseDateTimeField field = new MockStandardBaseDateTimeField();
        field.add(new TimeOfDay(), 2, new int[] {0, 0, 30, 40}, -31);
    }

    // Field Wrapping Tests --------------------------------------------------
    @Test
    public void testAddWrapField_WithLongValue_HandlesRollover() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(29 * 60L, field.addWrapField(60L * 29, 0));
        assertEquals(59 * 60L, field.addWrapField(60L * 29, 30));
        assertEquals(0 * 60L, field.addWrapField(60L * 29, 31));
    }

    @Test
    public void testAddWrapFieldToPartial_WithValidValue_UpdatesCorrectly() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        TimeOfDay time = new TimeOfDay();
        
        // No change
        assertArrayEquals(new int[] {10, 20, 30, 40}, 
            field.addWrapField(time, 2, new int[] {10, 20, 30, 40}, 0));
        
        // Add within range
        assertArrayEquals(new int[] {10, 20, 59, 40}, 
            field.addWrapField(time, 2, new int[] {10, 20, 30, 40}, 29));
        
        // Wrap around boundary
        assertArrayEquals(new int[] {10, 20, 0, 40}, 
            field.addWrapField(time, 2, new int[] {10, 20, 30, 40}, 30));
        
        // Wrap around with positive overflow
        assertArrayEquals(new int[] {10, 20, 1, 40}, 
            field.addWrapField(time, 2, new int[] {10, 20, 30, 40}, 31));
    }

    // Difference Calculation Tests ------------------------------------------
    @Test
    public void testGetDifference_DelegatesToDurationField() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(30, field.getDifference(0L, 0L));
        assertEquals(1, MockCountingDurationField.difference_long);
    }

    @Test
    public void testGetDifferenceAsLong_DelegatesToDurationField() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(30, field.getDifferenceAsLong(0L, 0L));
        assertEquals(1, MockCountingDurationField.difference_long);
    }

    // Field Setting Tests ---------------------------------------------------
    @Test
    public void testSetLongValue_WithValidValue_UpdatesCorrectly() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.set(120L, 0));
        assertEquals(29 * 60, field.set(120L, 29));
    }

    @Test
    public void testSetPartialValue_WithValidValue_UpdatesCorrectly() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        TimeOfDay time = new TimeOfDay();
        
        // Set to same value
        assertArrayEquals(new int[] {10, 20, 30, 40}, 
            field.set(time, 2, new int[] {10, 20, 30, 40}, 30));
        
        // Set to new valid value
        assertArrayEquals(new int[] {10, 20, 29, 40}, 
            field.set(time, 2, new int[] {10, 20, 30, 40}, 29));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPartialValue_WithOverflow_ThrowsException() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        field.set(new TimeOfDay(), 2, new int[] {10, 20, 30, 40}, 60);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPartialValue_WithUnderflow_ThrowsException() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        field.set(new TimeOfDay(), 2, new int[] {10, 20, 30, 40}, -1);
    }

    @Test
    public void testSetFromText_WithLongAndString_UpdatesCorrectly() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.set(0L, "0"));
        assertEquals(29 * 60, field.set(0L, "29"));
    }

    @Test
    public void testSetPartialFromText_WithValidText_UpdatesCorrectly() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        TimeOfDay time = new TimeOfDay();
        
        // Set with same value
        assertArrayEquals(new int[] {10, 20, 30, 40}, 
            field.set(time, 2, new int[] {10, 20, 30, 40}, "30", null));
        
        // Set with new valid value
        assertArrayEquals(new int[] {10, 20, 29, 40}, 
            field.set(time, 2, new int[] {10, 20, 30, 40}, "29", Locale.ENGLISH));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPartialFromText_WithInvalidText_ThrowsException() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        field.set(new TimeOfDay(), 2, new int[] {10, 20, 30, 40}, "60", null);
    }

    @Test
    public void testConvertText_WithValidInput_ReturnsValue() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.convertText("0", null));
        assertEquals(29, field.convertText("29", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertText_WithNonNumericInput_ThrowsException() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        field.convertText("2A", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertText_WithNullInput_ThrowsException() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        field.convertText(null, null);
    }

    // Leap Year Handling Tests ----------------------------------------------
    @Test
    public void testLeapYearHandling_AlwaysReturnsFalse() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertFalse("Field should never be leap", field.isLeap(0L));
        assertEquals("Leap amount should always be zero", 0, field.getLeapAmount(0L));
        assertNull("Leap duration field should be null", field.getLeapDurationField());
    }

    // Value Range Tests -----------------------------------------------------
    @Test
    public void testMinimumValue_AlwaysReturnsZero() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.getMinimumValue());
        assertEquals(0, field.getMinimumValue(0L));
        assertEquals(0, field.getMinimumValue(new TimeOfDay()));
        assertEquals(0, field.getMinimumValue(new TimeOfDay(), new int[4]));
    }

    @Test
    public void testMaximumValue_AlwaysReturns59() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(59, field.getMaximumValue());
        assertEquals(59, field.getMaximumValue(0L));
        assertEquals(59, field.getMaximumValue(new TimeOfDay()));
        assertEquals(59, field.getMaximumValue(new TimeOfDay(), new int[4]));
    }

    @Test
    public void testMaximumTextLength_VariesWithMaximumValue() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(2, field.getMaximumTextLength(Locale.ENGLISH));

        field = createFieldWithMaxValue(5);
        assertEquals(1, field.getMaximumTextLength(Locale.ENGLISH));
        
        field = createFieldWithMaxValue(555);
        assertEquals(3, field.getMaximumTextLength(Locale.ENGLISH));
        
        field = createFieldWithMaxValue(5555);
        assertEquals(4, field.getMaximumTextLength(Locale.ENGLISH));
        
        field = createFieldWithMaxValue(-1);
        assertEquals(2, field.getMaximumTextLength(Locale.ENGLISH));
    }

    @Test
    public void testMaximumShortTextLength_AlwaysReturnsTwo() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(2, field.getMaximumShortTextLength(Locale.ENGLISH));
    }

    // Rounding Operation Tests ----------------------------------------------
    @Test
    public void testRoundFloor_ReturnsCorrectValues() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(-120L, field.roundFloor(-61L));
        assertEquals(-60L, field.roundFloor(-60L));
        assertEquals(-60L, field.roundFloor(-59L));
        assertEquals(-60L, field.roundFloor(-1L));
        assertEquals(0L, field.roundFloor(0L));
        assertEquals(0L, field.roundFloor(1L));
        assertEquals(0L, field.roundFloor(29L));
        assertEquals(0L, field.roundFloor(30L));
        assertEquals(0L, field.roundFloor(31L));
        assertEquals(60L, field.roundFloor(60L));
    }

    @Test
    public void testRoundCeiling_ReturnsCorrectValues() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(-60L, field.roundCeiling(-61L));
        assertEquals(-60L, field.roundCeiling(-60L));
        assertEquals(0L, field.roundCeiling(-59L));
        assertEquals(0L, field.roundCeiling(-1L));
        assertEquals(0L, field.roundCeiling(0L));
        assertEquals(60L, field.roundCeiling(1L));
        assertEquals(60L, field.roundCeiling(29L));
        assertEquals(60L, field.roundCeiling(30L));
        assertEquals(60L, field.roundCeiling(31L));
        assertEquals(60L, field.roundCeiling(60L));
    }

    @Test
    public void testRoundHalfFloor_ReturnsCorrectValues() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.roundHalfFloor(0L));
        assertEquals(0L, field.roundHalfFloor(29L));
        assertEquals(0L, field.roundHalfFloor(30L));
        assertEquals(60L, field.roundHalfFloor(31L));
        assertEquals(60L, field.roundHalfFloor(60L));
    }

    @Test
    public void testRoundHalfCeiling_ReturnsCorrectValues() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.roundHalfCeiling(0L));
        assertEquals(0L, field.roundHalfCeiling(29L));
        assertEquals(60L, field.roundHalfCeiling(30L));
        assertEquals(60L, field.roundHalfCeiling(31L));
        assertEquals(60L, field.roundHalfCeiling(60L));
    }

    @Test
    public void testRoundHalfEven_ReturnsCorrectValues() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.roundHalfEven(0L));
        assertEquals(0L, field.roundHalfEven(29L));
        assertEquals(0L, field.roundHalfEven(30L));  // Exact half, rounds to even (0)
        assertEquals(60L, field.roundHalfEven(31L));
        assertEquals(60L, field.roundHalfEven(60L));
        assertEquals(60L, field.roundHalfEven(89L));
        assertEquals(120L, field.roundHalfEven(90L)); // Exact half, rounds to even (120)
        assertEquals(120L, field.roundHalfEven(91L));
    }

    @Test
    public void testRemainder_ReturnsCorrectValues() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.remainder(0L));
        assertEquals(29L, field.remainder(29L));
        assertEquals(30L, field.remainder(30L));
        assertEquals(31L, field.remainder(31L));
        assertEquals(0L, field.remainder(60L));
    }

    // Helper Methods --------------------------------------------------------
    private void assertArrayEquals(int[] expected, int[] actual) {
        assertTrue(
            "Arrays differ: expected=" + Arrays.toString(expected) + ", actual=" + Arrays.toString(actual),
            Arrays.equals(expected, actual)
        );
    }

    private BaseDateTimeField createFieldWithMaxValue(final int maxValue) {
        return new MockPreciseDurationDateTimeField() {
            @Override
            public int getMaximumValue() {
                return maxValue;
            }
        };
    }

    // Inner Classes ---------------------------------------------------------
    static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {
        protected MockPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(),
                new MockCountingDurationField(DurationFieldType.seconds()));
        }
        protected MockPreciseDurationDateTimeField(DateTimeFieldType type, DurationField dur) {
            super(type, dur);
        }
        @Override
        public int get(long instant) {
            return (int) (instant / 60L);
        }
        @Override
        public DurationField getRangeDurationField() {
            return new MockCountingDurationField(DurationFieldType.minutes());
        }
        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    static class MockStandardBaseDateTimeField extends MockPreciseDurationDateTimeField {
        protected MockStandardBaseDateTimeField() {
            super();
        }
        @Override
        public DurationField getDurationField() {
            return ISOChronology.getInstanceUTC().seconds();
        }
        @Override
        public DurationField getRangeDurationField() {
            return ISOChronology.getInstanceUTC().minutes();
        }
    }

    static class MockCountingDurationField extends BaseDurationField {
        static int add_int = 0;
        static int add_long = 0;
        static int difference_long = 0;
        
        static void resetCounters() {
            add_int = 0;
            add_long = 0;
            difference_long = 0;
        }
        
        protected MockCountingDurationField(DurationFieldType type) {
            super(type);
        }
        @Override
        public boolean isPrecise() {
            return true;
        }
        @Override
        public long getUnitMillis() {
            return 60;
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
            return instant + (value * 60L);
        }
        @Override
        public long add(long instant, long value) {
            add_long++;
            return instant + (value * 60L);
        }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            difference_long++;
            return 30;
        }
    }

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
            return 0;
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
            return 0;
        }
        @Override
        public long add(long instant, long value) {
            return 0;
        }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0;
        }
    }

    static class MockImpreciseDurationField extends BaseDurationField {
        protected MockImpreciseDurationField(DurationFieldType type) {
            super(type);
        }
        @Override
        public boolean isPrecise() {
            return false;
        }
        @Override
        public long getUnitMillis() {
            return 0;
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
            return 0;
        }
        @Override
        public long add(long instant, long value) {
            return 0;
        }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0;
        }
    }
}