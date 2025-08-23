package org.jsoup.helper;

import org.junit.Test;

/**
 * Tests for the {@link Validate} utility class.
 */
public class ValidateTest {

    @Test
    public void isFalse_withFalseCondition_doesNotThrowException() {
        // The purpose of this test is to verify that Validate.isFalse()
        // correctly handles a `false` input without throwing an exception.
        
        // The test implicitly passes if no exception is thrown.
        Validate.isFalse(false);
    }
}