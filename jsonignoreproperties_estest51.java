package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the {@link JsonIgnoreProperties.Value} class,
 * specifically its factory methods.
 */
// The original test class name and inheritance are preserved to maintain context
// within its existing test suite.
public class JsonIgnoreProperties_ESTestTest51 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Verifies that the `withoutIgnoreUnknown()` method correctly creates a new
     * `JsonIgnoreProperties.Value` instance with the `ignoreUnknown` property set to false,
     * while preserving other properties and ensuring the original instance remains unchanged.
     */
    @Test
    public void withoutIgnoreUnknownShouldCreateNewInstanceWithFlagDisabled() {
        // Arrange: Create a Value instance with ignoreUnknown set to true.
        JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Act: Call the method under test to create a new instance.
        JsonIgnoreProperties.Value updatedValue = initialValue.withoutIgnoreUnknown();

        // Assert: Verify the state of both the original and the new instance.

        // 1. A new, distinct instance should be returned.
        assertNotSame("The method should return a new instance, not modify the original.", initialValue, updatedValue);

        // 2. The original instance should remain unchanged (verifying immutability).
        assertTrue("Original 'ignoreUnknown' flag should remain true.", initialValue.getIgnoreUnknown());
        assertTrue("Original 'merge' flag should be true (default).", initialValue.getMerge());
        assertFalse("Original 'allowGetters' flag should be false (default).", initialValue.getAllowGetters());
        assertFalse("Original 'allowSetters' flag should be false (default).", initialValue.getAllowSetters());

        // 3. The new instance should have the 'ignoreUnknown' flag set to false.
        assertFalse("Updated 'ignoreUnknown' flag should now be false.", updatedValue.getIgnoreUnknown());

        // 4. Other properties in the new instance should retain their original default values.
        assertTrue("Updated 'merge' flag should remain true.", updatedValue.getMerge());
        assertFalse("Updated 'allowGetters' flag should remain false.", updatedValue.getAllowGetters());
        assertFalse("Updated 'allowSetters' flag should remain false.", updatedValue.getAllowSetters());
    }
}