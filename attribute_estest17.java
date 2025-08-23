package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Attribute} class.
 * This is an improved version of an automatically generated test.
 */
// The original class name "Attribute_ESTestTest17" is kept, but a more conventional
// name would be "AttributeTest".
public class Attribute_ESTestTest17 {

    /**
     * Verifies that calling {@link Attribute#setKey(String)} with an empty string
     * throws an {@link IllegalArgumentException}, as attribute keys must not be empty.
     */
    @Test
    public void setKeyWithEmptyStringShouldThrowIllegalArgumentException() {
        // Arrange: Create an attribute with a valid initial key.
        Attribute attribute = new Attribute("initial-key", "value");

        // Act & Assert: Attempt to set the key to an empty string and verify the resulting exception.
        try {
            attribute.setKey("");
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify the exception message is as expected, confirming the correct validation is in place.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}