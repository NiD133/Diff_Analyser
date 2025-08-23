package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that creating an Attribute with an empty key string is disallowed.
     * The {@link Attribute#createFromEncoded(String, String)} factory method should throw
     * an {@link IllegalArgumentException} because attribute keys must be non-empty.
     */
    @Test
    public void createFromEncodedShouldThrowExceptionForEmptyKey() {
        // Arrange: Define an empty key, which is invalid. The value is arbitrary for this test.
        String emptyKey = "";
        String anyValue = "any-value";

        // Act & Assert: Call the method and verify that it throws the expected exception.
        // The `assertThrows` method is a modern, declarative way to test for exceptions.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> Attribute.createFromEncoded(emptyKey, anyValue)
        );

        // Assert: Further verify that the exception message is correct, confirming the validation reason.
        assertEquals("String must not be empty", thrown.getMessage());
    }
}