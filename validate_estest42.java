package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the {@link Validate} utility class.
 */
public class ValidateTest {

    /**
     * Verifies that {@link Validate#notNull(Object, String)} does not throw an exception
     * when provided with a non-null object. This tests the "happy path" scenario.
     */
    @Test
    public void notNullWithMessage_shouldNotThrowException_whenObjectIsNotNull() {
        // Arrange: Create a non-null object to be validated.
        Object nonNullObject = new Object();
        String errorMessage = "The object should not be null, but it was.";

        // Act & Assert: The test will pass if no exception is thrown.
        // An exception would indicate a failure in the validation logic.
        Validate.notNull(nonNullObject, errorMessage);
    }
}