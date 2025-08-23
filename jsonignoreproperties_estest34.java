package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the {@link JsonIgnoreProperties.Value} class,
 * specifically its equals() method.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Tests that two {@link JsonIgnoreProperties.Value} instances created with different
     * configurations are correctly identified as not being equal.
     */
    @Test
    public void equals_shouldReturnFalse_forDifferentConfigurations() {
        // Arrange
        // Create a value configured to ignore a specific (but empty) set of properties.
        JsonIgnoreProperties.Value valueWithNoIgnoredProperties = JsonIgnoreProperties.Value.forIgnoredProperties();

        // Create another value configured to ignore any unknown properties.
        JsonIgnoreProperties.Value valueWithIgnoreUnknown = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Sanity check: Verify the default properties of the 'ignoreUnknown' value.
        // The factory method should set these defaults consistently.
        assertFalse("allowSetters should be false by default", valueWithIgnoreUnknown.getAllowSetters());
        assertFalse("allowGetters should be false by default", valueWithIgnoreUnknown.getAllowGetters());
        assertTrue("merge should be true by default", valueWithIgnoreUnknown.getMerge());

        // Act & Assert
        // The two instances have different settings (the _ignoreUnknown flag differs),
        // so they should not be equal.
        assertNotEquals(valueWithNoIgnoredProperties, valueWithIgnoreUnknown);
    }
}