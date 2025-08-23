package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class, focusing on its
 * construction from an annotation and its core methods like {@code toString()},
 * {@code equals()}, and {@code hashCode()}.
 */
class JsonIncludePropertiesValueTest {

    // A simple class holding the annotation for testing purposes.
    @JsonIncludeProperties(value = {"foo", "bar"})
    private static class AnnotatedClass {
    }

    private JsonIncludeProperties.Value valueFromAnnotation;

    @BeforeEach
    void setUp() {
        // Arrange: Create a Value instance from a sample annotation before each test.
        JsonIncludeProperties annotation = AnnotatedClass.class.getAnnotation(JsonIncludeProperties.class);
        valueFromAnnotation = JsonIncludeProperties.Value.from(annotation);
    }

    /**
     * Helper method to create a Set from a varargs array for expected values.
     * Using LinkedHashSet to match the implementation detail, though order is not
     * strictly guaranteed by the annotation contract.
     */
    private Set<String> asSet(String... names) {
        return new LinkedHashSet<>(Arrays.asList(names));
    }

    @Test
    void from_shouldExtractPropertiesFromAnnotation() {
        // Arrange
        Set<String> expectedProperties = asSet("foo", "bar");

        // Act
        Set<String> actualProperties = valueFromAnnotation.getIncluded();

        // Assert
        assertNotNull(actualProperties, "The set of included properties should not be null.");
        assertEquals(expectedProperties, actualProperties, "The set of included properties should match the annotation.");
    }

    @Test
    void toString_shouldProvideClearRepresentation() {
        // Act
        String actualString = valueFromAnnotation.toString();

        // Assert
        // The order of elements in an annotation array is not guaranteed by the JLS.
        // Therefore, we test for the static parts and the presence of each element
        // to make the test robust against ordering variations.
        assertTrue(actualString.startsWith("JsonIncludeProperties.Value(included=["));
        assertTrue(actualString.endsWith("])"));
        assertTrue(actualString.contains("foo"));
        assertTrue(actualString.contains("bar"));
    }

    @Test
    void equalsAndHashCode_shouldBeConsistentForIdenticalValues() {
        // Arrange: Create another identical Value instance for comparison.
        JsonIncludeProperties annotation = AnnotatedClass.class.getAnnotation(JsonIncludeProperties.class);
        JsonIncludeProperties.Value anotherValue = JsonIncludeProperties.Value.from(annotation);

        // Assert
        // 1. Test for equality: two instances created from the same source should be equal.
        assertEquals(valueFromAnnotation, anotherValue, "Two Value instances from the same source should be equal.");

        // 2. Test for hashCode consistency as per the equals/hashCode contract.
        assertEquals(valueFromAnnotation.hashCode(), anotherValue.hashCode(), "Hash codes should be equal for equal objects.");
    }
}