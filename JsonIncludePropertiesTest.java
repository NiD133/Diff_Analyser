package com.fasterxml.jackson.annotation;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to verify that {@link JsonIncludeProperties.Value} instances can be created,
 * merged, and overridden correctly for property inclusion configuration.
 */
public class JsonIncludePropertiesTest {

    // Test fixture: A class annotated with JsonIncludeProperties containing "foo" and "bar"
    @JsonIncludeProperties(value = {"foo", "bar"})
    private final static class ClassWithIncludedProperties {
    }

    // Constant representing the "include all properties" configuration
    private final JsonIncludeProperties.Value INCLUDE_ALL_PROPERTIES = JsonIncludeProperties.Value.all();

    @Test
    public void testAllPropertiesIncluded_WhenCreatedFromNullAnnotation() {
        // Given: No annotation (null input)
        JsonIncludeProperties.Value result = JsonIncludeProperties.Value.from(null);
        
        // Then: Should return the singleton "all properties" instance
        assertSame(INCLUDE_ALL_PROPERTIES, result);
        assertNull(INCLUDE_ALL_PROPERTIES.getIncluded(), "All properties mode should have null included set");
        assertEquals(INCLUDE_ALL_PROPERTIES, INCLUDE_ALL_PROPERTIES, "Should be equal to itself");
        assertEquals("JsonIncludeProperties.Value(included=null)", INCLUDE_ALL_PROPERTIES.toString());
        assertEquals(0, INCLUDE_ALL_PROPERTIES.hashCode(), "All properties mode should have consistent hash code");
    }

    @Test
    public void testSpecificPropertiesIncluded_WhenCreatedFromAnnotation() {
        // Given: An annotation with specific properties ("foo", "bar")
        JsonIncludeProperties annotation = ClassWithIncludedProperties.class.getAnnotation(JsonIncludeProperties.class);
        
        // When: Creating Value from annotation
        JsonIncludeProperties.Value result = JsonIncludeProperties.Value.from(annotation);
        
        // Then: Should contain exactly the annotated properties
        assertNotNull(result, "Value should be created from annotation");
        
        Set<String> includedProperties = result.getIncluded();
        assertEquals(2, includedProperties.size(), "Should include exactly 2 properties");
        assertEquals(createSet("foo", "bar"), includedProperties, "Should include 'foo' and 'bar'");
        
        // Verify string representation (order may vary due to Set implementation)
        String stringRepresentation = result.toString();
        boolean hasExpectedFormat = stringRepresentation.equals("JsonIncludeProperties.Value(included=[foo, bar])") ||
                                   stringRepresentation.equals("JsonIncludeProperties.Value(included=[bar, foo])");
        assertTrue(hasExpectedFormat, "String representation should contain both properties");
        
        // Verify equality with another instance created from same annotation
        JsonIncludeProperties.Value duplicate = JsonIncludeProperties.Value.from(annotation);
        assertEquals(result, duplicate, "Values created from same annotation should be equal");
    }

    @Test
    public void testOriginalPropertiesPreserved_WhenOverridingWithAllProperties() {
        // Given: A Value with specific properties ("foo", "bar")
        JsonIncludeProperties annotation = ClassWithIncludedProperties.class.getAnnotation(JsonIncludeProperties.class);
        JsonIncludeProperties.Value originalValue = JsonIncludeProperties.Value.from(annotation);
        
        // When: Overriding with "include all properties"
        JsonIncludeProperties.Value result = originalValue.withOverrides(INCLUDE_ALL_PROPERTIES);
        
        // Then: Original specific properties should be preserved (not expanded to "all")
        Set<String> includedProperties = result.getIncluded();
        assertEquals(2, includedProperties.size(), "Should still include exactly 2 properties");
        assertEquals(createSet("foo", "bar"), includedProperties, "Should preserve original properties");
    }

    @Test
    public void testEmptyPropertiesResult_WhenOverridingWithEmptySet() {
        // Given: A Value with specific properties ("foo", "bar")
        JsonIncludeProperties annotation = ClassWithIncludedProperties.class.getAnnotation(JsonIncludeProperties.class);
        JsonIncludeProperties.Value originalValue = JsonIncludeProperties.Value.from(annotation);
        
        // When: Overriding with empty property set
        JsonIncludeProperties.Value emptyOverride = new JsonIncludeProperties.Value(Collections.<String>emptySet());
        JsonIncludeProperties.Value result = originalValue.withOverrides(emptyOverride);
        
        // Then: Result should have no included properties (intersection is empty)
        Set<String> includedProperties = result.getIncluded();
        assertEquals(0, includedProperties.size(), "Override with empty set should result in no included properties");
    }

    @Test
    public void testIntersectionOfProperties_WhenOverridingWithPartialMatch() {
        // Given: A Value with specific properties ("foo", "bar")
        JsonIncludeProperties annotation = ClassWithIncludedProperties.class.getAnnotation(JsonIncludeProperties.class);
        JsonIncludeProperties.Value originalValue = JsonIncludeProperties.Value.from(annotation);
        
        // When: Overriding with a set that partially overlaps (only "foo")
        JsonIncludeProperties.Value partialOverride = new JsonIncludeProperties.Value(createSet("foo"));
        JsonIncludeProperties.Value result = originalValue.withOverrides(partialOverride);
        
        // Then: Result should contain only the intersection ("foo")
        Set<String> includedProperties = result.getIncluded();
        assertEquals(1, includedProperties.size(), "Should include only the intersecting property");
        assertEquals(createSet("foo"), includedProperties, "Should include only 'foo' (the intersection)");
    }

    /**
     * Helper method to create a LinkedHashSet from string arguments.
     * Uses LinkedHashSet to maintain insertion order for predictable testing.
     */
    private Set<String> createSet(String... propertyNames) {
        return new LinkedHashSet<>(Arrays.asList(propertyNames));
    }
}