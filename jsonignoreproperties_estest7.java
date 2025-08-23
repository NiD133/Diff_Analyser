package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the JsonIgnoreProperties.Value class.
 */
// The original test class name is kept for context. In a real-world scenario,
// this test would be part of a larger, more descriptively named test suite
// like `JsonIgnorePropertiesValueTest`.
public class JsonIgnoreProperties_ESTestTest7 {

    /**
     * Tests that the getAllowGetters() method of JsonIgnoreProperties.Value
     * correctly returns the boolean value provided during its construction.
     */
    @Test
    public void getAllowGettersShouldReturnConstructorValue() {
        // Arrange
        // Define the properties for a new JsonIgnoreProperties.Value instance.
        // The key property for this test is 'allowGetters', which is explicitly set to false.
        Set<String> ignoredProperties = Collections.emptySet();
        boolean ignoreUnknown = true;
        boolean allowGetters = false;
        boolean allowSetters = true;
        boolean merge = true;

        // Act
        JsonIgnoreProperties.Value value = new JsonIgnoreProperties.Value(
                ignoredProperties,
                ignoreUnknown,
                allowGetters,
                allowSetters,
                merge
        );

        // Assert
        // The primary assertion confirms that getAllowGetters() returns the value set in the constructor.
        assertFalse("getAllowGetters() should return the configured value of false.", value.getAllowGetters());

        // Additional assertions to verify the overall state of the constructed object is correct.
        assertTrue("getIgnoreUnknown() should be true.", value.getIgnoreUnknown());
        assertTrue("getAllowSetters() should be true.", value.getAllowSetters());
    }
}