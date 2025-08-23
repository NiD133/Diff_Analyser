package com.fasterxml.jackson.annotation;

import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for verifying the behavior of {@link JsonIncludeProperties.Value}
 * instances, particularly focusing on merging and overriding behaviors.
 */
public class JsonIncludePropertiesTest {

    // Test class annotated with JsonIncludeProperties
    @JsonIncludeProperties(value = {"foo", "bar"})
    private static final class AnnotatedClass {}

    // Constant representing a Value instance with all properties included
    private static final JsonIncludeProperties.Value ALL_PROPERTIES_INCLUDED = JsonIncludeProperties.Value.all();

    @Test
    public void shouldReturnAllPropertiesWhenSourceIsNull() {
        // Verify that when source is null, the ALL instance is returned
        assertSame(ALL_PROPERTIES_INCLUDED, JsonIncludeProperties.Value.from(null));
        assertNull(ALL_PROPERTIES_INCLUDED.getIncluded());
        assertEquals(ALL_PROPERTIES_INCLUDED, ALL_PROPERTIES_INCLUDED);
        assertEquals("JsonIncludeProperties.Value(included=null)", ALL_PROPERTIES_INCLUDED.toString());
        assertEquals(0, ALL_PROPERTIES_INCLUDED.hashCode());
    }

    @Test
    public void shouldCreateValueFromAnnotation() {
        // Create a Value instance from the annotation on AnnotatedClass
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.from(AnnotatedClass.class.getAnnotation(JsonIncludeProperties.class));
        assertNotNull(value);

        // Verify the included properties
        Set<String> includedProperties = value.getIncluded();
        assertEquals(2, includedProperties.size());
        assertEquals(createSet("foo", "bar"), includedProperties);

        // Verify the string representation
        String representation = value.toString();
        boolean isCorrectRepresentation = representation.equals("JsonIncludeProperties.Value(included=[foo, bar])")
                || representation.equals("JsonIncludeProperties.Value(included=[bar, foo])");
        assertTrue(isCorrectRepresentation);

        // Verify equality with another instance created from the same annotation
        assertEquals(value, JsonIncludeProperties.Value.from(AnnotatedClass.class.getAnnotation(JsonIncludeProperties.class)));
    }

    @Test
    public void shouldOverrideWithAllProperties() {
        // Create a Value instance and override it with ALL_PROPERTIES_INCLUDED
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.from(AnnotatedClass.class.getAnnotation(JsonIncludeProperties.class));
        value = value.withOverrides(ALL_PROPERTIES_INCLUDED);

        // Verify the included properties remain unchanged
        Set<String> includedProperties = value.getIncluded();
        assertEquals(2, includedProperties.size());
        assertEquals(createSet("foo", "bar"), includedProperties);
    }

    @Test
    public void shouldOverrideWithEmptySet() {
        // Create a Value instance and override it with an empty set
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.from(AnnotatedClass.class.getAnnotation(JsonIncludeProperties.class));
        value = value.withOverrides(new JsonIncludeProperties.Value(Collections.emptySet()));

        // Verify that no properties are included
        Set<String> includedProperties = value.getIncluded();
        assertEquals(0, includedProperties.size());
    }

    @Test
    public void shouldMergeWithSpecificProperties() {
        // Create a Value instance and override it with a specific set of properties
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.from(AnnotatedClass.class.getAnnotation(JsonIncludeProperties.class));
        value = value.withOverrides(new JsonIncludeProperties.Value(createSet("foo")));

        // Verify that only the specified properties are included
        Set<String> includedProperties = value.getIncluded();
        assertEquals(1, includedProperties.size());
        assertEquals(createSet("foo"), includedProperties);
    }

    // Helper method to create a set from given arguments
    private Set<String> createSet(String... elements) {
        return new LinkedHashSet<>(Arrays.asList(elements));
    }
}