package com.fasterxml.jackson.annotation;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonIncludePropertiesTest {
    @JsonIncludeProperties(value = {"foo", "bar"})
    private static class Bogus { }

    private static final JsonIncludeProperties.Value ALL = JsonIncludeProperties.Value.all();

    // Helper to create predictable ordered sets
    private Set<String> createSet(String... elements) {
        return new LinkedHashSet<>(Arrays.asList(elements));
    }

    @Test
    void testAllValueProperties() {
        // Verify static 'ALL' instance behavior
        assertSame(ALL, JsonIncludeProperties.Value.from(null),
            "Value.from(null) should return ALL instance");
        assertNull(ALL.getIncluded(),
            "ALL instance should have null included set");
        assertEquals(ALL, ALL,
            "ALL instance should equal itself");
        assertEquals(0, ALL.hashCode(),
            "ALL instance hashcode should be 0");
        assertEquals("JsonIncludeProperties.Value(included=null)", ALL.toString(),
            "ALL instance should have expected string representation");
    }

    @Test
    void testValueCreationFromAnnotation() {
        // Create Value from class annotation and verify contents
        JsonIncludeProperties ann = Bogus.class.getAnnotation(JsonIncludeProperties.class);
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.from(ann);

        assertNotNull(value, "Created Value should not be null");
        Set<String> included = value.getIncluded();
        assertEquals(2, included.size(),
            "Should contain exactly 2 properties");
        assertEquals(createSet("foo", "bar"), included,
            "Should contain 'foo' and 'bar'");

        // Validate toString() has one of the expected outputs
        String stringRep = value.toString();
        Set<String> validReps = new HashSet<>(Arrays.asList(
            "JsonIncludeProperties.Value(included=[foo, bar])",
            "JsonIncludeProperties.Value(included=[bar, foo])"
        ));
        assertTrue(validReps.contains(stringRep),
            "Unexpected string representation: " + stringRep);

        // Check equality
        JsonIncludeProperties.Value sameValue = JsonIncludeProperties.Value.from(ann);
        assertEquals(value, sameValue,
            "Equivalent annotations should produce equal Value instances");
    }

    @Test
    void testOverridesWithAllValue() {
        // Verify ALL override doesn't change original properties
        JsonIncludeProperties.Value original = getBogusValue();
        JsonIncludeProperties.Value merged = original.withOverrides(ALL);

        Set<String> included = merged.getIncluded();
        assertEquals(2, included.size(),
            "Merged with ALL should retain original 2 properties");
        assertEquals(createSet("foo", "bar"), included,
            "Merged with ALL should retain 'foo' and 'bar'");
    }

    @Test
    void testOverridesWithEmptyValue() {
        // Verify empty override clears all properties
        JsonIncludeProperties.Value original = getBogusValue();
        JsonIncludeProperties.Value emptyOverride = new JsonIncludeProperties.Value(Collections.emptySet());
        JsonIncludeProperties.Value merged = original.withOverrides(emptyOverride);

        Set<String> included = merged.getIncluded();
        assertTrue(included.isEmpty(),
            "Merged with empty override should result in no included properties");
    }

    @Test
    void testOverridesWithPropertyIntersection() {
        // Verify override intersects with original properties
        JsonIncludeProperties.Value original = getBogusValue();
        JsonIncludeProperties.Value partialOverride = new JsonIncludeProperties.Value(createSet("foo"));
        JsonIncludeProperties.Value merged = original.withOverrides(partialOverride);

        Set<String> included = merged.getIncluded();
        assertEquals(1, included.size(),
            "Merged with partial override should retain only matching property");
        assertEquals(createSet("foo"), included,
            "Merged with partial override should retain only 'foo'");
    }

    // Helper to get Value instance from Bogus annotation
    private JsonIncludeProperties.Value getBogusValue() {
        return JsonIncludeProperties.Value.from(
            Bogus.class.getAnnotation(JsonIncludeProperties.class)
        );
    }
}