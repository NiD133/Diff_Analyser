package com.fasterxml.jackson.annotation;

import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to verify the functionality of merging {@link JsonIgnoreProperties.Value}
 * instances for overrides and other related behaviors.
 */
public class JsonIgnorePropertiesTest {

    // A test class with JsonIgnoreProperties annotation for testing purposes
    @JsonIgnoreProperties(value = { "foo", "bar" }, ignoreUnknown = true)
    private static final class AnnotatedClass {}

    // A constant representing an empty JsonIgnoreProperties.Value instance
    private static final JsonIgnoreProperties.Value EMPTY = JsonIgnoreProperties.Value.empty();

    @Test
    public void testEmptyValueCreation() {
        // Verify that creating a Value from null results in an empty Value
        assertSame(EMPTY, JsonIgnoreProperties.Value.from(null));

        // Verify properties of the empty Value
        assertEquals(0, EMPTY.getIgnored().size());
        assertFalse(EMPTY.getAllowGetters());
        assertFalse(EMPTY.getAllowSetters());
    }

    @Test
    public void testValueEquality() {
        // Verify that the same instance is equal to itself
        assertEquals(EMPTY, EMPTY);

        // Verify that the merge operation returns the same instance for empty Value
        assertSame(EMPTY, EMPTY.withMerge());

        // Create a non-merged Value and verify equality properties
        JsonIgnoreProperties.Value nonMergedValue = EMPTY.withoutMerge();
        assertEquals(nonMergedValue, nonMergedValue);
        assertNotEquals(EMPTY, nonMergedValue);
        assertNotEquals(nonMergedValue, EMPTY);
    }

    @Test
    public void testValueFromAnnotation() throws Exception {
        // Create a Value from an annotation and verify its properties
        JsonIgnoreProperties.Value valueFromAnnotation = JsonIgnoreProperties.Value.from(
                AnnotatedClass.class.getAnnotation(JsonIgnoreProperties.class));
        assertNotNull(valueFromAnnotation);
        assertFalse(valueFromAnnotation.getMerge());
        assertFalse(valueFromAnnotation.getAllowGetters());
        assertFalse(valueFromAnnotation.getAllowSetters());
        assertEquals(_set("foo", "bar"), valueFromAnnotation.getIgnored());
    }

    @Test
    public void testValueFactories() {
        // Verify factory methods for creating empty Values
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoreUnknown(false));
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties());
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties(Collections.emptySet()));

        // Verify creation of Value with specific ignored properties
        JsonIgnoreProperties.Value valueWithIgnored = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");
        assertEquals(_set("a", "b"), valueWithIgnored.getIgnored());

        // Verify properties of Value with allowed getters
        JsonIgnoreProperties.Value valueWithGetters = valueWithIgnored.withAllowGetters();
        assertTrue(valueWithGetters.getAllowGetters());
        assertFalse(valueWithGetters.getAllowSetters());
        assertEquals(_set("a", "b"), valueWithGetters.getIgnored());
        assertEquals(_set("a", "b"), valueWithGetters.findIgnoredForDeserialization());
        assertEquals(_set(), valueWithGetters.findIgnoredForSerialization());

        // Verify properties of Value with allowed setters
        JsonIgnoreProperties.Value valueWithSetters = valueWithIgnored.withAllowSetters();
        assertFalse(valueWithSetters.getAllowGetters());
        assertTrue(valueWithSetters.getAllowSetters());
        assertEquals(_set("a", "b"), valueWithSetters.getIgnored());
        assertEquals(_set(), valueWithSetters.findIgnoredForDeserialization());
        assertEquals(_set("a", "b"), valueWithSetters.findIgnoredForSerialization());
    }

    @Test
    public void testMutantFactories() {
        // Verify creation of Values with different ignored properties
        assertEquals(2, EMPTY.withIgnored("a", "b").getIgnored().size());
        assertEquals(1, EMPTY.withIgnored(Collections.singleton("x")).getIgnored().size());
        assertEquals(0, EMPTY.withIgnored((Set<String>) null).getIgnored().size());

        // Verify toggling of ignoreUnknown property
        assertTrue(EMPTY.withIgnoreUnknown().getIgnoreUnknown());
        assertFalse(EMPTY.withoutIgnoreUnknown().getIgnoreUnknown());

        // Verify toggling of allowGetters property
        assertTrue(EMPTY.withAllowGetters().getAllowGetters());
        assertFalse(EMPTY.withoutAllowGetters().getAllowGetters());

        // Verify toggling of allowSetters property
        assertTrue(EMPTY.withAllowSetters().getAllowSetters());
        assertFalse(EMPTY.withoutAllowSetters().getAllowSetters());

        // Verify toggling of merge property
        assertTrue(EMPTY.withMerge().getMerge());
        assertFalse(EMPTY.withoutMerge().getMerge());
    }

    @Test
    public void testSimpleMergeBehavior() {
        // Create Values with different properties
        JsonIgnoreProperties.Value baseValue = EMPTY.withIgnoreUnknown().withAllowGetters();
        JsonIgnoreProperties.Value overrideValueWithMerge = EMPTY.withMerge().withIgnored("a");
        JsonIgnoreProperties.Value overrideValueWithoutMerge = EMPTY.withoutMerge();

        // Verify merging behavior with merge enabled
        JsonIgnoreProperties.Value mergedValueWithMerge = baseValue.withOverrides(overrideValueWithMerge);
        assertEquals(Collections.singleton("a"), mergedValueWithMerge.getIgnored());
        assertTrue(mergedValueWithMerge.getIgnoreUnknown());
        assertTrue(mergedValueWithMerge.getAllowGetters());
        assertFalse(mergedValueWithMerge.getAllowSetters());

        // Verify merging behavior with merge disabled
        JsonIgnoreProperties.Value mergedValueWithoutMerge = JsonIgnoreProperties.Value.merge(baseValue, overrideValueWithoutMerge);
        assertEquals(Collections.emptySet(), mergedValueWithoutMerge.getIgnored());
        assertFalse(mergedValueWithoutMerge.getIgnoreUnknown());
        assertFalse(mergedValueWithoutMerge.getAllowGetters());
        assertFalse(mergedValueWithoutMerge.getAllowSetters());

        // Verify that non-merged value is effectively the override
        assertEquals(overrideValueWithoutMerge, mergedValueWithoutMerge);

        // Verify that merging with null or empty returns the original
        assertSame(overrideValueWithoutMerge, overrideValueWithoutMerge.withOverrides(null));
        assertSame(overrideValueWithoutMerge, overrideValueWithoutMerge.withOverrides(EMPTY));
    }

    @Test
    public void testMergeMultipleIgnoreProperties() {
        // Create multiple Values with different ignored properties
        JsonIgnoreProperties.Value value1 = EMPTY.withIgnored("a");
        JsonIgnoreProperties.Value value2 = EMPTY.withIgnored("b");
        JsonIgnoreProperties.Value value3 = EMPTY.withIgnored("c");

        // Merge all Values and verify the combined ignored properties
        JsonIgnoreProperties.Value mergedValue = JsonIgnoreProperties.Value.mergeAll(value1, value2, value3);
        Set<String> allIgnored = mergedValue.getIgnored();
        assertEquals(3, allIgnored.size());
        assertTrue(allIgnored.contains("a"));
        assertTrue(allIgnored.contains("b"));
        assertTrue(allIgnored.contains("c"));
    }

    @Test
    public void testValueToString() {
        // Verify the string representation of a Value with specific properties
        assertEquals(
                "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=false,allowGetters=false,allowSetters=true,merge=true)",
                EMPTY.withAllowSetters().withMerge().toString());

        // Verify that the hash code is non-zero
        int hash = EMPTY.hashCode();
        if (hash == 0) {
            fail("Hash code should not be zero");
        }
    }

    // Helper method to create a set from given elements
    private Set<String> _set(String... elements) {
        return new LinkedHashSet<>(Arrays.asList(elements));
    }
}