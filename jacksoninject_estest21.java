package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JacksonInject.Value} class.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that the {@code withOptional()} method returns the same instance
     * when the provided 'optional' value is identical to the existing one.
     * This behavior is an important optimization to avoid creating unnecessary objects.
     */
    @Test
    public void withOptional_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange: Create an initial JacksonInject.Value instance.
        Object injectionId = new Object();
        Boolean useInputFlag = false;
        Boolean optionalFlag = false;
        
        JacksonInject.Value initialValue = new JacksonInject.Value(injectionId, useInputFlag, optionalFlag);

        // Act: Call withOptional() with the same value it was constructed with.
        JacksonInject.Value result = initialValue.withOptional(optionalFlag);

        // Assert: The method should return the original instance, not a new one.
        assertSame("Expected the same instance when the optional value is not changed", initialValue, result);
        
        // Also, verify that other properties like the ID are preserved.
        assertTrue("The instance should still have its ID after the call", result.hasId());
    }
}