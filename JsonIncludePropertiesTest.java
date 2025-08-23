package com.fasterxml.jackson.annotation;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for JsonIncludeProperties.Value to ensure:
 * - "all"/undefined instance is a stable singleton
 * - Values can be created from annotations
 * - Overrides merge according to documented semantics (intersection)
 */
public class JsonIncludePropertiesTest {

    // Use an easy-to-read annotated type for building a Value from an annotation
    @JsonIncludeProperties({"foo", "bar"})
    private static final class SampleAnnotatedType { }

    // In the implementation, "ALL" represents an undefined value (null included set)
    private static final JsonIncludeProperties.Value UNDEFINED = JsonIncludeProperties.Value.all();

    @Test
    public void allValue_isSingletonUndefined() {
        // from(null) should return the singleton undefined instance
        assertSame(UNDEFINED, JsonIncludeProperties.Value.from(null));

        // Undefined means "not defined": included set is null
        assertNull(UNDEFINED.getIncluded());

        // Basic equals/hashCode/toString contracts for readability and stability
        assertEquals(UNDEFINED, UNDEFINED);
        assertEquals("JsonIncludeProperties.Value(included=null)", UNDEFINED.toString());
        assertEquals(0, UNDEFINED.hashCode());
    }

    @Test
    public void fromAnnotation_buildsExpectedIncludedSet() {
        JsonIncludeProperties.Value value = fromSampleAnnotation();

        assertNotNull(value);
        Set<String> included = value.getIncluded();
        assertNotNull(included);
        assertEquals(2, included.size());
        assertEquals(set("foo", "bar"), included);

        // toString order is not guaranteed; assert content without relying on element order
        String s = value.toString();
        assertTrue(s.startsWith("JsonIncludeProperties.Value(included=["));
        assertTrue(s.contains("foo"));
        assertTrue(s.contains("bar"));
        assertTrue(s.endsWith("])"));

        // Re-reading from the same annotation should give an equal Value
        assertEquals(value, fromSampleAnnotation());
    }

    @Test
    public void withOverrides_whenOverrideIsUndefined_keepsOriginalIncludedSet() {
        JsonIncludeProperties.Value original = fromSampleAnnotation();
        JsonIncludeProperties.Value merged = original.withOverrides(UNDEFINED);

        Set<String> included = merged.getIncluded();
        assertNotNull(included);
        assertEquals(2, included.size());
        assertEquals(set("foo", "bar"), included);
    }

    @Test
    public void withOverrides_whenOverrideIsEmpty_clearsAllProperties() {
        JsonIncludeProperties.Value original = fromSampleAnnotation();

        // Empty set means "include none"
        JsonIncludeProperties.Value override = new JsonIncludeProperties.Value(Collections.<String>emptySet());
        JsonIncludeProperties.Value merged = original.withOverrides(override);

        Set<String> included = merged.getIncluded();
        assertNotNull(included);
        assertEquals(0, included.size());
    }

    @Test
    public void withOverrides_whenOverrideRestricts_mergesByIntersection() {
        JsonIncludeProperties.Value original = fromSampleAnnotation();

        // Restrict to just "foo": merge should compute intersection => {"foo"}
        JsonIncludeProperties.Value override = new JsonIncludeProperties.Value(set("foo"));
        JsonIncludeProperties.Value merged = original.withOverrides(override);

        Set<String> included = merged.getIncluded();
        assertNotNull(included);
        assertEquals(1, included.size());
        assertEquals(set("foo"), included);
    }

    // Helper to build a Value from the SampleAnnotatedType's annotation
    private static JsonIncludeProperties.Value fromSampleAnnotation() {
        return JsonIncludeProperties.Value.from(
                SampleAnnotatedType.class.getAnnotation(JsonIncludeProperties.class)
        );
    }

    // Helper to create a deterministic Set preserving insertion order for stable comparisons
    private static Set<String> set(String... values) {
        return new LinkedHashSet<>(Arrays.asList(values));
    }
}