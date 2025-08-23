package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Validate} helper class.
 * This test focuses on the notEmptyParam() method.
 */
// Note: The original class name 'Validate_ESTestTest23' is preserved from the request.
// In a real-world scenario, this would be renamed to 'ValidateTest'.
public class Validate_ESTestTest23 {

    /**
     * Verifies that notEmptyParam() throws an IllegalArgumentException when the input string is empty.
     * The exception message should clearly state which parameter failed the validation.
     */
    @Test
    public void notEmptyParamShouldThrowExceptionForEmptyString() {
        // Arrange: Define a realistic parameter name and the expected exception message.
        String parameterName = "userName";
        String expectedMessage = "The '" + parameterName + "' parameter must not be empty.";

        try {
            // Act: Call the method with an empty string, which should trigger the exception.
            Validate.notEmptyParam("", parameterName);
            
            // If this line is reached, the test has failed because no exception was thrown.
            fail("Expected an IllegalArgumentException to be thrown for an empty parameter.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the caught exception has the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}