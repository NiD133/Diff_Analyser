package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Verifies that calling notEmpty() with a null string argument
     * throws an IllegalArgumentException with a specific message.
     */
    @Test
    public void notEmptyThrowsIllegalArgumentExceptionForNullString() {
        // Arrange: Define the expected exception type and message.
        String expectedMessage = "String must not be empty";

        try {
            // Act: Call the method that is expected to throw.
            Validate.notEmpty((String) null);
            
            // Assert: Fail the test if no exception was thrown.
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the caught exception has the correct message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}