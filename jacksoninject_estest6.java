package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the {@link JacksonInject.Value} class.
 */
public class JacksonInjectValueTest {

    /**
     * Tests that the `withOptional()` method creates a new, distinct instance of
     * {@link JacksonInject.Value} when the 'optional' property is changed.
     * This verifies the immutability of the Value object.
     */
    @Test
    public void withOptional_whenValueIsChanged_shouldCreateNewUnequalInstance() {
        // Arrange: Create an initial JacksonInject.Value with a defined 'optional' state.
        final Boolean initialUseInput = false;
        final Boolean initialOptional = false;
        JacksonInject.Value initialValue = JacksonInject.Value.construct(null, initialUseInput, initialOptional);

        // Define the new 'optional' value, which is different from the original.
        final Boolean newOptional = null;

        // Act: Call the "wither" method to create a new instance with the updated 'optional' value.
        JacksonInject.Value updatedValue = initialValue.withOptional(newOptional);

        // Assert: Verify the outcome.
        // 1. The method must return a new object instance, confirming immutability.
        assertNotSame("The withOptional() method should create a new instance.", initialValue, updatedValue);

        // 2. The new instance should not be equal to the original due to the changed 'optional' property.
        assertNotEquals("Instances with different 'optional' values should not be equal.", initialValue, updatedValue);

        // 3. The new instance should have the correct properties.
        assertEquals("The 'id' property should remain unchanged.", initialValue.getId(), updatedValue.getId());
        assertEquals("The 'useInput' property should remain unchanged.", initialUseInput, updatedValue.getUseInput());
        assertEquals("The 'optional' property should be updated to the new value.", newOptional, updatedValue.getOptional());
    }
}