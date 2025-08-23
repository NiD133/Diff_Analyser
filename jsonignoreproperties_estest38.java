package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Verifies that the `construct` factory method correctly creates an instance
     * with the specified properties, particularly when all boolean flags are set to false.
     */
    @Test
    public void shouldConstructValueWithAllBooleanFlagsDisabled() {
        // Arrange
        Set<String> ignoredProperties = Collections.emptySet();
        boolean ignoreUnknown = false;
        boolean allowGetters = false;
        boolean allowSetters = false;
        boolean merge = false;

        // Act
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.construct(
                ignoredProperties,
                ignoreUnknown,
                allowGetters,
                allowSetters,
                merge
        );

        // Assert
        assertEquals("Ignored properties set should be empty", ignoredProperties, value.getIgnored());
        assertFalse("ignoreUnknown should be false", value.getIgnoreUnknown());
        assertFalse("allowGetters should be false", value.getAllowGetters());
        assertFalse("allowSetters should be false", value.getAllowSetters());
        assertFalse("merge should be false", value.getMerge());
    }
}