package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Focused, readable tests for JsonIncludeProperties.Value behavior.
 * These tests use only the public API (all(), from(), withOverrides(), getIncluded(), equals(), hashCode(), toString(), valueFor()).
 */
public class JsonIncludePropertiesValueTest {

    private static JsonIncludeProperties mockAnnotation(String... included) {
        JsonIncludeProperties ann = mock(JsonIncludeProperties.class);
        when(ann.value()).thenReturn(included);
        return ann;
    }

    @Test
    public void allFactoryRepresentsUndefinedIncludedSet() {
        JsonIncludeProperties.Value v = JsonIncludeProperties.Value.all();
        assertNull("all() should represent an undefined included set", v.getIncluded());
    }

    @Test
    public void fromNullReturnsAllEquivalentValue() {
        JsonIncludeProperties.Value v = JsonIncludeProperties.Value.from(null);
        assertNull(v.getIncluded());
        assertEquals(JsonIncludeProperties.Value.all(), v);
    }

    @Test
    public void fromAnnotationBuildsSetWithProvidedNames() {
        JsonIncludeProperties ann = mockAnnotation("a", "b");
        JsonIncludeProperties.Value v = JsonIncludeProperties.Value.from(ann);

        Set<String> included = v.getIncluded();
        assertNotNull(included);
        assertEquals(2, included.size());
        assertTrue(included.contains("a"));
        assertTrue(included.contains("b"));
    }

    @Test
    public void emptyArrayMeansEmptySetNotNull() {
        JsonIncludeProperties ann = mockAnnotation(); // zero length array
        JsonIncludeProperties.Value v = JsonIncludeProperties.Value.from(ann);

        Set<String> included = v.getIncluded();
        assertNotNull(included);
        assertTrue(included.isEmpty());
    }

    @Test
    public void withOverridesIgnoresUndefinedOverrideAndReturnsOriginal() {
        JsonIncludeProperties.Value base = JsonIncludeProperties.Value.from(mockAnnotation("a", "b"));
        JsonIncludeProperties.Value override = JsonIncludeProperties.Value.all(); // undefined

        JsonIncludeProperties.Value merged = base.withOverrides(override);

        assertSame("Overriding with undefined should return the original value", base, merged);
    }

    @Test
    public void withOverridesWhenBaseIsUndefinedReturnsOverride() {
        JsonIncludeProperties.Value base = JsonIncludeProperties.Value.all(); // undefined
        JsonIncludeProperties.Value override = JsonIncludeProperties.Value.from(mockAnnotation("x"));

        JsonIncludeProperties.Value merged = base.withOverrides(override);

        assertSame("Undefined base overridden by a defined value should return the override", override, merged);
    }

    @Test
    public void withOverridesComputesIntersectionOfIncludedNames() {
        JsonIncludeProperties.Value base = JsonIncludeProperties.Value.from(mockAnnotation("a", "b"));
        JsonIncludeProperties.Value override = JsonIncludeProperties.Value.from(mockAnnotation("b", "c"));

        JsonIncludeProperties.Value merged = base.withOverrides(override);

        Set<String> included = merged.getIncluded();
        assertNotNull(included);
        assertEquals(1, included.size());
        assertTrue(included.contains("b"));
    }

    @Test
    public void equalsAndHashCodeDependOnlyOnIncludedSet() {
        JsonIncludeProperties.Value v1 = JsonIncludeProperties.Value.from(mockAnnotation("a"));
        JsonIncludeProperties.Value v2 = JsonIncludeProperties.Value.from(mockAnnotation("a"));
        JsonIncludeProperties.Value v3 = JsonIncludeProperties.Value.from(mockAnnotation("b"));

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());

        assertNotEquals(v1, v3);
        assertNotEquals(v1, "not-a-Value");
        assertNotEquals(v1, null);

        assertEquals(JsonIncludeProperties.Value.all(), JsonIncludeProperties.Value.from(null));
    }

    @Test
    public void toStringMentionsIncludedContent() {
        assertTrue(JsonIncludeProperties.Value.all().toString().contains("included=null"));

        JsonIncludeProperties.Value v = JsonIncludeProperties.Value.from(mockAnnotation("a"));
        String s = v.toString();
        assertTrue("toString should include the included value(s)", s.contains("a"));
    }

    @Test
    public void valueForReturnsAnnotationType() {
        assertEquals(JsonIncludeProperties.class, JsonIncludeProperties.Value.all().valueFor());
    }
}