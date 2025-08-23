package org.joda.time;

import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.SkipDateTimeField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link IllegalFieldValueException}.
 */
class IllegalFieldValueExceptionTest {

    @Test
    void setOnSkipDateTimeField_withSkippedValue_throwsExceptionWithCorrectDetails() {
        // Arrange
        final int skippedYear = 1970;
        DateTimeField yearField = ISOChronology.getInstanceUTC().year();
        DateTimeField fieldToTest = new SkipDateTimeField(ISOChronology.getInstanceUTC(), yearField, skippedYear);

        // Act & Assert
        // The set() method is expected to throw an exception when trying to set the skipped value.
        IllegalFieldValueException thrownException = assertThrows(
            IllegalFieldValueException.class,
            () -> fieldToTest.set(0L, skippedYear)
        );

        // Assert that the exception contains the correct details about the failure.
        assertAll("Exception properties",
            () -> assertEquals(DateTimeFieldType.year(), thrownException.getDateTimeFieldType(), "DateTime field type should be 'year'"),
            () -> assertNull(thrownException.getDurationFieldType(), "Duration field type should be null"),
            () -> assertEquals("year", thrownException.getFieldName(), "Field name should be 'year'"),
            () -> assertEquals(Integer.valueOf(skippedYear), thrownException.getIllegalNumberValue(), "Illegal number value should be the skipped year"),
            () -> assertNull(thrownException.getIllegalStringValue(), "Illegal string value should be null"),
            () -> assertEquals(String.valueOf(skippedYear), thrownException.getIllegalValueAsString(), "Illegal value as string should be the skipped year"),
            () -> assertNull(thrownException.getLowerBound(), "Lower bound should be null"),
            () -> assertNull(thrownException.getUpperBound(), "Upper bound should be null")
        );
    }
}