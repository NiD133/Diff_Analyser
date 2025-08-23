package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Contains tests for the {@link JsonIgnoreProperties.Value} class, focusing on its merging logic.
 */
public class JsonIgnoreProperties_ESTestTest55 {

    /**
     * Tests that {@link JsonIgnoreProperties.Value#mergeAll(JsonIgnoreProperties.Value...)}
     * correctly combines multiple {@code Value} instances, including EMPTY, null, and a
     * custom-configured instance. The final merged result should adopt the settings from the
     * most specific, non-empty configuration provided.
     */
    @Test
    public void mergeAllShouldPrioritizeSpecificConfigurationOverEmptyAndNulls() {
        // Arrange
        // 1. An empty value, representing the default state.
        JsonIgnoreProperties.Value defaultValue = JsonIgnoreProperties.Value.EMPTY;

        // 2. A specific configuration that overrides the defaults.
        // Here, we allow getters and setters but do not ignore unknown properties.
        Set<String> noIgnoredProperties = Collections.emptySet();
        JsonIgnoreProperties.Value specificConfig = new JsonIgnoreProperties.Value(
                noIgnoredProperties,
                /* ignoreUnknown= */ false,
                /* allowGetters= */ true,
                /* allowSetters= */ true,
                /* merge= */ true
        );

        // 3. An array containing various values to be merged, including nulls.
        JsonIgnoreProperties.Value[] valuesToMerge = {
                defaultValue,
                null,
                specificConfig
        };

        // Act
        JsonIgnoreProperties.Value mergedValue = JsonIgnoreProperties.Value.mergeAll(valuesToMerge);

        // Assert
        assertNotNull("The merged result should not be null", mergedValue);

        // The merged value should have taken its properties from 'specificConfig'.
        assertFalse("ignoreUnknown should be false", mergedValue.getIgnoreUnknown());
        assertTrue("allowGetters should be true", mergedValue.getAllowGetters());
        assertTrue("allowSetters should be true", mergedValue.getAllowSetters());

        // The merged value should be logically equal to the specific configuration...
        assertEquals("Merged value should be equal to the specific configuration", specificConfig, mergedValue);
        // ...but it should be a new instance created by the merge process.
        assertNotSame("Merged value should be a new instance", specificConfig, mergedValue);
    }
}