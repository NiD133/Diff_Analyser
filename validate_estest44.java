package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the Validate helper class.
 */
public class ValidateTest {

    /**
     * Verifies that notNullParam() does not throw an exception when the provided object is not null.
     */
    @Test
    public void notNullParamShouldNotThrowExceptionForNonNullObject() {
        // Arrange
        Object nonNullObject = "I am not null";
        String parameterName = "testParameter";

        // Act & Assert
        // The test's purpose is to ensure this method call completes without throwing an exception.
        // JUnit will automatically fail the test if any unexpected exception is thrown.
        Validate.notNullParam(nonNullObject, parameterName);
    }
}