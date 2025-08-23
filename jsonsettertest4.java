package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its factory methods
 * and construction logic.
 */
@DisplayName("JsonSetter.Value")
class JsonSetterValueTest {

    @Test
    @DisplayName("construct() with null arguments should return the singleton empty instance")
    void constructWithNullsShouldReturnEmptyInstance() {
        // Arrange
        // The 'empty' instance is a pre-existing singleton, retrieved via its factory method.
        final JsonSetter.Value expectedEmptyInstance = JsonSetter.Value.empty();

        // Act
        JsonSetter.Value actualInstance = JsonSetter.Value.construct(null, null);

        // Assert
        assertSame(expectedEmptyInstance, actualInstance,
                "Calling construct(null, null) should return the same singleton instance as empty()");
    }
}