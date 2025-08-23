package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Validate} helper class.
 */
public class ValidateTest {

    /**
     * Verifies that calling notNullParam with a null object throws an
     * IllegalArgumentException with a specific, helpful message.
     */
    @Test
    public void notNullParam_withNullObject_throwsIllegalArgumentException() {
        // Arrange
        String parameterName = "f";
        String expectedMessage = "The parameter 'f' must not be null.";

        // Act & Assert
        try {
            Validate.notNullParam(null, parameterName);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // This is the expected outcome. Now, we verify the message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}