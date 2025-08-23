package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test suite focuses on the {@link JsonIgnoreProperties.Value} class,
 * specifically its static merging capabilities.
 */
// Note: The original test class name and inheritance are kept to match the context.
public class JsonIgnoreProperties_ESTestTest59 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that merging an array of {@link JsonIgnoreProperties.Value} instances,
     * which includes the default EMPTY instance and nulls, results in a new
     * Value instance where 'ignoreUnknown' remains false (its default state).
     */
    @Test
    public void mergeAllWithEmptyAndNullValuesShouldRetainDefaultIgnoreUnknown() {
        // Arrange: Create an array containing the default EMPTY value and nulls.
        // The EMPTY value has 'ignoreUnknown' set to false by default.
        JsonIgnoreProperties.Value[] valuesToMerge = {
            JsonIgnoreProperties.Value.EMPTY,
            null,
            JsonIgnoreProperties.Value.EMPTY
        };

        // Act: Merge all the values in the array. The mergeAll method should skip nulls
        // and combine the non-null values.
        JsonIgnoreProperties.Value mergedValue = JsonIgnoreProperties.Value.mergeAll(valuesToMerge);

        // Assert: The resulting merged value should retain the default 'ignoreUnknown'
        // setting from the EMPTY instances, which is false.
        assertFalse("Merging EMPTY and null values should result in ignoreUnknown=false",
                mergedValue.getIgnoreUnknown());
    }
}