package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for the {@link JsonTypeInfo.Value} class.
 */
@DisplayName("JsonTypeInfo.Value")
class JsonTypeInfoValueTest {

    @Test
    @DisplayName("from(null) should return null to represent the absence of an annotation")
    void from_givenNullAnnotation_shouldReturnNull() {
        // This test verifies that passing a null JsonTypeInfo annotation to the `from()`
        // factory method results in a null Value object. This is the desired behavior
        // to distinguish the complete absence of an annotation from an annotation
        // present with default or "empty" values.

        // When
        JsonTypeInfo.Value result = JsonTypeInfo.Value.from(null);

        // Then
        assertNull(result, "A null annotation should produce a null Value object, not an empty one.");
    }
}