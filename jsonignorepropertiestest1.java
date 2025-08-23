package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class, focusing on its
 * factory methods and the properties of the default "empty" instance.
 */
@DisplayName("JsonIgnoreProperties.Value")
class JsonIgnorePropertiesValueTest {

    @Test
    @DisplayName("The empty() instance should have correct default values")
    void emptyInstanceShouldHaveDefaultProperties() {
        // Arrange
        final JsonIgnoreProperties.Value emptyValue = JsonIgnoreProperties.Value.empty();

        // Assert: Check the properties of the default empty instance.
        // Based on its definition, the default instance should:
        // - have no ignored properties
        // - not ignore unknown properties (default: false)
        // - not allow getters for ignored properties (default: false)
        // - not allow setters for ignored properties (default: false)
        // - have merging enabled (default: true)
        assertTrue(emptyValue.getIgnored().isEmpty(),
                "Default ignored properties set should be empty");
        assertFalse(emptyValue.getIgnoreUnknown(),
                "Default ignoreUnknown should be false");
        assertFalse(emptyValue.getAllowGetters(),
                "Default allowGetters should be false");
        assertFalse(emptyValue.getAllowSetters(),
                "Default allowSetters should be false");
        assertTrue(emptyValue.getMerge(),
                "Default merge should be true");
    }

    @Test
    @DisplayName("The from(null) factory method should return the singleton empty instance")
    void fromNullAnnotationShouldReturnEmptyInstance() {
        // Act
        JsonIgnoreProperties.Value fromNullResult = JsonIgnoreProperties.Value.from(null);

        // Assert
        // The factory method `from(null)` is specified to return the singleton empty instance.
        assertSame(JsonIgnoreProperties.Value.empty(), fromNullResult,
                "Calling from(null) should return the singleton empty instance");
    }
}