package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test suite contains tests for the CopticChronology class.
 */
public class CopticChronologyTest {

    /**
     * Tests that the CopticChronology constructor throws an IllegalArgumentException
     * if the 'minDaysInFirstWeek' parameter is outside the valid range of 1-7.
     */
    @Test
    public void constructorShouldThrowExceptionForInvalidMinDaysInFirstWeek() {
        // Arrange: Set up the test conditions.
        // The valid range for minDaysInFirstWeek is 1 to 7. We use an invalid value.
        final int invalidMinDays = 634;
        final CopticChronology baseChronology = CopticChronology.getInstanceUTC();
        final String expectedMessage = "Invalid min days in first week: " + invalidMinDays;

        // Act & Assert: Attempt to create an instance with the invalid parameter
        // and verify that the correct exception is thrown.
        try {
            new CopticChronology(baseChronology, null, invalidMinDays);
            fail("Expected an IllegalArgumentException to be thrown due to invalid 'minDaysInFirstWeek'.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is as expected.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}