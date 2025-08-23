package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite focusing on the merging logic of {@link JsonIgnoreProperties.Value},
 * specifically the {@code mergeAll} method.
 */
public class JsonIgnorePropertiesValueMergeTest {

    /**
     * This test verifies the behavior of the {@code mergeAll} method when combining
     * two {@code JsonIgnoreProperties.Value} instances that are identical except
     * for their 'merge' flag.
     *
     * The expected outcome is that if any of the values being merged has its 'merge'
     * flag enabled, the resulting merged value will also have its 'merge' flag enabled.
     */
    @Test
    public void mergeAll_whenCombiningValuesWithAndWithoutMerge_shouldProduceMergedValueWithMergeEnabled() {
        // Arrange
        // 1. Create a base Value object. By default, its 'merge' flag is enabled.
        String[] ignoredProperties = { "internalId" };
        JsonIgnoreProperties.Value valueWithMerge = JsonIgnoreProperties.Value.forIgnoredProperties(ignoredProperties);

        // 2. Create a second Value object based on the first, but with the 'merge' flag disabled.
        JsonIgnoreProperties.Value valueWithoutMerge = valueWithMerge.withoutMerge();

        // Sanity-check our initial setup
        assertTrue("Precondition: The first value should have merge enabled.", valueWithMerge.getMerge());
        assertFalse("Precondition: The second value should have merge disabled.", valueWithoutMerge.getMerge());

        // Act
        // 3. Merge the two values. The 'merge' property is expected to be true in the result
        //    because at least one of the source values had it enabled.
        JsonIgnoreProperties.Value mergedValue = JsonIgnoreProperties.Value.mergeAll(valueWithMerge, valueWithoutMerge);

        // Assert
        // 4. The result should be a new instance, not a reference to either of the inputs.
        assertNotSame("Merged value should be a new instance.", valueWithMerge, mergedValue);
        assertNotSame("Merged value should be a new instance.", valueWithoutMerge, mergedValue);

        // 5. The merged value should be logically equal to the input that had merge enabled,
        //    as all other properties were identical. This is a concise way to check all fields.
        assertEquals("Merged value should be equal to the value with merge enabled.", valueWithMerge, mergedValue);

        // 6. For maximum clarity, we can also explicitly verify the key properties of the merged value.
        assertTrue("The 'merge' flag should be true in the merged result.", mergedValue.getMerge());
        assertEquals(valueWithMerge.getIgnored(), mergedValue.getIgnored());
        assertFalse("The 'ignoreUnknown' flag should be false.", mergedValue.getIgnoreUnknown());
        assertFalse("The 'allowGetters' flag should be false.", mergedValue.getAllowGetters());
        assertFalse("The 'allowSetters' flag should be false.", mergedValue.getAllowSetters());
    }
}