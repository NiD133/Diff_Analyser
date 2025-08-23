package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * A test suite for the CopticChronology class, focusing on instance creation.
 */
public class CopticChronologyTest {

    /**
     * Tests that creating a CopticChronology instance with an invalid value for
     * 'minDaysInFirstWeek' throws an IllegalArgumentException. The valid range is 1 to 7.
     */
    @Test
    public void getInstance_withInvalidMinDaysInFirstWeek_shouldThrowIllegalArgumentException() {
        // Arrange: Set up the test data.
        // The specific time zone is not relevant to this test, so we use UTC for simplicity.
        DateTimeZone anyZone = DateTimeZone.UTC;

        // A negative value is used to test the validation for the minimum days in the first week.
        int invalidMinDaysInFirstWeek = -1686;

        // Act & Assert: Call the method and verify that it throws the expected exception.
        try {
            CopticChronology.getInstance(anyZone, invalidMinDaysInFirstWeek);
            fail("Expected an IllegalArgumentException to be thrown due to invalid 'minDaysInFirstWeek'.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct and informative.
            // This confirms the exception was thrown for the correct reason.
            String expectedMessage = "Invalid min days in first week: " + invalidMinDaysInFirstWeek;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}