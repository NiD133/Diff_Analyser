package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the {@link Validate} utility class.
 */
public class ValidateTest {

    /**
     * Tests the happy path for {@link Validate#notEmpty(String)}, ensuring it
     * does not throw an exception when provided with a valid, non-empty string.
     */
    @Test
    public void notEmptyShouldNotThrowExceptionForValidString() {
        // This test is successful if the following call completes without throwing an exception.
        Validate.notEmpty("some valid string");
    }
}