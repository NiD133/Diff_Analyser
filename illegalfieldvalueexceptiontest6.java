package org.joda.time;

import org.joda.time.chrono.GJChronology;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link IllegalFieldValueException}.
 * This test focuses on edge cases related to the Gregorian-Julian calendar cutover.
 */
class IllegalFieldValueExceptionTest {

    @DisplayName("Setting day of month to a non-existent day during the GJ cutover should throw IllegalFieldValueException")
    @ParameterizedTest(name = "Setting day to {1} on {0} should fail")
    @CsvSource({
        "'1582-10-04', 5",  // Try to set to a day within the gap from a date before the gap
        "'1582-10-15', 14"  // Try to set to a day within the gap from a date after the gap
    })
    void setDayOfMonth_toInvalidDayInGJCutoverGap_throwsException(String initialDateStr, int invalidDayToSet) {
        // The Gregorian calendar reform skipped the dates from October 5 to October 14, 1582.
        // This test verifies that attempting to set the day of the month to any of these
        // non-existent dates throws an IllegalFieldValueException with the correct details.
        
        // Arrange
        DateTime initialDate = new DateTime(initialDateStr, GJChronology.getInstanceUTC());

        // Act & Assert
        IllegalFieldValueException thrownException = assertThrows(
            IllegalFieldValueException.class,
            () -> initialDate.dayOfMonth().setCopy(invalidDayToSet),
            "Expected an IllegalFieldValueException for a non-existent day."
        );

        // Assert on Exception properties
        assertEquals(DateTimeFieldType.dayOfMonth(), thrownException.getDateTimeFieldType(), "Field type should be dayOfMonth");
        assertEquals("dayOfMonth", thrownException.getFieldName(), "Field name should be 'dayOfMonth'");
        assertEquals(invalidDayToSet, thrownException.getIllegalNumberValue(), "Illegal number value should match the input");
        assertEquals(String.valueOf(invalidDayToSet), thrownException.getIllegalValueAsString(), "Illegal string value should match the input");
        
        // Verify that properties not relevant to this exception type are null
        assertNull(thrownException.getDurationFieldType(), "DurationFieldType should be null");
        assertNull(thrownException.getIllegalStringValue(), "IllegalStringValue should be null for a numeric error");
        assertNull(thrownException.getLowerBound(), "LowerBound is not specified for this error");
        assertNull(thrownException.getUpperBound(), "UpperBound is not specified for this error");
    }
}