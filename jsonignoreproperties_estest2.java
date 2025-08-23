package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the {@link JsonIgnoreProperties.Value} class,
 * specifically its factory methods and default property values.
 *
 * Note: The original test class name and inheritance from scaffolding are preserved.
 * In a real-world scenario, renaming "JsonIgnoreProperties_ESTestTest2" to
 * "JsonIgnorePropertiesValueTest" and removing the scaffolding if not needed
 * would be recommended for clarity.
 */
public class JsonIgnoreProperties_ESTestTest2 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Verifies that the `Value.forIgnoreUnknown(true)` factory method correctly creates
     * an instance with `ignoreUnknown` set to true, while other properties retain their
     * expected default values.
     */
    @Test
    public void forIgnoreUnknown_shouldSetIgnoreUnknownToTrue_andRetainDefaults() {
        // Arrange & Act: Create a configuration value to ignore unknown properties.
        JsonIgnoreProperties.Value configValue = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Assert: Verify the properties of the created configuration value.
        // The primary property should be set correctly.
        assertTrue("ignoreUnknown should be true as set by the factory method", configValue.getIgnoreUnknown());

        // Other properties should have their default values.
        assertFalse("allowGetters should default to false", configValue.getAllowGetters());
        assertFalse("allowSetters should default to false", configValue.getAllowSetters());
        assertTrue("merge should default to true", configValue.getMerge());
    }
}