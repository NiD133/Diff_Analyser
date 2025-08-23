package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is a test suite for the {@link JsonIgnoreProperties.Value} class.
 * The original test class name 'JsonIgnoreProperties_ESTestTest39' is kept for consistency.
 * In a real-world scenario, it would be renamed to something like 'JsonIgnorePropertiesValueTest'.
 */
public class JsonIgnoreProperties_ESTestTest39 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that the `JsonIgnoreProperties.Value.construct()` factory method
     * correctly initializes an instance with all boolean properties set to true.
     */
    @Test
    public void constructShouldCorrectlySetAllProperties() {
        // Arrange
        Set<String> ignoredProperties = Collections.emptySet();
        boolean ignoreUnknown = true;
        boolean allowGetters = true;
        boolean allowSetters = true;
        boolean merge = true;

        // Act
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.construct(
                ignoredProperties,
                ignoreUnknown,
                allowGetters,
                allowSetters,
                merge
        );

        // Assert
        // Verify that all boolean flags were set correctly.
        assertTrue("ignoreUnknown should be true", value.getIgnoreUnknown());
        assertTrue("allowGetters should be true", value.getAllowGetters());
        assertTrue("allowSetters should be true", value.getAllowSetters());
        assertTrue("merge should be true", value.getMerge());
        
        // Verify that the set of ignored properties was also initialized correctly.
        assertEquals("The set of ignored properties should be empty", ignoredProperties, value.getIgnored());
    }
}