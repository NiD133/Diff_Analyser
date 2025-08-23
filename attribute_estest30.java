package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Attribute} class.
 * This snippet focuses on constructor validation.
 */
public class AttributeTest {

    /**
     * Verifies that the Attribute constructor throws an IllegalArgumentException
     * when initialized with an empty string as the key.
     */
    @Test
    public void constructorShouldThrowExceptionForEmptyAttributeKey() {
        // The Attribute constructor must not accept an empty key.
        // The value is irrelevant for this test case.
        String emptyKey = "";
        String anyValue = "some-value";

        try {
            new Attribute(emptyKey, anyValue);
            fail("Expected an IllegalArgumentException to be thrown for an empty attribute key, but it was not.");
        } catch (IllegalArgumentException e) {
            // Assert that the correct exception was thrown with the expected message.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}