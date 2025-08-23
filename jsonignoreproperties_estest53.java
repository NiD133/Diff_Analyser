package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains tests for the {@link JsonIgnoreProperties.Value} class,
 * focusing on its merging capabilities.
 */
public class JsonIgnoreProperties_ESTestTest53 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that merging a {@link JsonIgnoreProperties.Value} instance with an empty
     * Value instance results in a new Value that is logically equivalent to the
     * original non-empty one.
     */
    @Test(timeout = 4000)
    public void mergeAllWithEmptyValueShouldProduceEquivalentOfNonEmptyValue() {
        // Arrange
        // Create a Value instance with one ignored property (a single null, as in the original test).
        String[] propertiesToIgnore = { null };
        JsonIgnoreProperties.Value valueWithIgnoredProperty = JsonIgnoreProperties.Value.forIgnoredProperties(propertiesToIgnore);

        // Create an empty Value instance, which should act as a neutral element in the merge.
        JsonIgnoreProperties.Value emptyValue = JsonIgnoreProperties.Value.empty();

        JsonIgnoreProperties.Value[] valuesToMerge = { emptyValue, valueWithIgnoredProperty };

        // Act
        // Merge the empty value with the value containing the ignored property.
        JsonIgnoreProperties.Value mergedValue = JsonIgnoreProperties.Value.mergeAll(valuesToMerge);

        // Assert
        // 1. The merged result should be logically equal to the original non-empty value.
        assertEquals(valueWithIgnoredProperty, mergedValue);

        // 2. The merge operation should create a new instance, not return one of the inputs.
        assertNotSame("Merged value should be a new instance, not the non-empty input",
                valueWithIgnoredProperty, mergedValue);
        assertNotSame("Merged value should be a new instance, not the empty input",
                emptyValue, mergedValue);

        // 3. Verify that the properties of the merged value match the expected defaults
        //    from the non-empty input.
        assertFalse("ignoreUnknown should be false by default", mergedValue.getIgnoreUnknown());
        assertFalse("allowGetters should be false by default", mergedValue.getAllowGetters());
        assertFalse("allowSetters should be false by default", mergedValue.getAllowSetters());
    }
}