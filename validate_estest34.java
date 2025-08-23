package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the {@link Validate} class.
 */
public class ValidateTest {

    /**
     * Verifies that {@link Validate#isTrue(boolean, String)} does not throw an
     * exception when the provided condition is true. This is the expected "happy path"
     * behavior.
     */
    @Test
    public void isTrueShouldNotThrowExceptionWhenConditionIsTrue() {
        // This call is expected to succeed without throwing any exception.
        Validate.isTrue(true, "This message should not be displayed.");
    }
}