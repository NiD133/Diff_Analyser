package org.joda.time;

import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
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
    @DisplayName("When verifying a DateTimeField, an out-of-bounds value should throw an exception with correct details")
    void verifyValueBounds_forDateTimeField_throwsExceptionWithCorrectDetails() {
        // Arrange
        DateTimeField monthOfYearField = ISOChronology.getInstance().monthOfYear();
        int illegalValue = -5;
        int lowerBound = 1;
        int upperBound = 31;

        // Act
        IllegalFieldValueException thrown = assertThrows(
            IllegalFieldValueException.class,
            () -> FieldUtils.verifyValueBounds(monthOfYearField, illegalValue, lowerBound, upperBound)
        );

        // Assert
        assertAll("Exception properties for DateTimeField",
            () -> assertEquals(DateTimeFieldType.monthOfYear(), thrown.getDateTimeFieldType()),
            () -> assertNull(thrown.getDurationFieldType()),
            () -> assertEquals("monthOfYear", thrown.getFieldName()),
            () -> assertEquals(illegalValue, thrown.getIllegalNumberValue().intValue()),
            () -> assertNull(thrown.getIllegalStringValue()),
            () -> assertEquals(String.valueOf(illegalValue), thrown.getIllegalValueAsString()),
            () -> assertEquals(lowerBound, thrown.getLowerBound().intValue()),
            () -> assertEquals(upperBound, thrown.getUpperBound().intValue())
        );
    }

    @Test
    @DisplayName("When verifying a DateTimeFieldType, an out-of-bounds value should throw an exception with correct details")
    void verifyValueBounds_forDateTimeFieldType_throwsExceptionWithCorrectDetails() {
        // Arrange
        DateTimeFieldType hourOfDayType = DateTimeFieldType.hourOfDay();
        int illegalValue = 27;
        int lowerBound = 0;
        int upperBound = 23;

        // Act
        IllegalFieldValueException thrown = assertThrows(
            IllegalFieldValueException.class,
            () -> FieldUtils.verifyValueBounds(hourOfDayType, illegalValue, lowerBound, upperBound)
        );

        // Assert
        assertAll("Exception properties for DateTimeFieldType",
            () -> assertEquals(hourOfDayType, thrown.getDateTimeFieldType()),
            () -> assertNull(thrown.getDurationFieldType()),
            () -> assertEquals("hourOfDay", thrown.getFieldName()),
            () -> assertEquals(illegalValue, thrown.getIllegalNumberValue().intValue()),
            () -> assertNull(thrown.getIllegalStringValue()),
            () -> assertEquals(String.valueOf(illegalValue), thrown.getIllegalValueAsString()),
            () -> assertEquals(lowerBound, thrown.getLowerBound().intValue()),
            () -> assertEquals(upperBound, thrown.getUpperBound().intValue())
        );
    }

    @Test
    @DisplayName("When verifying a field by name, an out-of-bounds value should throw an exception with correct details")
    void verifyValueBounds_forStringFieldName_throwsExceptionWithCorrectDetails() {
        // Arrange
        String fieldName = "foo";
        int illegalValue = 1;
        int lowerBound = 2;
        int upperBound = 3;

        // Act
        IllegalFieldValueException thrown = assertThrows(
            IllegalFieldValueException.class,
            () -> FieldUtils.verifyValueBounds(fieldName, illegalValue, lowerBound, upperBound)
        );

        // Assert
        assertAll("Exception properties for String field name",
            () -> assertNull(thrown.getDateTimeFieldType()),
            () -> assertNull(thrown.getDurationFieldType()),
            () -> assertEquals(fieldName, thrown.getFieldName()),
            () -> assertEquals(illegalValue, thrown.getIllegalNumberValue().intValue()),
            () -> assertNull(thrown.getIllegalStringValue()),
            () -> assertEquals(String.valueOf(illegalValue), thrown.getIllegalValueAsString()),
            () -> assertEquals(lowerBound, thrown.getLowerBound().intValue()),
            () -> assertEquals(upperBound, thrown.getUpperBound().intValue())
        );
    }
}