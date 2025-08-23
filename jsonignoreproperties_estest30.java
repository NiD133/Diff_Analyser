package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link JsonIgnoreProperties.Value} class, focusing on the merge logic.
 */
public class JsonIgnoreProperties_ESTestTest30 {

    /**
     * This test verifies two key behaviors of the `JsonIgnoreProperties.Value.merge()` method:
     * 1. When merging two `Value` instances, boolean properties are combined such that `true` takes precedence.
     * 2. A redundant merge (i.e., merging a value with an identical override) returns the original instance
     *    due to an internal optimization, rather than creating a new, identical object.
     */
    @Test
    public void mergeShouldCombinePropertiesAndReturnSameInstanceOnRedundantMerge() {
        // Arrange
        // Create a base value with default settings (all flags false except 'merge').
        JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value.forIgnoredProperties();

        // Create an override by enabling 'allowSetters' on the base value.
        JsonIgnoreProperties.Value valueWithSettersAllowed = baseValue.withAllowSetters();

        // Act
        // 1. First merge: The 'allowSetters' flag from the override should be applied.
        JsonIgnoreProperties.Value firstMergeResult = JsonIgnoreProperties.Value.merge(baseValue, valueWithSettersAllowed);

        // 2. Second merge: Merging the result with the same override again should be a no-op.
        JsonIgnoreProperties.Value secondMergeResult = JsonIgnoreProperties.Value.merge(firstMergeResult, valueWithSettersAllowed);

        // Assert
        // Verify that the first merge produced a value equal to the override.
        assertEquals("First merge should result in a value equal to the override",
                valueWithSettersAllowed, firstMergeResult);

        // Verify that the second, redundant merge returned the exact same object instance,
        // confirming the optimization.
        assertSame("A redundant merge should return the same instance without creating a new object",
                firstMergeResult, secondMergeResult);

        // Explicitly verify the properties of the final merged value.
        assertTrue("allowSetters should be true after merge", secondMergeResult.getAllowSetters());
        assertFalse("ignoreUnknown should remain false", secondMergeResult.getIgnoreUnknown());
        assertFalse("allowGetters should remain false", secondMergeResult.getAllowGetters());
        assertTrue("merge flag should be true by default", secondMergeResult.getMerge());
    }
}