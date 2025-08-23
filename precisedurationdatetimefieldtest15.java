package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadablePartial;
import org.joda.time.TimeOfDay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the behavior of methods in {@link BaseDateTimeField} as inherited by
 * {@link PreciseDurationDateTimeField}.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A minimal, concrete implementation of PreciseDurationDateTimeField for testing purposes.
     * It provides dummy implementations for abstract methods that are not relevant to this test.
     */
    private static class StubDateTimeField extends PreciseDurationDateTimeField {

        StubDateTimeField() {
            // The super constructor requires a non-null, precise duration field.
            super(DateTimeFieldType.secondOfMinute(), new StubDurationField());
        }

        // The following abstract methods must be implemented but are not called by the test.
        @Override
        public int get(long instant) {
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            return null;
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    /**
     * A minimal, precise duration field required by the StubDateTimeField constructor.
     */
    private static class StubDurationField extends BaseDurationField {

        StubDurationField() {
            super(DurationFieldType.seconds());
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            // This value must be 1 or greater for the parent constructor to succeed.
            return 1000L;
        }

        // Other methods from BaseDurationField are not needed for this test.
    }

    private BaseDateTimeField testField;

    @BeforeEach
    public void setUp() {
        testField = new StubDateTimeField();
    }

    @Test
    public void getAsShortTextWithValueShouldIgnorePartialAndLocale() {
        // This test verifies the behavior of BaseDateTimeField.getAsShortText(), which is inherited.
        // The method should simply convert the integer value to a String, ignoring the
        // ReadablePartial and Locale arguments.

        // Arrange
        final ReadablePartial ignoredPartial = new TimeOfDay(12, 30, 40, 50);
        final int valueToFormat = 20;
        final String expectedText = "20";

        // Act: Call the method with a specific locale (Locale.ENGLISH)
        String actualTextWithLocale = testField.getAsShortText(ignoredPartial, valueToFormat, Locale.ENGLISH);

        // Assert
        assertEquals(expectedText, actualTextWithLocale,
                "getAsShortText should return the string representation of the value, ignoring the locale.");

        // Act: Call the method with a null locale
        String actualTextWithNullLocale = testField.getAsShortText(ignoredPartial, valueToFormat, null);

        // Assert
        assertEquals(expectedText, actualTextWithNullLocale,
                "getAsShortText should handle a null locale gracefully and return the same string representation.");
    }
}