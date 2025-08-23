package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class, focusing on its
 * factory methods and configuration logic.
 */
@DisplayName("JsonIgnoreProperties.Value")
class JsonIgnorePropertiesValueTest {

    private static final JsonIgnoreProperties.Value EMPTY = JsonIgnoreProperties.Value.empty();

    /**
     * Helper to create a Set from a varargs array of strings.
     */
    private Set<String> asSet(String... args) {
        return new LinkedHashSet<>(Arrays.asList(args));
    }

    @Test
    @DisplayName("factories for empty or default values should return the singleton EMPTY instance")
    void shouldReturnEmptyInstanceForDefaultValues() {
        // When/Then: Factories called with default or empty values should return the canonical empty instance.
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoreUnknown(false));
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties());
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties(Collections.emptySet()));
    }

    @Test
    @DisplayName("factory forIgnoredProperties should create an instance with the specified properties")
    void shouldCreateInstanceWithIgnoredProperties() {
        // Given
        String[] propertiesToIgnore = {"prop1", "prop2"};

        // When
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoredProperties(propertiesToIgnore);

        // Then
        assertEquals(asSet("prop1", "prop2"), value.getIgnored());
        // And other properties should have default values
        assertFalse(value.getIgnoreUnknown());
        assertFalse(value.getAllowGetters());
        assertFalse(value.getAllowSetters());
    }

    @Test
    @DisplayName("withAllowGetters() should configure properties to be ignored only during deserialization")
    void withAllowGetters_shouldOnlyIgnorePropertiesForDeserialization() {
        // Given: A base configuration that ignores "prop1" and "prop2"
        JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value.forIgnoredProperties("prop1", "prop2");

        // When: We create a new configuration that allows getters
        JsonIgnoreProperties.Value valueWithGettersAllowed = baseValue.withAllowGetters();

        // Then: The configuration flags should be updated correctly
        assertTrue(valueWithGettersAllowed.getAllowGetters(), "allowGetters flag should be true");
        assertFalse(valueWithGettersAllowed.getAllowSetters(), "allowSetters flag should remain false");
        assertEquals(asSet("prop1", "prop2"), valueWithGettersAllowed.getIgnored(), "The base set of ignored properties should be unchanged");

        // And: The behavior for serialization and deserialization should be correct
        assertEquals(Collections.emptySet(), valueWithGettersAllowed.findIgnoredForSerialization(),
            "When getters are allowed, properties should NOT be ignored for serialization (i.e., they are read-only)");
        assertEquals(asSet("prop1", "prop2"), valueWithGettersAllowed.findIgnoredForDeserialization(),
            "Properties should still be ignored for deserialization");
    }

    @Test
    @DisplayName("withAllowSetters() should configure properties to be ignored only during serialization")
    void withAllowSetters_shouldOnlyIgnorePropertiesForSerialization() {
        // Given: A base configuration that ignores "prop1" and "prop2"
        JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value.forIgnoredProperties("prop1", "prop2");

        // When: We create a new configuration that allows setters
        JsonIgnoreProperties.Value valueWithSettersAllowed = baseValue.withAllowSetters();

        // Then: The configuration flags should be updated correctly
        assertTrue(valueWithSettersAllowed.getAllowSetters(), "allowSetters flag should be true");
        assertFalse(valueWithSettersAllowed.getAllowGetters(), "allowGetters flag should remain false");
        assertEquals(asSet("prop1", "prop2"), valueWithSettersAllowed.getIgnored(), "The base set of ignored properties should be unchanged");

        // And: The behavior for serialization and deserialization should be correct
        assertEquals(asSet("prop1", "prop2"), valueWithSettersAllowed.findIgnoredForSerialization(),
            "Properties should still be ignored for serialization");
        assertEquals(Collections.emptySet(), valueWithSettersAllowed.findIgnoredForDeserialization(),
            "When setters are allowed, properties should NOT be ignored for deserialization (i.e., they are write-only)");
    }
}