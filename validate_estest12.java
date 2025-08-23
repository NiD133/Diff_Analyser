package org.jsoup.helper;

import org.junit.Test;
import java.util.MissingFormatArgumentException;

/**
 * Test suite for the {@link Validate} class.
 */
public class ValidateTest {

    /**
     * Verifies that ensureNotNull throws a MissingFormatArgumentException when the object is null
     * and the error message template requires arguments that are not provided.
     *
     * This scenario tests the exception handling of the message formatting itself, which occurs
     * after the primary validation (the null check) fails.
     */
    @Test(expected = MissingFormatArgumentException.class)
    public void ensureNotNullThrowsMissingFormatArgumentExceptionForMismatchedMessageArgs() {
        // Arrange: A null object to trigger the validation failure, and a message template
        // that requires one argument ('%s'), but with no arguments provided.
        final Object objectToValidate = null;
        final String errorMessageTemplate = "The '%s' parameter must not be empty.";
        final Object[] noArguments = new Object[0];

        // Act: Call the method under test. This is expected to throw.
        Validate.ensureNotNull(objectToValidate, errorMessageTemplate, noArguments);

        // Assert: The test will pass only if a MissingFormatArgumentException is thrown,
        // as specified by the `expected` parameter in the @Test annotation.
    }
}