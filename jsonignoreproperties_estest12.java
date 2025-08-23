package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JsonIgnoreProperties.Value} class, focusing on its
 * immutability and the behavior of its 'with' methods.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Verifies that the `with...()` methods of `JsonIgnoreProperties.Value`
     * create new, immutable instances with the correct properties updated.
     * This test specifically checks the chaining of `withAllowGetters()` and `withMerge()`.
     */
    @Test
    public void withMethodsShouldCreateNewImmutableInstancesWithUpdatedFlags() {
        // Arrange: Create a base Value instance with all boolean flags set to false.
        JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value.construct(
                Collections.emptySet(),
                /* ignoreUnknown */ false,
                /* allowGetters */ false,
                /* allowSetters */ false,
                /* merge */ false
        );

        // Act: Chain 'with' methods to create new instances.
        // Since JsonIgnoreProperties.Value is immutable, each call returns a new object.
        JsonIgnoreProperties.Value valueWithGettersEnabled = baseValue.withAllowGetters();
        JsonIgnoreProperties.Value finalValue = valueWithGettersEnabled.withMerge();

        // Assert: Check the properties of the intermediate and final instances
        // to confirm the correct flags were set and that the original objects remain unchanged.

        // 1. Verify the intermediate instance state (after withAllowGetters).
        assertTrue("withAllowGetters() should enable the 'allowGetters' flag.",
                valueWithGettersEnabled.getAllowGetters());
        assertFalse("withAllowGetters() should not affect the 'merge' flag.",
                valueWithGettersEnabled.getMerge());

        // 2. Verify the final instance state (after withMerge).
        assertTrue("The 'allowGetters' flag should be preserved in the final instance.",
                finalValue.getAllowGetters());
        assertTrue("withMerge() should enable the 'merge' flag.",
                finalValue.getMerge());

        // 3. Verify that other flags in the final instance remain unchanged.
        assertFalse("The 'ignoreUnknown' flag should remain unchanged.",
                finalValue.getIgnoreUnknown());
        assertFalse("The 'allowSetters' flag should remain unchanged.",
                finalValue.getAllowSetters());
    }
}