package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class, focusing on
 * its core object methods like {@code toString()} and the {@code hashCode()}/{@code equals()} contract.
 */
class JsonIgnorePropertiesValueTest {

    @Test
    @DisplayName("toString() should generate a correct string representation of the Value's state")
    void toStringShouldGenerateCorrectRepresentation() {
        // Arrange
        // Start with an empty Value and modify it to create a specific state.
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.empty()
                .withAllowSetters()
                .withMerge();

        String expected = "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=false,allowGetters=false,allowSetters=true,merge=true)";

        // Act
        String actual = value.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("hashCode() should be consistent for equal Value objects")
    void hashCodeShouldBeConsistentForEqualObjects() {
        // Arrange
        // Create two Value objects that should be considered equal.
        JsonIgnoreProperties.Value value1 = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b")
                .withIgnoreUnknown();
        JsonIgnoreProperties.Value value2 = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b")
                .withIgnoreUnknown();

        // Assert
        // First, confirm they are indeed equal.
        assertEquals(value1, value2, "Two Value objects with the same properties should be equal.");

        // Then, assert that their hash codes are also equal, as required by the hashCode contract.
        assertEquals(value1.hashCode(), value2.hashCode(), "Equal objects must have the same hash code.");
    }

    @Test
    @DisplayName("hashCode() should differ for unequal Value objects")
    void hashCodeShouldDifferForUnequalObjects() {
        // Arrange
        // Create two Value objects that have different properties.
        JsonIgnoreProperties.Value value1 = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");
        JsonIgnoreProperties.Value value2 = JsonIgnoreProperties.Value.forIgnoredProperties("a", "c"); // Different ignored property
        JsonIgnoreProperties.Value value3 = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b")
                .withIgnoreUnknown(); // Different ignoreUnknown flag

        // Assert
        // First, confirm they are not equal.
        assertNotEquals(value1, value2, "Value objects with different ignored properties should not be equal.");
        assertNotEquals(value1, value3, "Value objects with a different 'ignoreUnknown' flag should not be equal.");

        // Assert that their hash codes are different. While not a strict guarantee due to
        // potential collisions, it's a strong indicator of a correct hashCode implementation.
        assertNotEquals(value1.hashCode(), value2.hashCode(), "Unequal objects should have different hash codes.");
        assertNotEquals(value1.hashCode(), value3.hashCode(), "Unequal objects should have different hash codes.");
    }
}