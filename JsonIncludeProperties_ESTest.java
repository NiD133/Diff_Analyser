package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Helper to create a mock {@link JsonIncludeProperties} annotation
     * that returns the given property names from its value() method.
     */
    private JsonIncludeProperties mockAnnotation(String... properties) {
        JsonIncludeProperties mockAnn = mock(JsonIncludeProperties.class);
        doReturn(properties).when(mockAnn).value();
        return mockAnn;
    }

    // --- Factory Method Tests ---

    @Test
    public void from_shouldReturnAllInstance_whenAnnotationIsNull() {
        // Act
        JsonIncludeProperties.Value result = JsonIncludeProperties.Value.from(null);

        // Assert
        assertSame("from(null) should return the singleton ALL instance",
                JsonIncludeProperties.Value.ALL, result);
    }

    @Test
    public void from_shouldCreateValueWithEmptySet_whenAnnotationValueIsNull() {
        // Arrange: Mock an annotation where value() returns null
        JsonIncludeProperties mockAnn = mock(JsonIncludeProperties.class);
        doReturn(null).when(mockAnn).value();

        // Act
        JsonIncludeProperties.Value result = JsonIncludeProperties.Value.from(mockAnn);

        // Assert
        assertNotNull(result.getIncluded());
        assertTrue("A null property array should result in an empty set of includes",
                result.getIncluded().isEmpty());
    }

    @Test
    public void from_shouldCreateValueWithProperties_whenAnnotationHasValues() {
        // Arrange
        JsonIncludeProperties mockAnn = mockAnnotation("prop1", "prop2");
        Set<String> expected = new HashSet<>(Arrays.asList("prop1", "prop2"));

        // Act
        JsonIncludeProperties.Value result = JsonIncludeProperties.Value.from(mockAnn);

        // Assert
        assertEquals(expected, result.getIncluded());
    }

    @Test
    public void all_shouldReturnSharedAllInstance() {
        // Act
        JsonIncludeProperties.Value v1 = JsonIncludeProperties.Value.all();
        JsonIncludeProperties.Value v2 = JsonIncludeProperties.Value.all();

        // Assert
        assertSame(v1, v2);
        assertSame(JsonIncludeProperties.Value.ALL, v1);
    }

    // --- Accessor and Core Method Tests ---

    @Test
    public void getIncluded_shouldReturnNull_forAllInstance() {
        // Arrange
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;

        // Act & Assert
        assertNull("The ALL instance should have a null set of included properties",
                allValue.getIncluded());
    }

    @Test
    public void valueFor_shouldReturnJsonIncludePropertiesClass() {
        // Arrange
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.ALL;

        // Act & Assert
        assertEquals(JsonIncludeProperties.class, value.valueFor());
    }

    // --- withOverrides() Tests ---

    @Test
    public void withOverrides_shouldReturnOriginalValue_whenOverrideIsNull() {
        // Arrange
        JsonIncludeProperties.Value original = JsonIncludeProperties.Value.from(mockAnnotation("a"));

        // Act
        JsonIncludeProperties.Value result = original.withOverrides(null);

        // Assert
        assertSame("Overriding with null should have no effect", original, result);
    }

    @Test
    public void withOverrides_shouldReturnOriginalValue_whenOverridingWithAll() {
        // Arrange
        JsonIncludeProperties.Value original = JsonIncludeProperties.Value.from(mockAnnotation("a"));

        // Act
        JsonIncludeProperties.Value result = original.withOverrides(JsonIncludeProperties.Value.ALL);

        // Assert
        assertSame("Overriding with ALL should have no effect", original, result);
    }

    @Test
    public void withOverrides_shouldReturnOverrideValue_whenOriginalIsAll() {
        // Arrange
        JsonIncludeProperties.Value override = JsonIncludeProperties.Value.from(mockAnnotation("a"));

        // Act
        JsonIncludeProperties.Value result = JsonIncludeProperties.Value.ALL.withOverrides(override);

        // Assert
        assertSame("When the original is ALL, the override should be returned", override, result);
    }

    @Test
    public void withOverrides_shouldReturnIntersection_whenBothHaveIncludes() {
        // Arrange
        JsonIncludeProperties.Value original = JsonIncludeProperties.Value.from(mockAnnotation("a", "b"));
        JsonIncludeProperties.Value override = JsonIncludeProperties.Value.from(mockAnnotation("b", "c"));
        Set<String> expected = Collections.singleton("b");

        // Act
        JsonIncludeProperties.Value result = original.withOverrides(override);

        // Assert
        assertEquals("Result should be the intersection of the two sets of properties",
                expected, result.getIncluded());
        assertNotSame(original, result);
        assertNotSame(override, result);
    }

    // --- equals() and hashCode() Contract Tests ---

    @Test
    public void equals_shouldAdhereToContract() {
        // Arrange: v1 and v2 are equal
        JsonIncludeProperties.Value v1 = new JsonIncludeProperties.Value(new HashSet<>(Arrays.asList("a", "b")));
        JsonIncludeProperties.Value v2 = new JsonIncludeProperties.Value(new HashSet<>(Arrays.asList("b", "a")));

        // Arrange: v3 is different
        JsonIncludeProperties.Value v3 = new JsonIncludeProperties.Value(new HashSet<>(Arrays.asList("a", "c")));

        // Arrange: v4 and v5 represent the "ALL" case
        JsonIncludeProperties.Value v4 = JsonIncludeProperties.Value.ALL;
        JsonIncludeProperties.Value v5 = new JsonIncludeProperties.Value(null);

        // Assert: self-equality
        assertEquals(v1, v1);

        // Assert: symmetry and consistency for equal objects
        assertEquals(v1, v2);
        assertEquals(v2, v1);
        assertEquals(v1.hashCode(), v2.hashCode());

        // Assert: symmetry and consistency for non-equal objects
        assertNotEquals(v1, v3);
        assertNotEquals(v3, v1);

        // Assert: comparison with ALL
        assertNotEquals(v1, v4);
        assertNotEquals(v4, v1);

        // Assert: equality for two ALL instances
        assertEquals(v4, v5);
        assertEquals(v5, v4);
        assertEquals(v4.hashCode(), v5.hashCode());

        // Assert: comparison with other types and null
        assertNotEquals(v1, "some string");
        assertNotEquals(v1, null);
    }

    @Test
    public void equals_shouldDifferentiateBetweenAllAndEmpty() {
        // Arrange
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL; // includes = null
        JsonIncludeProperties.Value emptyValue = JsonIncludeProperties.Value.from(mockAnnotation()); // includes = empty set

        // Act & Assert
        assertNotEquals("ALL (null includes) should not equal a value with an empty set of includes",
                allValue, emptyValue);
    }

    // --- toString() Tests ---

    @Test
    public void toString_shouldReturnCorrectRepresentation_forAllValue() {
        // Arrange
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.ALL;

        // Act & Assert
        assertEquals("JsonIncludeProperties.Value(included=null)", value.toString());
    }

    @Test
    public void toString_shouldReturnCorrectRepresentation_forValueWithIncludes() {
        // Arrange
        Set<String> includes = new HashSet<>(Arrays.asList("a", "b"));
        JsonIncludeProperties.Value value = new JsonIncludeProperties.Value(includes);

        // Act
        String str = value.toString();

        // Assert: Set.toString() order is not guaranteed, so check for parts
        assertTrue(str.startsWith("JsonIncludeProperties.Value(included=["));
        assertTrue(str.endsWith("])"));
        assertTrue(str.contains("a"));
        assertTrue(str.contains("b"));
    }
}