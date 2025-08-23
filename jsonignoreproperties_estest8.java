package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonIgnoreProperties_ESTestTest8 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Verifies that the {@link JsonIgnoreProperties.Value#withAllowSetters()}
     * method correctly creates a new, immutable instance with the 'allowSetters'
     * property enabled, while leaving other properties unchanged.
     */
    @Test
    public void withAllowSettersShouldCreateNewInstanceWithSettersAllowed() {
        // Arrange: Create a default Value instance where 'allowSetters' is disabled.
        JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.empty();
        assertFalse("Precondition: initialValue should have allowSetters disabled",
                initialValue.getAllowSetters());

        // Act: Call the method under test to create a new instance.
        JsonIgnoreProperties.Value updatedValue = initialValue.withAllowSetters();

        // Assert: Verify the properties of the new instance.
        // 1. The primary property 'allowSetters' should now be true.
        assertTrue("'allowSetters' should be enabled in the new instance",
                updatedValue.getAllowSetters());

        // 2. Other properties should retain their original default values.
        assertFalse("'allowGetters' should remain disabled", updatedValue.getAllowGetters());
        assertFalse("'ignoreUnknown' should remain disabled", updatedValue.getIgnoreUnknown());
        assertTrue("'merge' should remain enabled by default", updatedValue.getMerge());

        // 3. The original instance should remain unchanged (verifying immutability).
        assertFalse("The original instance should not be mutated",
                initialValue.getAllowSetters());
    }
}