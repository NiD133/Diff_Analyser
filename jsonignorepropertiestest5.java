package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonIgnoreProperties.Value")
class JsonIgnorePropertiesValueTest {

    private final JsonIgnoreProperties.Value EMPTY_VALUE = JsonIgnoreProperties.Value.empty();

    private Set<String> asSet(String... args) {
        return new LinkedHashSet<>(Arrays.asList(args));
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethodsTest {

        @Test
        @DisplayName("should create a new instance with specified ignored properties")
        void withIgnored() {
            // Arrange
            Set<String> expectedIgnored = asSet("a", "b");

            // Act
            JsonIgnoreProperties.Value result = EMPTY_VALUE.withIgnored(expectedIgnored);

            // Assert
            assertEquals(expectedIgnored, result.getIgnored());
        }

        @Test
        @DisplayName("should create a new instance with specified ignored properties (var-args)")
        void withIgnoredVarargs() {
            // Act
            JsonIgnoreProperties.Value result = EMPTY_VALUE.withIgnored("a", "b");

            // Assert
            assertEquals(asSet("a", "b"), result.getIgnored());
        }

        @Test
        @DisplayName("should create a new instance with an empty set for null ignored properties")
        void withIgnoredNull() {
            // Act
            JsonIgnoreProperties.Value result = EMPTY_VALUE.withIgnored((Set<String>) null);

            // Assert
            assertTrue(result.getIgnored().isEmpty());
        }

        @Test
        @DisplayName("should create a new instance with 'ignoreUnknown' set to true")
        void withIgnoreUnknown() {
            // Act
            JsonIgnoreProperties.Value result = EMPTY_VALUE.withIgnoreUnknown();

            // Assert
            assertTrue(result.getIgnoreUnknown());
        }

        @Test
        @DisplayName("should create a new instance with 'ignoreUnknown' set to false")
        void withoutIgnoreUnknown() {
            // Arrange
            JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

            // Act
            JsonIgnoreProperties.Value result = initialValue.withoutIgnoreUnknown();

            // Assert
            assertFalse(result.getIgnoreUnknown());
        }

        @Test
        @DisplayName("should create a new instance with 'allowGetters' set to true")
        void withAllowGetters() {
            // Act
            JsonIgnoreProperties.Value result = EMPTY_VALUE.withAllowGetters();

            // Assert
            assertTrue(result.getAllowGetters());
        }

        @Test
        @DisplayName("should create a new instance with 'allowGetters' set to false")
        void withoutAllowGetters() {
            // Arrange
            JsonIgnoreProperties.Value initialValue = EMPTY_VALUE.withAllowGetters();

            // Act
            JsonIgnoreProperties.Value result = initialValue.withoutAllowGetters();

            // Assert
            assertFalse(result.getAllowGetters());
        }

        @Test
        @DisplayName("should create a new instance with 'allowSetters' set to true")
        void withAllowSetters() {
            // Act
            JsonIgnoreProperties.Value result = EMPTY_VALUE.withAllowSetters();

            // Assert
            assertTrue(result.getAllowSetters());
        }

        @Test
        @DisplayName("should create a new instance with 'allowSetters' set to false")
        void withoutAllowSetters() {
            // Arrange
            JsonIgnoreProperties.Value initialValue = EMPTY_VALUE.withAllowSetters();

            // Act
            JsonIgnoreProperties.Value result = initialValue.withoutAllowSetters();

            // Assert
            assertFalse(result.getAllowSetters());
        }

        @Test
        @DisplayName("should create a new instance with 'merge' set to true")
        void withMerge() {
            // Act
            JsonIgnoreProperties.Value result = EMPTY_VALUE.withMerge();

            // Assert
            assertTrue(result.getMerge());
        }

        @Test
        @DisplayName("should create a new instance with 'merge' set to false")
        void withoutMerge() {
            // Arrange
            JsonIgnoreProperties.Value initialValue = EMPTY_VALUE.withMerge();

            // Act
            JsonIgnoreProperties.Value result = initialValue.withoutMerge();

            // Assert
            assertFalse(result.getMerge());
        }
        
        @Test
        @DisplayName("should be immutable and not modify the original instance")
        void shouldBeImmutable() {
            // Arrange
            JsonIgnoreProperties.Value original = JsonIgnoreProperties.Value.empty();
            JsonIgnoreProperties.Value originalSnapshot = JsonIgnoreProperties.Value.empty();

            // Act
            original.withIgnored("a", "b");
            original.withIgnoreUnknown();
            original.withoutIgnoreUnknown();
            original.withAllowGetters();
            original.withoutAllowGetters();
            original.withAllowSetters();
            original.withoutAllowSetters();
            original.withMerge();
            original.withoutMerge();

            // Assert
            assertEquals(originalSnapshot, original, "The original Value instance should not be modified.");
        }
    }
}