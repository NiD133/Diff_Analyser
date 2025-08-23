package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class, focusing on its
 * equality logic and immutability helpers.
 */
@DisplayName("JsonIgnoreProperties.Value Equality")
class JsonIgnorePropertiesValueEqualityTest {

    @Test
    @DisplayName("should be equal to itself but not to an instance with different properties")
    void testEqualsContract() {
        // Arrange
        final JsonIgnoreProperties.Value emptyValue = JsonIgnoreProperties.Value.empty();
        // Create a new instance that differs only by the 'merge' property.
        final JsonIgnoreProperties.Value valueWithoutMerge = emptyValue.withoutMerge();

        // Assert

        // 1. Reflexivity: An object must be equal to itself.
        assertEquals(emptyValue, emptyValue, "An instance should be equal to itself");
        assertEquals(valueWithoutMerge, valueWithoutMerge, "A modified instance should be equal to itself");

        // 2. Symmetry and inequality: Two objects with different properties should not be equal.
        // The 'emptyValue' has merge=true, while 'valueWithoutMerge' has merge=false.
        assertNotEquals(emptyValue, valueWithoutMerge, "Instances with different 'merge' values should not be equal");
        assertNotEquals(valueWithoutMerge, emptyValue, "Equality check should be symmetric");
    }

    @Test
    @DisplayName("withMerge() should return the same instance if merge is already true")
    void testWithMergeIsIdempotentForEmptyValue() {
        // Arrange
        final JsonIgnoreProperties.Value emptyValue = JsonIgnoreProperties.Value.empty();
        // The default empty() value has its 'merge' property set to true.

        // Act & Assert
        // Calling withMerge() on an instance that already has merge=true should be a no-op
        // and return the same instance for efficiency.
        assertSame(emptyValue, emptyValue.withMerge(),
                "withMerge() on the default EMPTY value should return the same instance");
    }
}