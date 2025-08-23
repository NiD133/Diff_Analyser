package org.jsoup.helper;

import org.junit.Test;

/**
 * Test case for the {@link Validate#isFalse(boolean, String)} method.
 */
public class ValidateTest {

    /**
     * Verifies that Validate.isFalse() completes normally (does not throw an exception)
     * when the condition it checks is indeed false.
     */
    @Test
    public void isFalseShouldNotThrowExceptionWhenConditionIsFalse() {
        // The test's success is confirmed by this method call completing without
        // throwing a ValidationException or any other error.
        Validate.isFalse(false, "This message should not be seen");
    }
}