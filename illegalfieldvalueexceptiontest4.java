package org.joda.time;

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
    @DisplayName("Setting an hour that falls into a DST gap should throw IllegalFieldValueException")
    void setHourOfDay_toNonExistentHourInDstGap_throwsException() {
        // Arrange: Define a date and time just before a Daylight Saving Time (DST) transition.
        // In the "America/Los_Angeles" timezone, on April 3, 2005, the clocks jumped
        // forward from 2:00 AM to 3:00 AM. The 2:xx hour did not exist on this day.
        final DateTimeZone losAngelesZone = DateTimeZone.forID("America/Los_Angeles");
        final DateTime dateTimeBeforeDstGap = new DateTime(2005, 4, 3, 1, 0, losAngelesZone);
        final int nonExistentHour = 2;

        // Act & Assert: Attempting to set the hour to a non-existent value should throw an exception.
        // We use assertThrows to verify that the correct exception is thrown and to capture it.
        IllegalFieldValueException thrownException = assertThrows(
            IllegalFieldValueException.class,
            () -> dateTimeBeforeDstGap.hourOfDay().setCopy(nonExistentHour)
        );

        // Assert: Verify the properties of the thrown exception are correct.
        // Using assertAll to group related assertions and report all failures at once.
        assertAll("Exception properties",
            () -> assertEquals(DateTimeFieldType.hourOfDay(), thrownException.getDateTimeFieldType(), "DateTimeFieldType should be hourOfDay"),
            () -> assertNull(thrownException.getDurationFieldType(), "DurationFieldType should be null"),
            () -> assertEquals("hourOfDay", thrownException.getFieldName(), "Field name should be 'hourOfDay'"),
            () -> assertEquals(nonExistentHour, thrownException.getIllegalNumberValue(), "IllegalNumberValue should be the attempted hour"),
            () -> assertNull(thrownException.getIllegalStringValue(), "IllegalStringValue should be null as a number was provided"),
            () -> assertEquals(String.valueOf(nonExistentHour), thrownException.getIllegalValueAsString(), "IllegalValueAsString should be the string representation of the attempted hour"),
            () -> assertNull(thrownException.getLowerBound(), "LowerBound should be null for a DST gap scenario"),
            () -> assertNull(thrownException.getUpperBound(), "UpperBound should be null for a DST gap scenario")
        );
    }
}