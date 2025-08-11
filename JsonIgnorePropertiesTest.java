package com.fasterxml.jackson.annotation;

import java.util.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonIgnorePropertiesTest {
    @JsonIgnoreProperties(value = {"foo", "bar"}, ignoreUnknown = true)
    private static class Bogus {}

    private static final JsonIgnoreProperties.Value EMPTY = JsonIgnoreProperties.Value.empty();

    private static Set<String> createSet(String... elements) {
        return new LinkedHashSet<>(Arrays.asList(elements));
    }

    @Test
    public void testFromNullYieldsEmpty() {
        assertSame(EMPTY, JsonIgnoreProperties.Value.from(null));
    }

    @Test
    public void testEmptyProperties() {
        assertEquals(0, EMPTY.getIgnored().size());
        assertFalse(EMPTY.getAllowGetters());
        assertFalse(EMPTY.getAllowSetters());
    }

    @Test
    public void testEqualityAndMerge() {
        // Empty instance should be equal to itself
        assertEquals(EMPTY, EMPTY);
        
        // withMerge() on empty should return same instance
        assertSame(EMPTY, EMPTY.withMerge());
    }

    @Test
    public void testEqualityWithNonEmptyValue() {
        JsonIgnoreProperties.Value withoutMerge = EMPTY.withoutMerge();
        assertNotEquals(EMPTY, withoutMerge);
        assertNotEquals(withoutMerge, EMPTY);
    }

    @Test
    public void testAnnotationValueConversion() throws Exception {
        JsonIgnoreProperties.Value v = JsonIgnoreProperties.Value.from(
            Bogus.class.getAnnotation(JsonIgnoreProperties.class));
        
        assertNotNull(v);
        assertFalse(v.getMerge());
        assertFalse(v.getAllowGetters());
        assertFalse(v.getAllowSetters());
        
        Set<String> ignored = v.getIgnored();
        assertEquals(2, ignored.size());
        assertEquals(createSet("foo", "bar"), ignored);
    }

    @Test
    public void testForIgnoreUnknownFactory() {
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoreUnknown(false));
        assertNotSame(EMPTY, JsonIgnoreProperties.Value.forIgnoreUnknown(true));
    }

    @Test
    public void testForIgnoredPropertiesFactories() {
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties());
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties(Collections.emptySet()));
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties(Collections.emptySet()));
        
        JsonIgnoreProperties.Value v = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");
        assertEquals(createSet("a", "b"), v.getIgnored());
    }

    @Test
    public void testWithAllowGettersMutation() {
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");
        JsonIgnoreProperties.Value mutated = base.withAllowGetters();
        
        assertTrue(mutated.getAllowGetters());
        assertFalse(mutated.getAllowSetters());
        assertEquals(createSet("a", "b"), mutated.getIgnored());
        assertEquals(createSet("a", "b"), mutated.findIgnoredForDeserialization());
        assertEquals(Collections.emptySet(), mutated.findIgnoredForSerialization());
    }

    @Test
    public void testWithAllowSettersMutation() {
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");
        JsonIgnoreProperties.Value mutated = base.withAllowSetters();
        
        assertFalse(mutated.getAllowGetters());
        assertTrue(mutated.getAllowSetters());
        assertEquals(createSet("a", "b"), mutated.getIgnored());
        assertEquals(Collections.emptySet(), mutated.findIgnoredForDeserialization());
        assertEquals(createSet("a", "b"), mutated.findIgnoredForSerialization());
    }

    @Test
    public void testIgnoredPropertiesMutation() {
        // Single property
        assertEquals(1, EMPTY.withIgnored(Collections.singleton("x")).getIgnored().size());
        
        // Multiple properties
        assertEquals(2, EMPTY.withIgnored("a", "b").getIgnored().size());
        
        // Null input
        assertEquals(0, EMPTY.withIgnored((Set<String>) null).getIgnored().size());
    }

    @Test
    public void testIgnoreUnknownMutation() {
        assertTrue(EMPTY.withIgnoreUnknown().getIgnoreUnknown());
        assertFalse(EMPTY.withoutIgnoreUnknown().getIgnoreUnknown());
    }

    @Test
    public void testAllowGettersMutation() {
        assertTrue(EMPTY.withAllowGetters().getAllowGetters());
        assertFalse(EMPTY.withoutAllowGetters().getAllowGetters());
    }

    @Test
    public void testAllowSettersMutation() {
        assertTrue(EMPTY.withAllowSetters().getAllowSetters());
        assertFalse(EMPTY.withoutAllowSetters().getAllowSetters());
    }

    @Test
    public void testMergeMutation() {
        assertTrue(EMPTY.withMerge().getMerge());
        assertFalse(EMPTY.withoutMerge().getMerge());
    }

    @Test
    public void testMergeWithOverride() {
        JsonIgnoreProperties.Value base = EMPTY.withIgnoreUnknown().withAllowGetters();
        JsonIgnoreProperties.Value overrides = EMPTY.withMerge().withIgnored("a");
        
        JsonIgnoreProperties.Value merged = base.withOverrides(overrides);
        
        assertEquals(Collections.singleton("a"), merged.getIgnored());
        assertTrue(merged.getIgnoreUnknown(), "IgnoreUnknown should be true");
        assertTrue(merged.getAllowGetters(), "AllowGetters should be true");
        assertFalse(merged.getAllowSetters(), "AllowSetters should be false");
    }

    @Test
    public void testMergeWithoutOverride() {
        JsonIgnoreProperties.Value base = EMPTY.withIgnoreUnknown().withAllowGetters();
        JsonIgnoreProperties.Value overrides = EMPTY.withoutMerge();
        
        JsonIgnoreProperties.Value merged = base.withOverrides(overrides);
        
        assertEquals(Collections.emptySet(), merged.getIgnored());
        assertFalse(merged.getIgnoreUnknown());
        assertFalse(merged.getAllowGetters());
        assertFalse(merged.getAllowSetters());
    }

    @Test
    public void testMergeWithNullOverrides() {
        JsonIgnoreProperties.Value value = EMPTY.withoutMerge();
        assertSame(value, value.withOverrides(null));
        assertSame(value, value.withOverrides(EMPTY));
    }

    @Test
    public void testMultiValueMerge() {
        JsonIgnoreProperties.Value v1 = EMPTY.withIgnored("a");
        JsonIgnoreProperties.Value v2 = EMPTY.withIgnored("b");
        JsonIgnoreProperties.Value v3 = EMPTY.withIgnored("c");

        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.mergeAll(v1, v2, v3);
        Set<String> all = merged.getIgnored();
        assertEquals(3, all.size());
        assertTrue(all.contains("a"));
        assertTrue(all.contains("b"));
        assertTrue(all.contains("c"));
    }

    @Test
    public void testToStringRepresentation() {
        String result = EMPTY.withAllowSetters().withMerge().toString();
        assertEquals(
            "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=false,allowGetters=false,allowSetters=true,merge=true)",
            result
        );
    }

    @Test
    public void testHashCodeNonZero() {
        assertNotEquals(0, EMPTY.hashCode(), "Hash code should not be zero");
    }
}