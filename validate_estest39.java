package org.jsoup.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Validate} utility class.
 */
public class ValidateTest {

    /**
     * Verifies that the deprecated {@link Validate#ensureNotNull(Object)} method
     * throws an {@link IllegalArgumentException} when a null object is provided.
     */
    @Test
    public void ensureNotNullWithNullInputShouldThrowException() {
        try {
            Validate.ensureNotNull(null);
            fail("Expected an IllegalArgumentException to be thrown, but no exception occurred.");
        } catch (IllegalArgumentException e) {
            // This is the expected behavior. Now, we verify the exception message.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}