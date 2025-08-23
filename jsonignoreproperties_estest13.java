package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class contains tests for the {@link JsonIgnoreProperties.Value} class.
 *
 * Note: The original test class name "JsonIgnoreProperties_ESTestTest13" and its parent
 * "JsonIgnoreProperties_ESTest_scaffolding" appear to be tool-generated. For better
 * clarity, they would typically be renamed to something more descriptive, like
 * "JsonIgnorePropertiesValueTest".
 */
public class JsonIgnoreProperties_ESTestTest13 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Verifies that the `withMerge()` and `withoutMerge()` methods correctly toggle the
     * `merge` property while creating new, immutable instances of {@link JsonIgnoreProperties.Value}.
     * It also ensures that other properties are preserved during these operations.
     */
    @Test
    public void withAndWithoutMergeShouldToggleFlagAndCreateNewInstances() {
        // Arrange: Create a base Value instance.
        // The factory `forIgnoreUnknown(true)` creates an instance where `merge` is also true by default.
        JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Act: Create a new instance with the merge property disabled.
        JsonIgnoreProperties.Value valueWithoutMerge = baseValue.withoutMerge();

        // Assert: The new instance should have merge disabled and be a different object,
        // while other properties remain unchanged.
        assertNotSame("withoutMerge() must return a new instance.", baseValue, valueWithoutMerge);
        assertFalse("The merge property should now be disabled.", valueWithoutMerge.getMerge());
        assertTrue("The ignoreUnknown property should be preserved.", valueWithoutMerge.getIgnoreUnknown());

        // Act: Re-enable the merge property on another new instance.
        JsonIgnoreProperties.Value valueWithMergeReapplied = valueWithoutMerge.withMerge();

        // Assert: The final instance should have merge enabled and be a different object.
        // Its properties should match the base value, but it must be a new instance.
        assertNotSame("withMerge() must return a new instance.", valueWithoutMerge, valueWithMergeReapplied);
        assertTrue("The merge property should be re-enabled.", valueWithMergeReapplied.getMerge());
        
        // Verify other properties are still preserved and match their original default state.
        assertTrue("The ignoreUnknown property should still be preserved.", valueWithMergeReapplied.getIgnoreUnknown());
        assertFalse("The allowGetters property should retain its default value of false.", valueWithMergeReapplied.getAllowGetters());
        assertFalse("The allowSetters property should retain its default value of false.", valueWithMergeReapplied.getAllowSetters());
    }
}