package org.jsoup.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Verifies that {@link Validate#isFalse(boolean)} throws an IllegalArgumentException
     * when the input condition is true, which is the failure case for this validation method.
     */
    @Test
    public void isFalse_withTrueCondition_throwsIllegalArgumentException() {
        try {
            // Act: Call the method with a value that should trigger the exception.
            Validate.isFalse(true);

            // This line should not be reached. If it is, the test fails because
            // the expected exception was not thrown.
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Assert: The correct exception was thrown. Now, verify its message.
            assertEquals("Must be false", e.getMessage());
        }
    }
}