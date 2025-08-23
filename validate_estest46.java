package org.jsoup.helper;

import org.junit.Test;

/**
 * Test suite for the {@link Validate} utility class.
 */
public class ValidateTest {

    @Test
    public void notNullWithNonNullObjectDoesNotThrowException() {
        // This test covers the "happy path" scenario for Validate.notNull().
        // The method is expected to complete without throwing an exception
        // when provided with a non-null object.
        Validate.notNull(new Object());
    }
}