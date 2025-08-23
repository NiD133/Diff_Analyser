package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the {@link Validate} utility class.
 */
public class ValidateTest {

    /**
     * Tests that {@link Validate#isTrue(boolean)} completes normally (does not throw an exception)
     * when the provided condition is {@code true}. This verifies the "happy path" scenario.
     */
    @Test
    public void isTrueWithTrueConditionSucceeds() {
        // The expectation is that this call does not throw an exception.
        // The test will pass if no exception is thrown.
        Validate.isTrue(true);
    }
}