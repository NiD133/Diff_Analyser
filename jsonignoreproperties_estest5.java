package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test class focuses on verifying the behavior of the {@link JsonIgnoreProperties.Value} class.
 */
// The original test class name and inheritance are kept for context.
public class JsonIgnoreProperties_ESTestTest5 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Verifies that the getAllowSetters() method of JsonIgnoreProperties.Value
     * correctly returns 'true' when the object is constructed with this setting.
     */
    @Test(timeout = 4000)
    public void getAllowSetters_shouldReturnTrue_whenConstructedWithTrue() {
        // Arrange: Create a Value instance with allowSetters set to true.
        Set<String> ignoredProperties = Collections.emptySet();
        boolean ignoreUnknown = false;
        boolean allowGetters = false;
        boolean allowSetters = true;
        boolean merge = true;

        JsonIgnoreProperties.Value configValue = new JsonIgnoreProperties.Value(
                ignoredProperties,
                ignoreUnknown,
                allowGetters,
                allowSetters,
                merge
        );

        // Act: Call the getter for the allowSetters property.
        boolean result = configValue.getAllowSetters();

        // Assert: The returned value should match the constructor argument.
        assertTrue("The 'allowSetters' property should be true as set in the constructor.", result);

        // Also, verify other properties of the constructed object to ensure its state is correct.
        assertFalse("The 'ignoreUnknown' property should be false.", configValue.getIgnoreUnknown());
        assertFalse("The 'allowGetters' property should be false.", configValue.getAllowGetters());
        assertTrue("The 'merge' property should be true.", configValue.getMerge());
    }
}