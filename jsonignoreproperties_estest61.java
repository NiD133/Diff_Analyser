package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;

public class JsonIgnoreProperties_ESTestTest61 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that calling {@link JsonIgnoreProperties.Value#withoutMerge()} on an instance
     * that already has merging disabled returns the same instance, as no modification is needed.
     * This is an optimization to avoid creating unnecessary new objects.
     */
    @Test
    public void withoutMerge_whenMergeIsAlreadyDisabled_shouldReturnSameInstance() {
        // Arrange: Create a Value instance directly with merging disabled.
        // Using the static `construct` method is clearer than setting up a mock.
        Set<String> ignoredProperties = Collections.emptySet();
        JsonIgnoreProperties.Value valueWithMergeDisabled = JsonIgnoreProperties.Value.construct(
                ignoredProperties,
                /* ignoreUnknown= */ false,
                /* allowGetters= */ false,
                /* allowSetters= */ false,
                /* merge= */ false // This is the key condition for this test
        );

        // Act: Call the method under test.
        JsonIgnoreProperties.Value result = valueWithMergeDisabled.withoutMerge();

        // Assert: The method should return the original instance because no change was needed.
        assertSame("Expected the same instance to be returned when merge is already false",
                valueWithMergeDisabled, result);

        // For completeness, verify that the properties of the returned instance are unchanged.
        assertFalse("Merge flag should remain false", result.getMerge());
        assertFalse(result.getIgnoreUnknown());
        assertFalse(result.getAllowGetters());
        assertFalse(result.getAllowSetters());
        assertEquals(ignoredProperties, result.getIgnored());
    }
}