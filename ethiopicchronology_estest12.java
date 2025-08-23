package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the EthiopicChronology class.
 */
public class EthiopicChronologyTest {

    /**
     * Tests that the constructor throws an IllegalArgumentException when provided with an
     * invalid value for the 'minDaysInFirstWeek' parameter.
     */
    @Test
    public void constructorShouldThrowExceptionForInvalidMinDaysInFirstWeek() {
        // The valid range for minDaysInFirstWeek is from 1 to 7.
        // We use an out-of-range value to trigger the exception.
        int invalidMinDays = -159;

        try {
            // Act: Attempt to create an EthiopicChronology instance with the invalid parameter.
            // The first two 'null' arguments are for the base chronology and a parameter object,
            // which are not relevant to this specific validation check.
            new EthiopicChronology((Chronology) null, null, invalidMinDays);

            // Assert: If no exception is thrown, the test should fail.
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the thrown exception has the expected message.
            assertEquals("Invalid min days in first week: " + invalidMinDays, e.getMessage());
        }
    }
}