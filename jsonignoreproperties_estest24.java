package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the immutable nature and fluent builder methods of {@link JsonIgnoreProperties.Value}.
 */
public class JsonIgnoreProperties_ESTestTest24 {

    /**
     * Verifies that the `with...()` methods on `JsonIgnoreProperties.Value`
     * create new, immutable instances with the correctly updated properties,
     * while leaving other properties unchanged.
     */
    @Test
    public void withMethodsShouldReturnNewImmutableInstanceWithUpdatedFlags() {
        // Arrange: Start with the default empty value configuration.
        JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value.EMPTY;

        // Act: Create a new value by enabling `allowGetters`.
        JsonIgnoreProperties.Value valueWithGetters = baseValue.withAllowGetters();

        // Assert: Verify the new instance is distinct and has the correct properties.
        assertNotSame("A new instance should be created", baseValue, valueWithGetters);
        assertNotEquals("Values with different properties should not be equal", baseValue, valueWithGetters);
        
        assertTrue("allowGetters should be enabled", valueWithGetters.getAllowGetters());
        // Verify other properties remain unchanged from the base state.
        assertFalse("allowSetters should be unchanged", valueWithGetters.getAllowSetters());
        assertFalse("ignoreUnknown should be unchanged", valueWithGetters.getIgnoreUnknown());
        assertTrue("merge should be unchanged", valueWithGetters.getMerge());

        // Act: Chain another call to enable `ignoreUnknown` on the intermediate value.
        JsonIgnoreProperties.Value finalValue = valueWithGetters.withIgnoreUnknown();

        // Assert: Verify the final instance is also distinct and has all expected properties.
        assertNotSame("Chaining should also create a new instance", valueWithGetters, finalValue);
        assertNotEquals("Values with different properties should not be equal", valueWithGetters, finalValue);

        assertTrue("ignoreUnknown should now be enabled", finalValue.getIgnoreUnknown());
        // Verify properties from the previous state are carried over.
        assertTrue("allowGetters should be carried over", finalValue.getAllowGetters());
        assertFalse("allowSetters should remain unchanged", finalValue.getAllowSetters());
        assertTrue("merge should remain unchanged", finalValue.getMerge());
    }
}