package com.fasterxml.jackson.annotation;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to verify that {@link JsonIgnoreProperties.Value} instances can be created,
 * configured, and merged properly for handling property ignoring overrides.
 */
public class JsonIgnorePropertiesTest {

    // Test class with JsonIgnoreProperties annotation for testing annotation parsing
    @JsonIgnoreProperties(value = {"foo", "bar"}, ignoreUnknown = true)
    private static final class TestClassWithAnnotation {
    }

    private final JsonIgnoreProperties.Value EMPTY_VALUE = JsonIgnoreProperties.Value.empty();

    @Test
    public void testEmptyValueCreation() {
        // Creating from null should return empty instance
        assertSame(EMPTY_VALUE, JsonIgnoreProperties.Value.from(null));

        // Empty value should have default settings
        assertEquals(0, EMPTY_VALUE.getIgnored().size());
        assertFalse(EMPTY_VALUE.getAllowGetters());
        assertFalse(EMPTY_VALUE.getAllowSetters());
    }

    @Test
    public void testValueEquality() {
        // Empty value should equal itself
        assertEquals(EMPTY_VALUE, EMPTY_VALUE);

        // Empty value already has merge=true, so withMerge() should return same instance
        assertSame(EMPTY_VALUE, EMPTY_VALUE.withMerge());

        // Value with merge=false should be different from empty value
        JsonIgnoreProperties.Value valueWithoutMerge = EMPTY_VALUE.withoutMerge();
        assertEquals(valueWithoutMerge, valueWithoutMerge);
        assertNotEquals(EMPTY_VALUE, valueWithoutMerge);
        assertNotEquals(valueWithoutMerge, EMPTY_VALUE);
    }

    @Test
    public void testCreatingValueFromAnnotation() throws Exception {
        JsonIgnoreProperties annotation = TestClassWithAnnotation.class
                .getAnnotation(JsonIgnoreProperties.class);
        JsonIgnoreProperties.Value valueFromAnnotation = JsonIgnoreProperties.Value.from(annotation);

        assertNotNull(valueFromAnnotation);
        assertFalse(valueFromAnnotation.getMerge());
        assertFalse(valueFromAnnotation.getAllowGetters());
        assertFalse(valueFromAnnotation.getAllowSetters());
        
        Set<String> ignoredProperties = valueFromAnnotation.getIgnored();
        assertEquals(2, ignoredProperties.size());
        assertEquals(createSet("foo", "bar"), ignoredProperties);
    }

    @Test
    public void testFactoryMethods() {
        // Factory methods for default cases should return empty value
        assertSame(EMPTY_VALUE, JsonIgnoreProperties.Value.forIgnoreUnknown(false));
        assertSame(EMPTY_VALUE, JsonIgnoreProperties.Value.forIgnoredProperties());
        assertSame(EMPTY_VALUE, JsonIgnoreProperties.Value.forIgnoredProperties(Collections.emptySet()));

        // Factory method with specific properties
        JsonIgnoreProperties.Value valueWithIgnoredProps = JsonIgnoreProperties.Value
                .forIgnoredProperties("a", "b");
        assertEquals(createSet("a", "b"), valueWithIgnoredProps.getIgnored());

        // Test allowGetters behavior - properties ignored for deserialization only
        JsonIgnoreProperties.Value valueAllowingGetters = valueWithIgnoredProps.withAllowGetters();
        assertTrue(valueAllowingGetters.getAllowGetters());
        assertFalse(valueAllowingGetters.getAllowSetters());
        assertEquals(createSet("a", "b"), valueAllowingGetters.getIgnored());
        assertEquals(createSet("a", "b"), valueAllowingGetters.findIgnoredForDeserialization());
        assertEquals(createSet(), valueAllowingGetters.findIgnoredForSerialization());

        // Test allowSetters behavior - properties ignored for serialization only
        JsonIgnoreProperties.Value valueAllowingSetters = valueWithIgnoredProps.withAllowSetters();
        assertFalse(valueAllowingSetters.getAllowGetters());
        assertTrue(valueAllowingSetters.getAllowSetters());
        assertEquals(createSet("a", "b"), valueAllowingSetters.getIgnored());
        assertEquals(createSet(), valueAllowingSetters.findIgnoredForDeserialization());
        assertEquals(createSet("a", "b"), valueAllowingSetters.findIgnoredForSerialization());
    }

    @Test
    public void testMutantFactoryMethods() {
        // Test ignored properties modification
        assertEquals(2, EMPTY_VALUE.withIgnored("a", "b").getIgnored().size());
        assertEquals(1, EMPTY_VALUE.withIgnored(Collections.singleton("x")).getIgnored().size());
        assertEquals(0, EMPTY_VALUE.withIgnored((Set<String>) null).getIgnored().size());

        // Test ignoreUnknown flag modification
        assertTrue(EMPTY_VALUE.withIgnoreUnknown().getIgnoreUnknown());
        assertFalse(EMPTY_VALUE.withoutIgnoreUnknown().getIgnoreUnknown());

        // Test allowGetters flag modification
        assertTrue(EMPTY_VALUE.withAllowGetters().getAllowGetters());
        assertFalse(EMPTY_VALUE.withoutAllowGetters().getAllowGetters());
        
        // Test allowSetters flag modification
        assertTrue(EMPTY_VALUE.withAllowSetters().getAllowSetters());
        assertFalse(EMPTY_VALUE.withoutAllowSetters().getAllowSetters());

        // Test merge flag modification
        assertTrue(EMPTY_VALUE.withMerge().getMerge());
        assertFalse(EMPTY_VALUE.withoutMerge().getMerge());
    }

    @Test
    public void testMergingBehavior() {
        // Create base value with ignoreUnknown and allowGetters
        JsonIgnoreProperties.Value baseValue = EMPTY_VALUE
                .withIgnoreUnknown()
                .withAllowGetters();
        
        // Create override value with merge=true and ignored property
        JsonIgnoreProperties.Value overrideWithMerge = EMPTY_VALUE
                .withMerge()
                .withIgnored("a");
        
        // Create override value with merge=false
        JsonIgnoreProperties.Value overrideWithoutMerge = EMPTY_VALUE.withoutMerge();

        // When merging with merge=true, should combine settings
        JsonIgnoreProperties.Value mergedWithMerge = baseValue.withOverrides(overrideWithMerge);
        assertEquals(Collections.singleton("a"), mergedWithMerge.getIgnored());
        assertTrue(mergedWithMerge.getIgnoreUnknown());
        assertTrue(mergedWithMerge.getAllowGetters());
        assertFalse(mergedWithMerge.getAllowSetters());

        // When merging with merge=false, should replace all settings
        JsonIgnoreProperties.Value mergedWithoutMerge = JsonIgnoreProperties.Value
                .merge(baseValue, overrideWithoutMerge);
        assertEquals(Collections.emptySet(), mergedWithoutMerge.getIgnored());
        assertFalse(mergedWithoutMerge.getIgnoreUnknown());
        assertFalse(mergedWithoutMerge.getAllowGetters());
        assertFalse(mergedWithoutMerge.getAllowSetters());

        // Should effectively be the same as override value
        assertEquals(overrideWithoutMerge, mergedWithoutMerge);

        // Merging with null or empty should return original value
        assertSame(overrideWithoutMerge, overrideWithoutMerge.withOverrides(null));
        assertSame(overrideWithoutMerge, overrideWithoutMerge.withOverrides(EMPTY_VALUE));
    }

    @Test
    public void testMergingMultipleIgnoredProperties() {
        JsonIgnoreProperties.Value valueIgnoringA = EMPTY_VALUE.withIgnored("a");
        JsonIgnoreProperties.Value valueIgnoringB = EMPTY_VALUE.withIgnored("b");
        JsonIgnoreProperties.Value valueIgnoringC = EMPTY_VALUE.withIgnored("c");

        JsonIgnoreProperties.Value mergedValue = JsonIgnoreProperties.Value
                .mergeAll(valueIgnoringA, valueIgnoringB, valueIgnoringC);
        
        Set<String> allIgnoredProperties = mergedValue.getIgnored();
        assertEquals(3, allIgnoredProperties.size());
        assertTrue(allIgnoredProperties.contains("a"));
        assertTrue(allIgnoredProperties.contains("b"));
        assertTrue(allIgnoredProperties.contains("c"));
    }

    @Test
    public void testStringRepresentationAndHashCode() {
        String expectedToString = "JsonIgnoreProperties.Value(" +
                "ignored=[]," +
                "ignoreUnknown=false," +
                "allowGetters=false," +
                "allowSetters=true," +
                "merge=true)";
        
        assertEquals(expectedToString, EMPTY_VALUE
                .withAllowSetters()
                .withMerge()
                .toString());
        
        // Hash code should not be zero
        int hashCode = EMPTY_VALUE.hashCode();
        assertNotEquals(0, hashCode, "Hash code should not be zero");
    }

    /**
     * Helper method to create a LinkedHashSet from string arguments.
     * Using LinkedHashSet to maintain insertion order for predictable testing.
     */
    private Set<String> createSet(String... elements) {
        return new LinkedHashSet<>(Arrays.asList(elements));
    }
}