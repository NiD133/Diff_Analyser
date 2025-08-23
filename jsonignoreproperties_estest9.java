package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the {@link JsonIgnoreProperties.Value} class,
 * particularly its factory and "mutant factory" (with/without) methods.
 */
public class JsonIgnoreProperties_ValueTest {

    @Test
    public void withoutAllowGetters_shouldCreateNewInstance_withAllowGettersSetToFalse() {
        // Arrange: Create a Value instance with allowGetters=true and other non-default properties.
        // The initial state is: ignoreUnknown=false, allowGetters=true, allowSetters=true, merge=true
        JsonIgnoreProperties.Value originalValue = JsonIgnoreProperties.Value.construct(
                Collections.emptySet(), // ignored properties
                false,                  // ignoreUnknown
                true,                   // allowGetters
                true,                   // allowSetters
                true                    // merge
        );

        // Act: Call the method under test to create a new instance with allowGetters disabled.
        JsonIgnoreProperties.Value updatedValue = originalValue.withoutAllowGetters();
        
        // Also, verify the behavior of readResolve(), which is used during deserialization.
        Object resolvedValue = updatedValue.readResolve();

        // Assert
        // 1. Verify the state of the original value object remains unchanged.
        assertTrue("Original value should still have allowGetters enabled", originalValue.getAllowGetters());

        // 2. Verify the state of the new value object.
        assertFalse("New value should have allowGetters disabled", updatedValue.getAllowGetters());
        assertTrue("New value should retain allowSetters setting", updatedValue.getAllowSetters());
        assertFalse("New value should retain ignoreUnknown setting", updatedValue.getIgnoreUnknown());
        assertTrue("New value should retain merge setting", updatedValue.getMerge());

        // 3. Verify the new instance is distinct from the original.
        assertNotSame("A new object instance should be created", originalValue, updatedValue);
        assertNotEquals("The new instance should not be equal to the original", originalValue, updatedValue);

        // 4. Verify that readResolve() returns the same instance because the configuration
        // is not equivalent to the EMPTY singleton.
        assertSame("readResolve() should return the same instance for non-empty configurations",
                updatedValue, resolvedValue);
    }
}