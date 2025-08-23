package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class, focusing on its factory methods
 * and default property values.
 */
public class JsonIgnoreProperties_ValueTest {

    /**
     * Verifies that creating a {@link JsonIgnoreProperties.Value} instance
     * via {@code forIgnoreUnknown(true)} correctly sets the {@code ignoreUnknown}
     * property to true, while ensuring other properties retain their expected default values.
     */
    @Test
    public void forIgnoreUnknown_whenSetToTrue_shouldEnableFlagAndUseDefaultsForOthers() {
        // Arrange & Act: Create a Value instance configured to ignore unknown properties.
        JsonIgnoreProperties.Value configValue = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Assert: Verify the state of the created Value instance.

        // 1. The main property 'ignoreUnknown' should be set as requested.
        assertTrue("The 'ignoreUnknown' property should be true.",
                configValue.getIgnoreUnknown());

        // 2. Other properties should retain their standard default values.
        assertFalse("The 'allowGetters' property should be false by default.",
                configValue.getAllowGetters());
        assertFalse("The 'allowSetters' property should be false by default.",
                configValue.getAllowSetters());
        assertTrue("The 'merge' property should be true by default.",
                configValue.getMerge());
    }
}