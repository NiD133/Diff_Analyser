package org.jsoup.nodes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link Attribute#hasDeclaredValue()} method.
 */
@DisplayName("Attribute.hasDeclaredValue()")
public class AttributeTest {

    @Test
    @DisplayName("should return false when the value is null")
    void hasDeclaredValue_whenValueIsNull_returnsFalse() {
        // Arrange: An attribute with a null value, typical for boolean attributes like 'disabled'.
        Attribute attributeWithNullValue = new Attribute("key", null);

        // Act
        boolean hasValue = attributeWithNullValue.hasDeclaredValue();

        // Assert
        assertFalse(hasValue, "An attribute with a null value should not have a declared value.");
    }

    @Test
    @DisplayName("should return true when the value is an empty string")
    void hasDeclaredValue_whenValueIsEmptyString_returnsTrue() {
        // Arrange: An attribute with an empty string value, e.g., alt="".
        Attribute attributeWithEmptyValue = new Attribute("key", "");

        // Act
        boolean hasValue = attributeWithEmptyValue.hasDeclaredValue();

        // Assert
        assertTrue(hasValue, "An attribute with an empty string value should be considered as having a declared value.");
    }

    @Test
    @DisplayName("should return true when the value is a non-empty string")
    void hasDeclaredValue_whenValueIsNonEmpty_returnsTrue() {
        // Arrange: A standard attribute with a key and a populated value.
        Attribute attributeWithPopulatedValue = new Attribute("key", "value");

        // Act
        boolean hasValue = attributeWithPopulatedValue.hasDeclaredValue();

        // Assert
        assertTrue(hasValue, "An attribute with a non-empty value should have a declared value.");
    }
}