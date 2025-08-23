package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the factory method {@link EthiopicChronology#getInstance(DateTimeZone, int)}.
 */
// The original class name and inheritance are kept to match the context provided.
public class EthiopicChronology_ESTestTest11 extends EthiopicChronology_ESTest_scaffolding {

    /**
     * Verifies that getInstance() throws an IllegalArgumentException when the
     * 'minDaysInFirstWeek' parameter is outside the valid range of 1-7.
     * This test specifically checks a value greater than the maximum allowed.
     */
    @Test
    public void getInstance_whenMinDaysInFirstWeekIsTooLarge_throwsIllegalArgumentException() {
        // Arrange: Define an invalid number of minimum days. The valid range is [1, 7].
        final int invalidMinDays = 8;
        final DateTimeZone utcZone = DateTimeZone.UTC;

        // Act & Assert
        try {
            EthiopicChronology.getInstance(utcZone, invalidMinDays);
            fail("Expected an IllegalArgumentException for an invalid 'minDaysInFirstWeek' value.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct and informative.
            String expectedMessage = "Invalid min days in first week: " + invalidMinDays;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}