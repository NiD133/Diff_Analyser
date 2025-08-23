package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;

public class JsonIgnoreProperties_ESTestTest17 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that the {@link JsonIgnoreProperties.Value#withAllowSetters()} method
     * creates a new immutable instance with the 'allowSetters' property enabled,
     * while preserving all other properties from the original instance.
     */
    @Test
    public void withAllowSetters_shouldCreateNewInstanceWithSettersAllowed() {
        // Arrange: Create a base Value instance with allowSetters set to false.
        // Properties: ignored=empty, ignoreUnknown=true, allowGetters=true, allowSetters=false, merge=true
        Set<String> ignoredProperties = Collections.emptySet();
        JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value.construct(
                ignoredProperties,
                true,  // ignoreUnknown
                true,  // allowGetters
                false, // allowSetters
                true   // merge
        );

        // Act: Call the method under test to create a new instance.
        JsonIgnoreProperties.Value updatedValue = baseValue.withAllowSetters();

        // Assert: Verify the new instance and the immutability of the original.

        // 1. A new, distinct instance should be created.
        assertNotSame("A new instance should be returned", baseValue, updatedValue);

        // 2. The new instance should have allowSetters enabled.
        assertTrue("Updated value should have allowSetters enabled", updatedValue.getAllowSetters());

        // 3. The original instance should remain unchanged (confirming immutability).
        assertFalse("Original value's allowSetters should not change", baseValue.getAllowSetters());

        // 4. All other properties should be copied from the original instance.
        assertEquals("Ignored properties should be preserved", baseValue.getIgnored(), updatedValue.getIgnored());
        assertEquals("ignoreUnknown property should be preserved", baseValue.getIgnoreUnknown(), updatedValue.getIgnoreUnknown());
        assertEquals("allowGetters property should be preserved", baseValue.getAllowGetters(), updatedValue.getAllowGetters());
        assertEquals("merge property should be preserved", baseValue.getMerge(), updatedValue.getMerge());
    }
}