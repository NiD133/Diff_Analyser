package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

/**
 * Unit tests for {@link PreciseDurationDateTimeField}.
 * <p>
 * This test class focuses on the {@link BaseDateTimeField#getMaximumTextLength(Locale)}
 * method, which is inherited by {@link PreciseDurationDateTimeField}.
 */
public class PreciseDurationDateTimeFieldTest {

    @Test
    public void getMaximumTextLength_givenDefaultMaxValue_returnsCorrectLength() {
        // The default mock has a maximum value of 59, so the length is 2.
        BaseDateTimeField field = new ConfigurableField();
        assertEquals(2, field.getMaximumTextLength(Locale.ENGLISH));
    }

    @Test
    public void getMaximumTextLength_givenSingleDigitMaxValue_returnsOne() {
        BaseDateTimeField field = new ConfigurableField(5);
        assertEquals(1, field.getMaximumTextLength(Locale.ENGLISH));
    }

    @Test
    public void getMaximumTextLength_givenMultiDigitMaxValue_returnsCorrectLength() {
        BaseDateTimeField field = new ConfigurableField(555);
        assertEquals(3, field.getMaximumTextLength(Locale.ENGLISH));
    }

    @Test
    public void getMaximumTextLength_givenLargeMultiDigitMaxValue_returnsCorrectLength() {
        BaseDateTimeField field = new ConfigurableField(5555);
        assertEquals(4, field.getMaximumTextLength(Locale.ENGLISH));
    }

    @Test
    public void getMaximumTextLength_givenNegativeMaxValue_returnsLengthIncludingSign() {
        // Max value of -1 requires 2 characters: '-' and '1'.
        BaseDateTimeField field = new ConfigurableField(-1);
        assertEquals(2, field.getMaximumTextLength(Locale.ENGLISH));
    }

    /**
     * A mock PreciseDurationDateTimeField that allows configuring the maximum value.
     * This is used to test how getMaximumTextLength behaves with different max values.
     */
    private static class ConfigurableField extends PreciseDurationDateTimeField {
        private final int maxValue;

        /**
         * Creates a field with a default maximum value of 59.
         */
        ConfigurableField() {
            this(59);
        }

        /**
         * Creates a field with a specific maximum value.
         * @param maxValue the maximum value for this field
         */
        ConfigurableField(int maxValue) {
            super(DateTimeFieldType.secondOfMinute(), new MockUnitDurationField());
            this.maxValue = maxValue;
        }

        @Override
        public int getMaximumValue() {
            return this.maxValue;
        }

        // The following methods are abstract in the parent class but not used
        // in these tests. They are given minimal, non-functional implementations.
        @Override
        public int get(long instant) {
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            return null;
        }
    }

    /**
     * A minimal, precise duration field required by the PreciseDurationDateTimeField constructor.
     * It is non-functional beyond satisfying the constructor's validation checks.
     */
    private static class MockUnitDurationField extends BaseDurationField {
        MockUnitDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override public boolean isPrecise() { return true; }
        @Override public long getUnitMillis() { return 1L; } // Must be >= 1
        @Override public long getValueAsLong(long duration, long instant) { return 0; }
        @Override public long getMillis(int value, long instant) { return 0; }
        @Override public long getMillis(long value, long instant) { return 0; }
        @Override public long add(long instant, int value) { return 0; }
        @Override public long add(long instant, long value) { return 0; }
        @Override public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { return 0; }
    }
}