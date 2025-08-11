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
 * Unit tests for the abstract PreciseDurationDateTimeField class.
 *
 * <p>This class uses mock implementations to test the functionality of the abstract base class.
 */
public class TestPreciseDurationDateTimeField extends TestCase {

    private MockPreciseDurationDateTimeField secondOfMinuteField;
    private MockCountingDurationField mockSecondsDurationField;

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
        mockSecondsDurationField = new MockCountingDurationField(DurationFieldType.seconds());
        secondOfMinuteField = new MockPreciseDurationDateTimeField(
            DateTimeFieldType.secondOfMinute(),
            mockSecondsDurationField
        );
    }

    //-----------------------------------------------------------------------
    // Constructor
    //-----------------------------------------------------------------------

    public void testConstructor_withNullType_throwsIllegalArgumentException() {
        try {
            new MockPreciseDurationDateTimeField(null, mockSecondsDurationField);
            fail("Constructor should have thrown an exception for null type");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    public void testConstructor_withImpreciseUnit_throwsIllegalArgumentException() {
        try {
            DurationField impreciseField = new MockImpreciseDurationField(DurationFieldType.minutes());
            new MockPreciseDurationDateTimeField(DateTimeFieldType.minuteOfHour(), impreciseField);
            fail("Constructor should have thrown an exception for an imprecise duration field");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    public void testConstructor_withZeroUnitMillis_throwsIllegalArgumentException() {
        try {
            DurationField zeroUnitField = new MockZeroDurationField(DurationFieldType.minutes());
            new MockPreciseDurationDateTimeField(DateTimeFieldType.minuteOfHour(), zeroUnitField);
            fail("Constructor should have thrown an exception for a zero-unit duration field");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    //-----------------------------------------------------------------------
    // Getters
    //-----------------------------------------------------------------------

    public void testGetters_shouldReturnCorrectValues() {
        assertEquals(DateTimeFieldType.secondOfMinute(), secondOfMinuteField.getType());
        assertEquals("secondOfMinute", secondOfMinuteField.getName());
        assertEquals("DateTimeField[secondOfMinute]", secondOfMinuteField.toString());
        assertTrue(secondOfMinuteField.isSupported());
        assertFalse(secondOfMinuteField.isLenient());
    }

    public void testGet_shouldReturnFieldValue() {
        assertEquals(0, secondOfMinuteField.get(0));
        assertEquals(1, secondOfMinuteField.get(60));
        assertEquals(2, secondOfMinuteField.get(123));
    }

    //-----------------------------------------------------------------------
    // GetAsText
    //-----------------------------------------------------------------------

    public void testGetAsText_forValue_shouldReturnStringValue() {
        assertEquals("80", secondOfMinuteField.getAsText(80, Locale.ENGLISH));
        assertEquals("80", secondOfMinuteField.getAsText(80, null));
    }

    public void testGetAsText_forInstant_shouldCalculateValueAndReturnString() {
        assertEquals("29", secondOfMinuteField.getAsText(60L * 29));
        assertEquals("29", secondOfMinuteField.getAsText(60L * 29, Locale.ENGLISH));
    }

    public void testGetAsText_forPartial_shouldReturnFieldValueAsString() {
        TimeOfDay partial = new TimeOfDay(12, 30, 40, 50);
        assertEquals("40", secondOfMinuteField.getAsText(partial, Locale.ENGLISH));
    }

    //-----------------------------------------------------------------------
    // Add
    //-----------------------------------------------------------------------

    public void testAdd_longInt_shouldDelegateToDurationField() {
        // Act
        long result = secondOfMinuteField.add(1L, 1);

        // Assert
        assertEquals("Result should be correctly added", 61L, result);
        assertEquals("add(long, int) should be called once", 1, mockSecondsDurationField.addIntCallCount);
    }

    public void testAdd_longLong_shouldDelegateToDurationField() {
        // Act
        long result = secondOfMinuteField.add(1L, 1L);

        // Assert
        assertEquals("Result should be correctly added", 61L, result);
        assertEquals("add(long, long) should be called once", 1, mockSecondsDurationField.addLongCallCount);
    }

    public void testAdd_toPartial_withPositiveAmount_shouldCascadeToNextField() {
        // Arrange
        MockStandardBaseDateTimeField field = new MockStandardBaseDateTimeField();
        int[] values = {10, 20, 30, 40}; // 10:20:30.40
        int amountToAdd = 30; // Adding 30 seconds to 30 seconds should yield 1 minute 0 seconds

        // Act
        int[] result = field.add(new TimeOfDay(), 2, values, amountToAdd);

        // Assert
        int[] expected = {10, 21, 0, 40}; // 10:21:00.40
        assertTrue("Resulting array should match expected", Arrays.equals(expected, result));
    }

    public void testAdd_toPartial_withNegativeAmount_shouldCascadeToNextField() {
        // Arrange
        MockStandardBaseDateTimeField field = new MockStandardBaseDateTimeField();
        int[] values = {10, 20, 30, 40}; // 10:20:30.40
        int amountToSubtract = -31; // Subtracting 31 seconds from 30 seconds should borrow 1 minute

        // Act
        int[] result = field.add(new TimeOfDay(), 2, values, amountToSubtract);

        // Assert
        int[] expected = {10, 19, 59, 40}; // 10:19:59.40
        assertTrue("Resulting array should match expected", Arrays.equals(expected, result));
    }

    public void testAdd_toPartial_withAmountExceedingUpperLimit_throwsException() {
        // Arrange
        MockStandardBaseDateTimeField field = new MockStandardBaseDateTimeField();
        int[] values = {23, 59, 30, 40}; // 23:59:30.40

        // Act
        try {
            field.add(new TimeOfDay(), 2, values, 30); // Adding 30 seconds would overflow the day
            fail("add should have thrown an exception for value out of range");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    //-----------------------------------------------------------------------
    // AddWrapField
    //-----------------------------------------------------------------------

    public void testAddWrapField_long_shouldWrapAround() {
        assertEquals(29 * 60L, secondOfMinuteField.addWrapField(60L * 29, 0));
        assertEquals(59 * 60L, secondOfMinuteField.addWrapField(60L * 29, 30));
        assertEquals(0 * 60L, secondOfMinuteField.addWrapField(60L * 29, 31)); // Wraps from 59 to 0
    }

    public void testAddWrapField_toPartial_shouldWrapAround() {
        // Arrange
        int[] values = {10, 20, 30, 40};

        // Act & Assert
        int[] result1 = secondOfMinuteField.addWrapField(new TimeOfDay(), 2, values, 29);
        assertTrue(Arrays.equals(new int[]{10, 20, 59, 40}, result1));

        int[] result2 = secondOfMinuteField.addWrapField(new TimeOfDay(), 2, values, 30);
        assertTrue(Arrays.equals(new int[]{10, 20, 0, 40}, result2));

        int[] result3 = secondOfMinuteField.addWrapField(new TimeOfDay(), 2, values, 31);
        assertTrue(Arrays.equals(new int[]{10, 20, 1, 40}, result3));
    }

    //-----------------------------------------------------------------------
    // GetDifference
    //-----------------------------------------------------------------------

    public void testGetDifference_shouldDelegateToDurationField() {
        // Act
        int result = secondOfMinuteField.getDifference(1000L, 0L);

        // Assert
        assertEquals(30, result); // Mock returns a fixed value
        assertEquals("getDifferenceAsLong should be called once", 1, mockSecondsDurationField.getDifferenceCallCount);
    }

    public void testGetDifferenceAsLong_shouldDelegateToDurationField() {
        // Act
        long result = secondOfMinuteField.getDifferenceAsLong(1000L, 0L);

        // Assert
        assertEquals(30L, result); // Mock returns a fixed value
        assertEquals("getDifferenceAsLong should be called once", 1, mockSecondsDurationField.getDifferenceCallCount);
    }

    //-----------------------------------------------------------------------
    // Set
    //-----------------------------------------------------------------------

    public void testSet_long_shouldReturnInstantWithNewValue() {
        assertEquals(0L, secondOfMinuteField.set(120L, 0));
        assertEquals(29 * 60L, secondOfMinuteField.set(120L, 29));
    }

    public void testSet_inPartial_withValidValue_shouldUpdateField() {
        // Arrange
        int[] values = {10, 20, 30, 40};
        int[] expected = {10, 20, 29, 40};

        // Act
        int[] result = secondOfMinuteField.set(new TimeOfDay(), 2, values, 29);

        // Assert
        assertTrue(Arrays.equals(expected, result));
    }

    public void testSet_inPartial_withValueTooLarge_throwsException() {
        // Arrange
        int[] values = {10, 20, 30, 40};
        int[] originalValues = values.clone();

        // Act
        try {
            secondOfMinuteField.set(new TimeOfDay(), 2, values, 60);
            fail("set should have thrown an exception for value out of range");
        } catch (IllegalArgumentException ex) {
            // Assert
            assertTrue("Original values array should not be modified", Arrays.equals(originalValues, values));
        }
    }

    public void testSet_inPartial_withValueTooSmall_throwsException() {
        // Arrange
        int[] values = {10, 20, 30, 40};
        int[] originalValues = values.clone();

        // Act
        try {
            secondOfMinuteField.set(new TimeOfDay(), 2, values, -1);
            fail("set should have thrown an exception for value out of range");
        } catch (IllegalArgumentException ex) {
            // Assert
            assertTrue("Original values array should not be modified", Arrays.equals(originalValues, values));
        }
    }

    public void testSet_longFromString_shouldParseAndSet() {
        assertEquals(0, secondOfMinuteField.set(0L, "0", null));
        assertEquals(29 * 60, secondOfMinuteField.set(0L, "29", Locale.ENGLISH));
    }

    //-----------------------------------------------------------------------
    // Rounding
    //-----------------------------------------------------------------------

    public void testRoundFloor_shouldRoundDownToNearestUnit() {
        assertEquals(-120L, secondOfMinuteField.roundFloor(-61L));
        assertEquals(-60L, secondOfMinuteField.roundFloor(-60L));
        assertEquals(-60L, secondOfMinuteField.roundFloor(-1L));
        assertEquals(0L, secondOfMinuteField.roundFloor(0L));
        assertEquals(0L, secondOfMinuteField.roundFloor(59L));
        assertEquals(60L, secondOfMinuteField.roundFloor(60L));
    }

    public void testRoundCeiling_shouldRoundUpToNearestUnit() {
        assertEquals(-60L, secondOfMinuteField.roundCeiling(-61L));
        assertEquals(-60L, secondOfMinuteField.roundCeiling(-60L));
        assertEquals(0L, secondOfMinuteField.roundCeiling(-59L));
        assertEquals(0L, secondOfMinuteField.roundCeiling(0L));
        assertEquals(60L, secondOfMinuteField.roundCeiling(1L));
        assertEquals(60L, secondOfMinuteField.roundCeiling(60L));
    }

    public void testRoundHalfFloor_shouldRoundToNearestUnitFloorOnTie() {
        assertEquals(0L, secondOfMinuteField.roundHalfFloor(29L)); // Rounds down
        assertEquals(0L, secondOfMinuteField.roundHalfFloor(30L)); // Tie, rounds down
        assertEquals(60L, secondOfMinuteField.roundHalfFloor(31L)); // Rounds up
    }

    public void testRoundHalfCeiling_shouldRoundToNearestUnitCeilingOnTie() {
        assertEquals(0L, secondOfMinuteField.roundHalfCeiling(29L)); // Rounds down
        assertEquals(60L, secondOfMinuteField.roundHalfCeiling(30L)); // Tie, rounds up
        assertEquals(60L, secondOfMinuteField.roundHalfCeiling(31L)); // Rounds up
    }

    public void testRoundHalfEven_shouldRoundToNearestEvenUnitOnTie() {
        assertEquals(0L, secondOfMinuteField.roundHalfEven(30L));   // 30/60 = 0.5, rounds to 0 (even)
        assertEquals(120L, secondOfMinuteField.roundHalfEven(90L)); // 90/60 = 1.5, rounds to 2*60 (even)
    }

    public void testRemainder_shouldReturnRemainderFromDivision() {
        assertEquals(0L, secondOfMinuteField.remainder(0L));
        assertEquals(29L, secondOfMinuteField.remainder(29L));
        assertEquals(30L, secondOfMinuteField.remainder(30L));
        assertEquals(0L, secondOfMinuteField.remainder(60L));
    }

    //-----------------------------------------------------------------------
    // Min/Max values
    //-----------------------------------------------------------------------

    public void testGetMinimumValue_shouldReturnZero() {
        assertEquals(0, secondOfMinuteField.getMinimumValue());
        assertEquals(0, secondOfMinuteField.getMinimumValue(1234L));
        assertEquals(0, secondOfMinuteField.getMinimumValue(new TimeOfDay()));
    }

    public void testGetMaximumValue_shouldReturn59() {
        assertEquals(59, secondOfMinuteField.getMaximumValue());
        assertEquals(59, secondOfMinuteField.getMaximumValue(1234L));
        assertEquals(59, secondOfMinuteField.getMaximumValue(new TimeOfDay()));
    }

    public void testGetMaximumTextLength_shouldReturnCorrectLength() {
        assertEquals(2, secondOfMinuteField.getMaximumTextLength(Locale.ENGLISH));
    }

    //-----------------------------------------------------------------------
    // Mock Implementations
    //-----------------------------------------------------------------------

    static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {
        protected MockPreciseDurationDateTimeField(DateTimeFieldType type, DurationField dur) {
            super(type, dur);
        }

        @Override
        public int get(long instant) {
            return (int) (instant / getUnitMillis());
        }

        @Override
        public DurationField getRangeDurationField() {
            return new MockCountingDurationField(DurationFieldType.minutes());
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }

        // Methods made concrete for testing
        @Override
        public boolean isLenient() { return false; }
        @Override
        public long roundFloor(long instant) { return super.roundFloor(instant); }
        @Override
        public long roundCeiling(long instant) { return super.roundCeiling(instant); }
        @Override
        public long remainder(long instant) { return super.remainder(instant); }
    }

    static class MockStandardBaseDateTimeField extends MockPreciseDurationDateTimeField {
        protected MockStandardBaseDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), ISOChronology.getInstanceUTC().seconds());
        }

        @Override
        public DurationField getRangeDurationField() {
            return ISOChronology.getInstanceUTC().minutes();
        }
    }

    static class MockCountingDurationField extends BaseDurationField {
        int addIntCallCount = 0;
        int addLongCallCount = 0;
        int getDifferenceCallCount = 0;

        protected MockCountingDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() { return true; }
        @Override
        public long getUnitMillis() { return 60; }
        @Override
        public long add(long instant, int value) {
            addIntCallCount++;
            return instant + (value * getUnitMillis());
        }
        @Override
        public long add(long instant, long value) {
            addLongCallCount++;
            return instant + (value * getUnitMillis());
        }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            getDifferenceCallCount++;
            return 30; // Return a fixed value for predictable testing
        }
        
        // Unused methods
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
    }

    static class MockZeroDurationField extends BaseDurationField {
        protected MockZeroDurationField(DurationFieldType type) { super(type); }
        @Override
        public boolean isPrecise() { return true; }
        @Override
        public long getUnitMillis() { return 0; } // The critical part for the test
        // Unused methods
        @Override
        public long add(long instant, int value) { return 0; }
        @Override
        public long add(long instant, long value) { return 0; }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
    }

    static class MockImpreciseDurationField extends BaseDurationField {
        protected MockImpreciseDurationField(DurationFieldType type) { super(type); }
        @Override
        public boolean isPrecise() { return false; } // The critical part for the test
        // Unused methods
        @Override
        public long getUnitMillis() { return 60; }
        @Override
        public long add(long instant, int value) { return 0; }
        @Override
        public long add(long instant, long value) { return 0; }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
        @Override
        public long getValueAsLong(long duration, long instant) { return 0; }
        @Override
        public long getMillis(int value, long instant) { return 0; }
        @Override
        public long getMillis(long value, long instant) { return 0; }
    }
}