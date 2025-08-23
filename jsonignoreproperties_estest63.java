package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Verifies that creating a {@link JsonIgnoreProperties.Value} instance using the
     * {@code forIgnoredProperties} factory method sets the boolean flags to their
     * expected default states.
     * <p>
     * By default, merging should be enabled, while all other flags
     * (ignoreUnknown, allowGetters, allowSetters) should be disabled.
     */
    @Test
    public void forIgnoredPropertiesShouldCreateValueWithDefaultFlags() {
        // Arrange
        // The specific properties to ignore are not relevant for this test.
        Set<String> ignoredProperties = Collections.emptySet();

        // Act
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoredProperties(ignoredProperties);

        // Assert
        // The 'merge' flag is expected to be true by default to allow combining configurations.
        assertTrue("The 'merge' property should be true by default", value.getMerge());

        // The other boolean properties should be false by default.
        assertFalse("The 'ignoreUnknown' property should be false by default", value.getIgnoreUnknown());
        assertFalse("The 'allowGetters' property should be false by default", value.getAllowGetters());
        assertFalse("The 'allowSetters' property should be false by default", value.getAllowSetters());
    }
}