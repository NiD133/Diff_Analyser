package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for the {@link JsonTypeInfo.Value} class, focusing on the
 * {@code withRequireTypeIdForSubtypes} method and the class's immutability.
 */
@DisplayName("JsonTypeInfo.Value")
class JsonTypeInfoValueTest {

    @Test
    @DisplayName("EMPTY instance should have null for 'requireTypeIdForSubtypes'")
    void emptyValueShouldHaveNullRequireTypeId() {
        // Arrange
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;

        // Act & Assert
        assertNull(emptyValue.getRequireTypeIdForSubtypes(),
                "The default EMPTY value should not have a 'requireTypeIdForSubtypes' setting.");
    }

    @Test
    @DisplayName("withRequireTypeIdForSubtypes(TRUE) should create a new instance with the value set to TRUE")
    void withRequireTypeIdForSubtypesShouldSetToTrue() {
        // Arrange
        JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.EMPTY;

        // Act
        JsonTypeInfo.Value updatedValue = originalValue.withRequireTypeIdForSubtypes(Boolean.TRUE);

        // Assert
        assertNotSame(originalValue, updatedValue, "A new instance should be created (immutability).");
        assertEquals(Boolean.TRUE, updatedValue.getRequireTypeIdForSubtypes());
        assertNull(originalValue.getRequireTypeIdForSubtypes(), "The original instance should be unchanged.");
    }

    @Test
    @DisplayName("withRequireTypeIdForSubtypes(FALSE) should create a new instance with the value set to FALSE")
    void withRequireTypeIdForSubtypesShouldSetToFalse() {
        // Arrange
        JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.EMPTY;

        // Act
        JsonTypeInfo.Value updatedValue = originalValue.withRequireTypeIdForSubtypes(Boolean.FALSE);

        // Assert
        assertNotSame(originalValue, updatedValue, "A new instance should be created (immutability).");
        assertEquals(Boolean.FALSE, updatedValue.getRequireTypeIdForSubtypes());
        assertNull(originalValue.getRequireTypeIdForSubtypes(), "The original instance should be unchanged.");
    }

    @Test
    @DisplayName("withRequireTypeIdForSubtypes(null) should create a new instance with the value set to null")
    void withRequireTypeIdForSubtypesShouldSetToNull() {
        // Arrange: Start with a non-null value to ensure it can be reset.
        JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.EMPTY.withRequireTypeIdForSubtypes(Boolean.TRUE);

        // Act
        JsonTypeInfo.Value updatedValue = originalValue.withRequireTypeIdForSubtypes(null);

        // Assert
        assertNotSame(originalValue, updatedValue, "A new instance should be created (immutability).");
        assertNull(updatedValue.getRequireTypeIdForSubtypes());
        assertEquals(Boolean.TRUE, originalValue.getRequireTypeIdForSubtypes(), "The original instance should be unchanged.");
    }
}