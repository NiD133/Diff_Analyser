package org.joda.time;

import org.joda.time.chrono.ISOChronology;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for IllegalFieldValueException, focusing on exceptions thrown
 * when setting a field with an invalid text value.
 */
public class IllegalFieldValueExceptionTest {

    private static final Chronology UTC_CHRONOLOGY = ISOChronology.getInstanceUTC();
    private static final long INSTANT = 0L; // A base instant for the 'set' method.

    /**
     * Verifies that setting a DateTimeField with an invalid text value throws
     * an IllegalFieldValueException with the correct state.
     */
    private void assertExceptionOnInvalidText(
            DateTimeField field,
            String invalidText,
            DateTimeFieldType expectedFieldType) {

        // Act: Attempt to set the field with invalid text, and expect an exception.
        IllegalFieldValueException exception = assertThrows(
                IllegalFieldValueException.class,
                () -> field.set(INSTANT, invalidText, Locale.US)
        );

        // Assert: Verify the state of the caught exception.
        assertThat(exception.getDateTimeFieldType()).isEqualTo(expectedFieldType);
        assertThat(exception.getDurationFieldType()).isNull();
        assertThat(exception.getFieldName()).isEqualTo(expectedFieldType.getName());
        assertThat(exception.getIllegalNumberValue()).isNull();
        assertThat(exception.getIllegalStringValue()).isEqualTo(invalidText);
        assertThat(exception.getLowerBound()).isNull();
        assertThat(exception.getUpperBound()).isNull();

        // The getIllegalValueAsString() method should return "null" for a null input string.
        String expectedValueAsString = (invalidText == null) ? "null" : invalidText;
        assertThat(exception.getIllegalValueAsString()).isEqualTo(expectedValueAsString);
    }

    @Test
    public void set_forYearWithNullText_throwsExceptionWithCorrectState() {
        assertExceptionOnInvalidText(UTC_CHRONOLOGY.year(), null, DateTimeFieldType.year());
    }

    @Test
    public void set_forYearWithUnparseableText_throwsExceptionWithCorrectState() {
        assertExceptionOnInvalidText(UTC_CHRONOLOGY.year(), "nineteen seventy", DateTimeFieldType.year());
    }

    @Test
    public void set_forEraWithUnparseableText_throwsExceptionWithCorrectState() {
        assertExceptionOnInvalidText(UTC_CHRONOLOGY.era(), "long ago", DateTimeFieldType.era());
    }

    @Test
    public void set_forMonthOfYearWithUnparseableText_throwsExceptionWithCorrectState() {
        assertExceptionOnInvalidText(UTC_CHRONOLOGY.monthOfYear(), "spring", DateTimeFieldType.monthOfYear());
    }

    @Test
    public void set_forDayOfWeekWithUnparseableText_throwsExceptionWithCorrectState() {
        assertExceptionOnInvalidText(UTC_CHRONOLOGY.dayOfWeek(), "yesterday", DateTimeFieldType.dayOfWeek());
    }

    @Test
    public void set_forHalfdayOfDayWithUnparseableText_throwsExceptionWithCorrectState() {
        assertExceptionOnInvalidText(UTC_CHRONOLOGY.halfdayOfDay(), "morning", DateTimeFieldType.halfdayOfDay());
    }
}