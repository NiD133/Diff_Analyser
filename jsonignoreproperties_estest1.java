package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test focuses on the merging behavior of {@link JsonIgnoreProperties.Value},
 * specifically how properties are combined when using the {@code mergeAll} method.
 */
public class JsonIgnorePropertiesValueMergeTest {

    /**
     * Tests that when merging multiple {@link JsonIgnoreProperties.Value} instances,
     * the properties from later instances in the sequence override those from earlier ones.
     *
     * This test merges two configurations:
     * 1. The first has 'merge' explicitly disabled.
     * 2. The second has 'allowSetters' enabled.
     *
     * The expected result is a new configuration that reflects the properties of the second
     * instance, effectively having 'allowSetters' enabled and the default 'merge' behavior (enabled).
     */
    @Test
    public void mergeAllShouldPrioritizePropertiesFromLaterValues() {
        // Arrange: Create two conflicting Value instances.
        String[] ignored = {"testProp"};

        // First value: has merging disabled.
        JsonIgnoreProperties.Value valueWithMergeDisabled = JsonIgnoreProperties.Value
                .forIgnoredProperties(ignored)
                .withoutMerge();

        // Second value: has allowSetters enabled (and default merge=true).
        JsonIgnoreProperties.Value valueWithAllowSettersEnabled = JsonIgnoreProperties.Value
                .forIgnoredProperties(ignored)
                .withAllowSetters();

        JsonIgnoreProperties.Value[] valuesToMerge = {valueWithMergeDisabled, valueWithAllowSettersEnabled};

        // Act: Merge the two values.
        JsonIgnoreProperties.Value mergedValue = JsonIgnoreProperties.Value.mergeAll(valuesToMerge);

        // Assert: The merged result should be equivalent to the second value, as it came later.
        // It should be a new instance but logically equal.
        assertNotSame("A new Value instance should be created",
                valueWithAllowSettersEnabled, mergedValue);
        assertEquals("Merged value should be logically equal to the last value in the sequence",
                valueWithAllowSettersEnabled, mergedValue);

        // For clarity, explicitly verify the key properties of the merged result.
        assertTrue("allowSetters should be true, as taken from the second value",
                mergedValue.getAllowSetters());
        assertTrue("merge should be true, as the second value's default overrides the first's",
                mergedValue.getMerge());
        assertFalse("ignoreUnknown should retain its default value of false",
                mergedValue.getIgnoreUnknown());
        assertFalse("allowGetters should retain its default value of false",
                mergedValue.getAllowGetters());
    }
}