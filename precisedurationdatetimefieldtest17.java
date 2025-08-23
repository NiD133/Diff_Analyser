package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the PreciseDurationDateTimeField class.
 *
 * This suite focuses on the behavior of getAsShortText, which is inherited
 * from the BaseDateTimeField superclass.
 */
public class PreciseDurationDateTimeFieldTest {

    /**
     * A concrete implementation of the abstract SUT (System Under Test) for instantiation.
     * The methods implemented here are required by the abstract class but are not
     * relevant to the specific test case, which exercises inherited behavior.
     */
    private static class TestablePreciseDurationDateTimeField extends PreciseDurationDateTimeField {
        TestablePreciseDurationDateTimeField() {
            // Provide a real, precise duration field to satisfy the superclass constructor.
            super(DateTimeFieldType.secondOfMinute(), ISOChronology.getInstanceUTC().seconds());
        }

        @Override
        public int get(long instant) {
            // Not relevant for this test.
            return 0;
        }

        @Override
        public DurationField getRangeDurationField() {
            // Not relevant for this test.
            return null;
        }

        @Override
        public int getMaximumValue() {
            // Not relevant for this test.
            return 59;
        }
    }

    @Test
    public void getAsShortText_shouldReturnStringRepresentationOfValue() {
        // Arrange
        // The getAsShortText method is inherited from BaseDateTimeField and simply calls
        // Integer.toString() on the value, ignoring the locale.
        BaseDateTimeField field = new TestablePreciseDurationDateTimeField();
        int value = 80;

        // Act & Assert
        // The behavior should be consistent whether a locale is provided or not.
        String textWithLocale = field.getAsShortText(value, Locale.ENGLISH);
        assertEquals("The short text should be the string representation of the value.", "80", textWithLocale);

        String textWithNullLocale = field.getAsShortText(value, null);
        assertEquals("A null locale should be handled gracefully, returning the same string representation.", "80", textWithNullLocale);
    }
}