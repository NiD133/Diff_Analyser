package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This test suite focuses on the behavior of the {@link JsonIgnoreProperties.Value} class.
 * The original test class name was preserved for context.
 */
public class JsonIgnoreProperties_ESTestTest45 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that calling {@link JsonIgnoreProperties.Value#withMerge()} on an instance
     * that already has merging enabled returns the same instance, as an optimization.
     */
    @Test
    public void withMerge_whenMergeAlreadyEnabled_shouldReturnSameInstance() {
        // Arrange: Create a Value instance. By default, instances created via
        // forIgnoredProperties have merging enabled.
        JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.forIgnoredProperties();

        // Act: Call withMerge() on the instance.
        JsonIgnoreProperties.Value resultValue = initialValue.withMerge();

        // Assert: The method should return the original instance, not a new one.
        // This confirms the optimization that avoids creating a new object unnecessarily.
        assertSame("Calling withMerge() on an instance with merging already enabled should return the same instance.",
                initialValue, resultValue);
    }
}