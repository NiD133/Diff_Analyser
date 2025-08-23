package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link GregorianChronology} factory methods.
 */
class GregorianChronologyTest {

    @Test
    void getInstance_throwsIllegalArgumentException_forInvalidMinDaysInFirstWeek() {
        // Arrange
        // The factory method `getInstance` expects minDaysInFirstWeek to be between 1 and 7.
        // We use a value far outside this range to verify the input validation.
        final int invalidMinDaysInFirstWeek = 531;
        final DateTimeZone dummyZone = DateTimeZone.UTC;
        final String expectedErrorMessage = "Invalid min days in first week: " + invalidMinDaysInFirstWeek;

        // Act & Assert
        // Verify that calling the method with an invalid argument throws the correct exception.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> GregorianChronology.getInstance(dummyZone, invalidMinDaysInFirstWeek)
        );

        // Further assert that the exception message is as expected.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}