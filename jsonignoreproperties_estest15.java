package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on verifying the behavior of the JsonIgnoreProperties.Value class.
 */
public class JsonIgnoreProperties_ESTestTest15_Improved {

    /**
     * Tests that the withAllowSetters() and withoutAllowSetters() methods
     * correctly create new, immutable instances of JsonIgnoreProperties.Value
     * with the 'allowSetters' flag updated appropriately.
     */
    @Test
    public void withAndWithoutAllowSettersShouldCreateNewInstancesWithUpdatedFlag() {
        // Arrange: Create a base Value instance with allowSetters initially set to false.
        // We use the empty() factory method for a clean and readable starting point.
        JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.empty();
        assertFalse("Precondition: initialValue should have allowSetters as false.", initialValue.getAllowSetters());

        // Act
        // 1. Create a new instance with allowSetters enabled.
        JsonIgnoreProperties.Value valueWithSettersAllowed = initialValue.withAllowSetters();
        
        // 2. Create another instance from the previous one, but with allowSetters disabled again.
        JsonIgnoreProperties.Value finalValue = valueWithSettersAllowed.withoutAllowSetters();

        // Assert
        // Verify the state of the instance where setters are allowed.
        assertTrue("withAllowSetters() should enable the flag.", valueWithSettersAllowed.getAllowSetters());
        // Other properties should remain unchanged from the initial state.
        assertEquals(initialValue.getIgnored(), valueWithSettersAllowed.getIgnored());
        assertEquals(initialValue.getIgnoreUnknown(), valueWithSettersAllowed.getIgnoreUnknown());
        
        // Verify the state of the final instance where setters are disallowed again.
        assertFalse("withoutAllowSetters() should disable the flag.", finalValue.getAllowSetters());

        // Verify immutability and equality.
        // The 'with' and 'without' methods should return new instances.
        assertNotSame("withAllowSetters() should return a new instance.", initialValue, valueWithSettersAllowed);
        assertNotSame("withoutAllowSetters() should return a new instance.", valueWithSettersAllowed, finalValue);

        // The final value should be logically equal to the initial value, as we've reverted the change.
        assertEquals("Reverting the change should result in a value equal to the initial one.", initialValue, finalValue);
        assertNotEquals("The intermediate value should not be equal to the initial one.", initialValue, valueWithSettersAllowed);
    }
}