package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link GregorianChronology}.
 */
// The original test class name and inheritance are kept for context.
public class GregorianChronology_ESTestTest12 extends GregorianChronology_ESTest_scaffolding {

    /**
     * Tests that getInstance() throws an IllegalArgumentException when the
     * 'minDaysInFirstWeek' parameter is outside the valid range (1-7).
     */
    @Test
    public void getInstance_withInvalidMinDaysInFirstWeek_throwsIllegalArgumentException() {
        // Arrange: Define an invalid value for the minimum days in the first week.
        // The valid range is 1 to 7.
        int invalidMinDaysInFirstWeek = -3203;
        String expectedMessage = "Invalid min days in first week: " + invalidMinDaysInFirstWeek;

        // Act & Assert: Attempt to create a chronology with the invalid value and
        // verify that the correct exception is thrown.
        try {
            GregorianChronology.getInstance((DateTimeZone) null, invalidMinDaysInFirstWeek);
            fail("Expected an IllegalArgumentException to be thrown for invalid min days in first week.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is as expected.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}