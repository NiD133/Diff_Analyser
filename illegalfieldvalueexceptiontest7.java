package org.joda.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link IllegalFieldValueException}.
 * This test class verifies that when a Joda-Time class like {@link YearMonthDay}
 * is constructed with invalid field values, it throws an {@link IllegalFieldValueException}
 * with the correct details.
 */
@SuppressWarnings("deprecation") // YearMonthDay is deprecated, but used here to trigger the exception.
class IllegalFieldValueExceptionTest {

    @Nested
    @DisplayName("When creating a YearMonthDay")
    class YearMonthDayCreation {

        @Test
        @DisplayName("throws exception with correct details for a month value that is too low")
        void throwsExceptionForMonthTooLow() {
            // Arrange: Define an invalid negative month value
            int invalidMonth = -5;

            // Act: Attempt to create a YearMonthDay with the invalid month
            IllegalFieldValueException exception = assertThrows(
                    IllegalFieldValueException.class,
                    () -> new YearMonthDay(1970, invalidMonth, 1)
            );

            // Assert: Verify the exception contains the correct details about the error
            assertAll("Exception properties",
                    () -> assertEquals(DateTimeFieldType.monthOfYear(), exception.getDateTimeFieldType(), "Field type should be monthOfYear"),
                    () -> assertNull(exception.getDurationFieldType(), "Duration field type should be null"),
                    () -> assertEquals("monthOfYear", exception.getFieldName(), "Field name should be 'monthOfYear'"),
                    () -> assertEquals(invalidMonth, exception.getIllegalNumberValue(), "Illegal value should be the one provided"),
                    () -> assertNull(exception.getIllegalStringValue(), "Illegal string value should be null for a number-based error"),
                    () -> assertEquals(String.valueOf(invalidMonth), exception.getIllegalValueAsString(), "Illegal value as string should match"),
                    () -> assertEquals(1, exception.getLowerBound(), "Lower bound for month should be 1"),
                    () -> assertNull(exception.getUpperBound(), "Upper bound should be null as the value was below the lower bound")
            );
        }

        @Test
        @DisplayName("throws exception with correct details for a month value that is too high")
        void throwsExceptionForMonthTooHigh() {
            // Arrange: Define a month value greater than 12
            int invalidMonth = 500;

            // Act: Attempt to create a YearMonthDay with the invalid month
            IllegalFieldValueException exception = assertThrows(
                    IllegalFieldValueException.class,
                    () -> new YearMonthDay(1970, invalidMonth, 1)
            );

            // Assert: Verify the exception details
            assertAll("Exception properties",
                    () -> assertEquals(DateTimeFieldType.monthOfYear(), exception.getDateTimeFieldType()),
                    () -> assertNull(exception.getDurationFieldType()),
                    () -> assertEquals("monthOfYear", exception.getFieldName()),
                    () -> assertEquals(invalidMonth, exception.getIllegalNumberValue()),
                    () -> assertNull(exception.getIllegalStringValue()),
                    () -> assertEquals(String.valueOf(invalidMonth), exception.getIllegalValueAsString()),
                    () -> assertNull(exception.getLowerBound(), "Lower bound should be null as the value was above the upper bound"),
                    () -> assertEquals(12, exception.getUpperBound(), "Upper bound for month should be 12")
            );
        }

        @Test
        @DisplayName("throws exception with correct details for a day value that is invalid for the given month")
        void throwsExceptionForInvalidDayOfMonth() {
            // Arrange: Day 30 is invalid for February 1970, which had 28 days.
            int invalidDay = 30;

            // Act: Attempt to create a YearMonthDay with the invalid day
            IllegalFieldValueException exception = assertThrows(
                    IllegalFieldValueException.class,
                    () -> new YearMonthDay(1970, 2, invalidDay)
            );

            // Assert: Verify the exception details
            assertAll("Exception properties",
                    () -> assertEquals(DateTimeFieldType.dayOfMonth(), exception.getDateTimeFieldType()),
                    () -> assertNull(exception.getDurationFieldType()),
                    () -> assertEquals("dayOfMonth", exception.getFieldName()),
                    () -> assertEquals(invalidDay, exception.getIllegalNumberValue()),
                    () -> assertNull(exception.getIllegalStringValue()),
                    () -> assertEquals(String.valueOf(invalidDay), exception.getIllegalValueAsString()),
                    () -> assertNull(exception.getLowerBound()),
                    () -> assertEquals(28, exception.getUpperBound(), "Upper bound for day in Feb 1970 should be 28")
            );
        }
    }
}