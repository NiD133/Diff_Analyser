package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the factory method {@link EthiopicChronology#getInstance(DateTimeZone, int)}.
 */
public class EthiopicChronologyTest {

    /**
     * Verifies that getInstance() throws an IllegalArgumentException when the
     * 'minDaysInFirstWeek' parameter is outside the valid range of 1 to 7.
     */
    @Test
    public void getInstance_withInvalidMinDaysInFirstWeek_throwsIllegalArgumentException() {
        // Arrange: Define an invalid value for the minimum days in the first week.
        // The valid range is [1, 7].
        final int invalidMinDays = -4898;
        final DateTimeZone utcZone = DateTimeZone.UTC;
        final String expectedErrorMessage = "Invalid min days in first week: " + invalidMinDays;

        // Act & Assert: Use assertThrows to verify that the correct exception is thrown.
        // This is a modern, readable way to test for expected exceptions.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> EthiopicChronology.getInstance(utcZone, invalidMinDays)
        );

        // Further assert that the exception message is as expected.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}