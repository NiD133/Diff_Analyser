package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the fluent "with" and "without"
 * methods of the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Tests that the fluent mutators {@code withAllowSetters()} and {@code withoutAllowSetters()}
     * create new, immutable instances of {@link JsonIgnoreProperties.Value}.
     * It also verifies that other properties (like ignoreUnknown) are correctly
     * preserved across these transformations.
     */
    @Test
    public void allowSettersConfigurationShouldBeImmutableAndPreserveOtherProperties() {
        // Arrange: Create an initial Value instance with a non-default setting
        // for 'ignoreUnknown' to verify that this property is preserved.
        JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Act 1: Create a new instance with allowSetters enabled.
        JsonIgnoreProperties.Value valueWithSettersAllowed = initialValue.withAllowSetters();

        // Assert 1: Verify the new instance is distinct and has the correct properties.
        assertNotSame("withAllowSetters() should create a new instance.", initialValue, valueWithSettersAllowed);
        assertTrue("allowSetters should now be true.", valueWithSettersAllowed.getAllowSetters());
        assertTrue("ignoreUnknown property should be preserved.", valueWithSettersAllowed.getIgnoreUnknown());
        assertFalse("allowGetters should remain at its default value of false.", valueWithSettersAllowed.getAllowGetters());

        // Act 2: Create another new instance by disabling allowSetters from the previous one.
        JsonIgnoreProperties.Value finalValue = valueWithSettersAllowed.withoutAllowSetters();

        // Assert 2: Verify this third instance is also distinct and has reverted the allowSetters change.
        assertNotSame("withoutAllowSetters() should create a new instance.", valueWithSettersAllowed, finalValue);
        assertFalse("allowSetters should be reverted to false.", finalValue.getAllowSetters());
        assertTrue("ignoreUnknown property should still be preserved.", finalValue.getIgnoreUnknown());

        // Assert 3: The final configuration should be logically equal to the initial one,
        // but not the same instance, confirming immutability.
        assertEquals("The final value should be logically equal to the initial value.", initialValue, finalValue);
        assertNotSame("The final value should not be the same instance as the initial one.", initialValue, finalValue);
    }
}