package com.google.common.util.concurrent;

import static org.junit.Assert.assertNotNull;

import com.google.common.util.concurrent.FuturesGetChecked.GetCheckedTypeValidator;
import org.junit.Test;

/**
 * Test suite for {@link FuturesGetChecked}.
 *
 * Note: The original test class name "FuturesGetChecked_ESTestTest14" and its
 * inheritance from a scaffolding class suggest it was auto-generated. This
 * version uses a more conventional name and structure for better clarity.
 */
public class FuturesGetCheckedTest {

    /**
     * Verifies that the weakSetValidator() factory method successfully returns a non-null instance.
     *
     * This is a simple smoke test to ensure the method, which retrieves a singleton instance
     * of the WeakSetValidator, can be called without throwing an exception and that it
     * returns the expected object.
     */
    @Test
    public void weakSetValidator_shouldReturnNonNullInstance() {
        // Act: Call the factory method to get the validator.
        GetCheckedTypeValidator validator = FuturesGetChecked.weakSetValidator();

        // Assert: The returned validator instance must not be null.
        assertNotNull("The weakSetValidator() should always return a non-null instance.", validator);
    }
}