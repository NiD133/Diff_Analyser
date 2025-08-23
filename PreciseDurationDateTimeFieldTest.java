package org.joda.time.field;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

/**
 * Tests for PreciseDurationDateTimeField behavior using simple in-memory mocks.
 * The mock field represents "seconds of minute" with a precise unit of 60 ms.
 *
 * Notes for future maintainers:
 * - UNIT_MILLIS is set to 60 to make arithmetic easy to see in assertions.
 * - SECOND_INDEX = 2 corresponds to the "second" field in TimeOfDay's int[].
 * - MockCountingDurationField tracks how many times add/difference methods are called.
 */
public class TestPreciseDurationDateTimeField {

    private static final long UNIT_MILLIS = 60L;
    private static final Locale EN = Locale.ENGLISH;
    private static final int SECOND_INDEX = 2; // TimeOfDay fields: [hour, minute, second, millis]
    private static final TimeOfDay TOD_12_30_40_50 = new TimeOfDay(12, 30, 40, 50);

    // ---------------------------------------------------------------------
    // Constructor, basic identity, and flags
    // ---------------------------------------------------------------------

    @Test
    public void constructor_validAndInvalidArguments() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(DateTimeFieldType.secondOfMinute(), field.getType());

        assertThrows(IllegalArgumentException.class,
                () -> new MockPreciseDurationDateTimeField(null, null));

        assertThrows(IllegalArgumentException.class,
                () -> new MockPreciseDurationDateTimeField(
                        DateTimeFieldType.minuteOfHour(),
                        new MockImpreciseDurationField(DurationFieldType.minutes())));

        assertThrows(IllegalArgumentException.class,
                () -> new MockPreciseDurationDateTimeField(
                        DateTimeFieldType.minuteOfHour(),
                        new MockZeroDurationField(DurationFieldType.minutes())));
    }

    @Test
    public void getType_returnsCtorType() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField(
                DateTimeFieldType.secondOfDay(),
                new MockCountingDurationField(DurationFieldType.minutes()));
        assertEquals(DateTimeFieldType.secondOfDay(), field.getType());
    }

    @Test
    public void getName_usesFieldTypeName() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField(
                DateTimeFieldType.secondOfDay(),
                new MockCountingDurationField(DurationFieldType.minutes()));
        assertEquals("secondOfDay", field.getName());
    }

    @Test
    public void toString_containsFieldType() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField(
                DateTimeFieldType.secondOfDay(),
                new MockCountingDurationField(DurationFieldType.minutes()));
        assertEquals("DateTimeField[secondOfDay]", field.toString());
    }

    @Test
    public void isSupported_alwaysTrue() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertTrue(field.isSupported());
    }

    @Test
    public void isLenient_falseByDefault() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertFalse(field.isLenient());
    }

    @Test
    public void get_returnsIntegerDivisionByUnit() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.get(0));
        assertEquals(1, field.get(UNIT_MILLIS));
        assertEquals(2, field.get(UNIT_MILLIS + 63)); // 123 / 60 = 2
    }

    // ---------------------------------------------------------------------
    // Text/short text formatting
    // ---------------------------------------------------------------------

    @Test
    public void getAsText_withInstantAndLocale_formatsNumber() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsText(UNIT_MILLIS * 29, EN));
        assertEquals("29", field.getAsText(UNIT_MILLIS * 29, null));
    }

    @Test
    public void getAsText_withInstant_formatsNumber() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsText(UNIT_MILLIS * 29));
    }

    @Test
    public void getAsText_withPartialValueAndLocale_formatsNumber() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("20", field.getAsText(TOD_12_30_40_50, 20, EN));
        assertEquals("20", field.getAsText(TOD_12_30_40_50, 20, null));
    }

    @Test
    public void getAsText_withPartialAndLocale_formatsCurrentField() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("40", field.getAsText(TOD_12_30_40_50, EN));
        assertEquals("40", field.getAsText(TOD_12_30_40_50, null));
    }

    @Test
    public void getAsText_withValueAndLocale_formatsNumber() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("80", field.getAsText(80, EN));
        assertEquals("80", field.getAsText(80, null));
    }

    @Test
    public void getAsShortText_withInstantAndLocale_formatsNumber() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsShortText(UNIT_MILLIS * 29, EN));
        assertEquals("29", field.getAsShortText(UNIT_MILLIS * 29, null));
    }

    @Test
    public void getAsShortText_withInstant_formatsNumber() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("29", field.getAsShortText(UNIT_MILLIS * 29));
    }

    @Test
    public void getAsShortText_withPartialValueAndLocale_formatsNumber() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("20", field.getAsShortText(TOD_12_30_40_50, 20, EN));
        assertEquals("20", field.getAsShortText(TOD_12_30_40_50, 20, null));
    }

    @Test
    public void getAsShortText_withPartialAndLocale_formatsCurrentField() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("40", field.getAsShortText(TOD_12_30_40_50, EN));
        assertEquals("40", field.getAsShortText(TOD_12_30_40_50, null));
    }

    @Test
    public void getAsShortText_withValueAndLocale_formatsNumber() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals("80", field.getAsShortText(80, EN));
        assertEquals("80", field.getAsShortText(80, null));
    }

    // ---------------------------------------------------------------------
    // Add operations
    // ---------------------------------------------------------------------

    @Test
    public void add_withInstantAndInt_delegatesToDurationField() {
        MockCountingDurationField.add_int = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(1L + UNIT_MILLIS, field.add(1L, 1));
        assertEquals(1, MockCountingDurationField.add_int);
    }

    @Test
    public void add_withInstantAndLong_delegatesToDurationField() {
        MockCountingDurationField.add_long = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(1L + UNIT_MILLIS, field.add(1L, 1L));
        assertEquals(1, MockCountingDurationField.add_long);
    }

    @Test
    public void add_withPartialAndValues_rollsOverWithinValidRange() {
        BaseDateTimeField field = new MockStandardBaseDateTimeField();

        int[] values = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 20, 30, 40}, field.add(new TimeOfDay(), SECOND_INDEX, values, 0));

        values = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 20, 31, 40}, field.add(new TimeOfDay(), SECOND_INDEX, values, 1));

        values = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 21, 0, 40}, field.add(new TimeOfDay(), SECOND_INDEX, values, 30));

        int[] edge = new int[] {23, 59, 30, 40};
        assertThrows(IllegalArgumentException.class, () -> field.add(new TimeOfDay(), SECOND_INDEX, edge, 30));

        values = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 20, 29, 40}, field.add(new TimeOfDay(), SECOND_INDEX, values, -1));

        values = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 19, 59, 40}, field.add(new TimeOfDay(), SECOND_INDEX, values, -31));

        edge = new int[] {0, 0, 30, 40};
        assertThrows(IllegalArgumentException.class, () -> field.add(new TimeOfDay(), SECOND_INDEX, edge, -31));
    }

    // ---------------------------------------------------------------------
    // Add (wrap field) operations
    // ---------------------------------------------------------------------

    @Test
    public void addWrapField_withInstant_wrapsWithinFieldRangeOnly() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(29 * UNIT_MILLIS, field.addWrapField(UNIT_MILLIS * 29, 0));
        assertEquals(59 * UNIT_MILLIS, field.addWrapField(UNIT_MILLIS * 29, 30));
        assertEquals(0 * UNIT_MILLIS, field.addWrapField(UNIT_MILLIS * 29, 31));
    }

    @Test
    public void addWrapField_withPartial_wrapsWithinFieldRangeOnly() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();

        int[] values = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 20, 30, 40}, field.addWrapField(new TimeOfDay(), SECOND_INDEX, values, 0));

        values = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 20, 59, 40}, field.addWrapField(new TimeOfDay(), SECOND_INDEX, values, 29));

        values = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 20, 0, 40}, field.addWrapField(new TimeOfDay(), SECOND_INDEX, values, 30));

        values = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 20, 1, 40}, field.addWrapField(new TimeOfDay(), SECOND_INDEX, values, 31));
    }

    // ---------------------------------------------------------------------
    // Difference operations
    // ---------------------------------------------------------------------

    @Test
    public void getDifference_usesDurationField() {
        MockCountingDurationField.difference_long = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(30, field.getDifference(0L, 0L));
        assertEquals(1, MockCountingDurationField.difference_long);
    }

    @Test
    public void getDifferenceAsLong_usesDurationField() {
        MockCountingDurationField.difference_long = 0;
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(30, field.getDifferenceAsLong(0L, 0L));
        assertEquals(1, MockCountingDurationField.difference_long);
    }

    // ---------------------------------------------------------------------
    // Set (by value and by text)
    // ---------------------------------------------------------------------

    @Test
    public void set_withInstant_setsValue() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.set(2 * UNIT_MILLIS, 0));
        assertEquals(29 * UNIT_MILLIS, field.set(2 * UNIT_MILLIS, 29));
    }

    @Test
    public void set_withPartialAndValues_setsAndValidates() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();

        int[] original = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 20, 30, 40}, field.set(new TimeOfDay(), SECOND_INDEX, original, 30));

        original = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 20, 29, 40}, field.set(new TimeOfDay(), SECOND_INDEX, original, 29));

        original = new int[] {10, 20, 30, 40};
        final int[] snapshot = original.clone();
        assertThrows(IllegalArgumentException.class, () -> field.set(new TimeOfDay(), SECOND_INDEX, original, 60));
        assertArrayEquals(snapshot, original);

        original = new int[] {10, 20, 30, 40};
        final int[] snapshot2 = original.clone();
        assertThrows(IllegalArgumentException.class, () -> field.set(new TimeOfDay(), SECOND_INDEX, original, -1));
        assertArrayEquals(snapshot2, original);
    }

    @Test
    public void set_withInstantAndText_parsesAndSets() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.set(0L, "0", null));
        assertEquals(29 * UNIT_MILLIS, field.set(0L, "29", EN));
    }

    @Test
    public void set_withInstantAndText_defaultLocale() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.set(0L, "0"));
        assertEquals(29 * UNIT_MILLIS, field.set(0L, "29"));
    }

    @Test
    public void set_withPartialAndText_setsAndValidates() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();

        int[] original = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 20, 30, 40}, field.set(new TimeOfDay(), SECOND_INDEX, original, "30", null));

        original = new int[] {10, 20, 30, 40};
        assertArrayEquals(new int[] {10, 20, 29, 40}, field.set(new TimeOfDay(), SECOND_INDEX, original, "29", EN));

        original = new int[] {10, 20, 30, 40};
        final int[] snapshot = original.clone();
        assertThrows(IllegalArgumentException.class, () -> field.set(new TimeOfDay(), SECOND_INDEX, original, "60", null));
        assertArrayEquals(snapshot, original);

        original = new int[] {10, 20, 30, 40};
        final int[] snapshot2 = original.clone();
        assertThrows(IllegalArgumentException.class, () -> field.set(new TimeOfDay(), SECOND_INDEX, original, "-1", null));
        assertArrayEquals(snapshot2, original);
    }

    @Test
    public void convertText_parsesOrRejects() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.convertText("0", null));
        assertEquals(29, field.convertText("29", null));
        assertThrows(IllegalArgumentException.class, () -> field.convertText("2A", null));
        assertThrows(IllegalArgumentException.class, () -> field.convertText(null, null));
    }

    // ---------------------------------------------------------------------
    // Leap info
    // ---------------------------------------------------------------------

    @Test
    public void isLeap_alwaysFalse() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertFalse(field.isLeap(0L));
    }

    @Test
    public void getLeapAmount_isZero() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.getLeapAmount(0L));
    }

    @Test
    public void getLeapDurationField_isNull() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(null, field.getLeapDurationField());
    }

    // ---------------------------------------------------------------------
    // Min/max value
    // ---------------------------------------------------------------------

    @Test
    public void getMinimumValue_returnsZero() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.getMinimumValue());
        assertEquals(0, field.getMinimumValue(0L));
        assertEquals(0, field.getMinimumValue(new TimeOfDay()));
        assertEquals(0, field.getMinimumValue(new TimeOfDay(), new int[4]));
    }

    @Test
    public void getMaximumValue_returns59() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(59, field.getMaximumValue());
        assertEquals(59, field.getMaximumValue(0L));
        assertEquals(59, field.getMaximumValue(new TimeOfDay()));
        assertEquals(59, field.getMaximumValue(new TimeOfDay(), new int[4]));
    }

    // ---------------------------------------------------------------------
    // Max text length
    // ---------------------------------------------------------------------

    @Test
    public void getMaximumTextLength_dependsOnMaxValue() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(2, field.getMaximumTextLength(EN));

        field = new MockPreciseDurationDateTimeField() {
            @Override
            public int getMaximumValue() {
                return 5;
            }
        };
        assertEquals(1, field.getMaximumTextLength(EN));

        field = new MockPreciseDurationDateTimeField() {
            @Override
            public int getMaximumValue() {
                return 555;
            }
        };
        assertEquals(3, field.getMaximumTextLength(EN));

        field = new MockPreciseDurationDateTimeField() {
            @Override
            public int getMaximumValue() {
                return 5555;
            }
        };
        assertEquals(4, field.getMaximumTextLength(EN));

        field = new MockPreciseDurationDateTimeField() {
            @Override
            public int getMaximumValue() {
                return -1;
            }
        };
        assertEquals(2, field.getMaximumTextLength(EN));
    }

    @Test
    public void getMaximumShortTextLength_matchesMaximumTextLengthForNumbers() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(2, field.getMaximumShortTextLength(EN));
    }

    // ---------------------------------------------------------------------
    // Rounding and remainder
    // ---------------------------------------------------------------------

    @Test
    public void roundFloor_alignsDownToUnitBoundary() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(-2 * UNIT_MILLIS, field.roundFloor(-61L));
        assertEquals(-1 * UNIT_MILLIS, field.roundFloor(-60L));
        assertEquals(-1 * UNIT_MILLIS, field.roundFloor(-59L));
        assertEquals(-1 * UNIT_MILLIS, field.roundFloor(-1L));
        assertEquals(0L, field.roundFloor(0L));
        assertEquals(0L, field.roundFloor(1L));
        assertEquals(0L, field.roundFloor(29L));
        assertEquals(0L, field.roundFloor(30L));
        assertEquals(0L, field.roundFloor(31L));
        assertEquals(1 * UNIT_MILLIS, field.roundFloor(60L));
    }

    @Test
    public void roundCeiling_alignsUpToUnitBoundary() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(-1 * UNIT_MILLIS, field.roundCeiling(-61L));
        assertEquals(-1 * UNIT_MILLIS, field.roundCeiling(-60L));
        assertEquals(0L, field.roundCeiling(-59L));
        assertEquals(0L, field.roundCeiling(-1L));
        assertEquals(0L, field.roundCeiling(0L));
        assertEquals(1 * UNIT_MILLIS, field.roundCeiling(1L));
        assertEquals(1 * UNIT_MILLIS, field.roundCeiling(29L));
        assertEquals(1 * UNIT_MILLIS, field.roundCeiling(30L));
        assertEquals(1 * UNIT_MILLIS, field.roundCeiling(31L));
        assertEquals(1 * UNIT_MILLIS, field.roundCeiling(60L));
    }

    @Test
    public void roundHalfFloor_tiesRoundDown() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.roundHalfFloor(0L));
        assertEquals(0L, field.roundHalfFloor(29L));
        assertEquals(0L, field.roundHalfFloor(30L));
        assertEquals(1 * UNIT_MILLIS, field.roundHalfFloor(31L));
        assertEquals(1 * UNIT_MILLIS, field.roundHalfFloor(60L));
    }

    @Test
    public void roundHalfCeiling_tiesRoundUp() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.roundHalfCeiling(0L));
        assertEquals(0L, field.roundHalfCeiling(29L));
        assertEquals(1 * UNIT_MILLIS, field.roundHalfCeiling(30L));
        assertEquals(1 * UNIT_MILLIS, field.roundHalfCeiling(31L));
        assertEquals(1 * UNIT_MILLIS, field.roundHalfCeiling(60L));
    }

    @Test
    public void roundHalfEven_tiesToEven() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.roundHalfEven(0L));
        assertEquals(0L, field.roundHalfEven(29L));
        assertEquals(0L, field.roundHalfEven(30L));   // even
        assertEquals(1 * UNIT_MILLIS, field.roundHalfEven(31L));
        assertEquals(1 * UNIT_MILLIS, field.roundHalfEven(60L));
        assertEquals(1 * UNIT_MILLIS, field.roundHalfEven(89L));
        assertEquals(2 * UNIT_MILLIS, field.roundHalfEven(90L)); // even
        assertEquals(2 * UNIT_MILLIS, field.roundHalfEven(91L));
    }

    @Test
    public void remainder_returnsOffsetWithinUnit() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0L, field.remainder(0L));
        assertEquals(29L, field.remainder(29L));
        assertEquals(30L, field.remainder(30L));
        assertEquals(31L, field.remainder(31L));
        assertEquals(0L, field.remainder(60L));
    }

    // ---------------------------------------------------------------------
    // Mocks used by the tests
    // ---------------------------------------------------------------------

    /**
     * Mock field representing "seconds of minute" with unit 60 millis.
     * get(long instant) returns integer division by 60.
     * Range duration field is "minutes".
     */
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
            return (int) (instant / UNIT_MILLIS);
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

    /**
     * Same mock as above but uses real ISOChronology duration fields.
     * This ensures arithmetic across boundaries behaves like real fields.
     */
    static class MockStandardBaseDateTimeField extends MockPreciseDurationDateTimeField {
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
     * DurationField that:
     * - is precise
     * - has unit millis = 60
     * - counts calls to add(...) and getDifferenceAsLong(...)
     */
    static class MockCountingDurationField extends BaseDurationField {
        static int add_int = 0;
        static int add_long = 0;
        static int difference_long = 0;

        protected MockCountingDurationField(DurationFieldType type) {
            super(type);
        }
        @Override
        public boolean isPrecise() {
            return true;
        }
        @Override
        public long getUnitMillis() {
            return UNIT_MILLIS;
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
            return instant + (value * UNIT_MILLIS);
        }
        @Override
        public long add(long instant, long value) {
            add_long++;
            return instant + (value * UNIT_MILLIS);
        }
        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            difference_long++;
            return 30;
        }
    }

    /**
     * Precise duration field with zero unit millis (invalid for construction).
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

    /**
     * Imprecise duration field (invalid for construction).
     */
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