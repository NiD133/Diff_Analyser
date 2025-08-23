package org.joda.time;

import org.joda.time.chrono.JulianChronology;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Setting the year to 0 in a Julian calendar should throw an IllegalFieldValueException with correct details")
    void setJulianYearToZero_shouldThrowException() {
        // The Julian calendar does not have a year 0. It transitions directly
        // from 1 BC to 1 AD. Attempting to set the year to 0 is an invalid operation.

        // Arrange
        DateTime dateTimeInJulianCalendar = new DateTime(JulianChronology.getInstanceUTC());
        final int illegalYearValue = 0;

        // Act & Assert: Verify that the expected exception is thrown.
        IllegalFieldValueException thrown = assertThrows(
                IllegalFieldValueException.class,
                () -> dateTimeInJulianCalendar.year().setCopy(illegalYearValue),
                "Setting the Julian year to 0 should throw an IllegalFieldValueException."
        );

        // Assert: Verify that the exception contains the correct details about the failure.
        assertAll("Exception properties",
                () -> assertEquals(DateTimeFieldType.year(), thrown.getDateTimeFieldType(), "The field type should be 'year'."),
                () -> assertNull(thrown.getDurationFieldType(), "The duration field type should be null as it's a DateTimeField."),
                () -> assertEquals("year", thrown.getFieldName(), "The field name should be 'year'."),
                () -> assertEquals(illegalYearValue, thrown.getIllegalNumberValue(), "The illegal number value should be 0."),
                () -> assertNull(thrown.getIllegalStringValue(), "The illegal string value should be null as a number was provided."),
                () -> assertEquals(String.valueOf(illegalYearValue), thrown.getIllegalValueAsString(), "The illegal value as a string should be '0'."),
                () -> assertNull(thrown.getLowerBound(), "The lower bound should be null for this type of error."),
                () -> assertNull(thrown.getUpperBound(), "The upper bound should be null for this type of error.")
        );
    }
}