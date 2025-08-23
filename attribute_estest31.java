package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the static utility methods in the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that {@link Attribute#isBooleanAttribute(String)} returns false for an empty string.
     * An empty string is not a valid attribute key and thus should not be classified as a boolean attribute.
     */
    @Test
    public void isBooleanAttributeShouldReturnFalseForEmptyKey() {
        // Arrange: The input is an empty string, which is an invalid attribute key.
        String emptyKey = "";

        // Act: Call the method under test.
        boolean result = Attribute.isBooleanAttribute(emptyKey);

        // Assert: The result should be false, as an empty key cannot be a boolean attribute.
        assertFalse("isBooleanAttribute should return false for an empty key.", result);
    }
}