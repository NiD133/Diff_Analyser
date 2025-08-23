package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link JsonIgnoreProperties.Value} class.
 * This class contains the refactored version of the original generated test.
 */
public class JsonIgnoreProperties_ESTestTest20 {

    /**
     * Tests the chaining of 'with' methods on JsonIgnoreProperties.Value.
     * <p>
     * This test verifies that each call to a {@code with...()} method, such as
     * {@code withAllowSetters()} and {@code withAllowGetters()}, creates a new
     * immutable instance with the correct properties updated, while leaving the
     * original instance unchanged.
     */
    @Test
    public void withAllowSettersAndGettersShouldCreateNewInstancesWithCorrectFlags() {
        // Arrange: Start with a default Value instance.
        // By default, flags like 'allowGetters' and 'allowSetters' are false.
        JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.forIgnoredProperties();

        // Act: Create new instances by progressively enabling 'allowSetters' and then 'allowGetters'.
        JsonIgnoreProperties.Value valueWithSetters = initialValue.withAllowSetters();
        JsonIgnoreProperties.Value valueWithSettersAndGetters = valueWithSetters.withAllowGetters();

        // Assert: Verify the properties of the intermediate instance ('valueWithSetters').
        // It should have 'allowSetters' as true, but other flags should remain at their default values.
        assertTrue("allowSetters should be true after withAllowSetters()", valueWithSetters.getAllowSetters());
        assertFalse("allowGetters should remain false", valueWithSetters.getAllowGetters());
        assertFalse("ignoreUnknown should remain false", valueWithSetters.getIgnoreUnknown());
        assertTrue("merge should remain true by default", valueWithSetters.getMerge());

        // Assert: Verify the properties of the final instance ('valueWithSettersAndGetters').
        // It should have both 'allowSetters' and 'allowGetters' as true.
        assertTrue("allowSetters should be carried over and remain true", valueWithSettersAndGetters.getAllowSetters());
        assertTrue("allowGetters should be true after withAllowGetters()", valueWithSettersAndGetters.getAllowGetters());
        assertFalse("ignoreUnknown should remain false", valueWithSettersAndGetters.getIgnoreUnknown());
        assertTrue("merge should remain true by default", valueWithSettersAndGetters.getMerge());

        // Assert: Crucially, verify that the original instance remains unchanged, proving immutability.
        assertFalse("Original instance's allowSetters should be unchanged", initialValue.getAllowSetters());
        assertFalse("Original instance's allowGetters should be unchanged", initialValue.getAllowGetters());
    }
}