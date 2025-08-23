package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JacksonInject.Value} class, focusing on its construction and property access.
 */
public class JacksonInjectValueTest {

    /**
     * Tests that when a {@link JacksonInject.Value} is constructed with a null 'optional'
     * parameter, the {@code getOptional()} method correctly returns null.
     */
    @Test
    public void getOptional_shouldReturnNull_whenConstructedWithNullOptional() {
        // Arrange
        Object injectionId = new Object();
        Boolean useInputFlag = Boolean.FALSE;
        
        // Act
        // Construct a Value instance where the 'optional' parameter is explicitly null.
        JacksonInject.Value injectValue = JacksonInject.Value.construct(injectionId, useInputFlag, null);
        Boolean actualOptional = injectValue.getOptional();

        // Assert
        // Verify that the ID was set correctly.
        assertTrue("The constructed value should have an ID.", injectValue.hasId());
        
        // Verify that the 'optional' property is null, as provided during construction.
        assertNull("getOptional() should return null when the instance was constructed with null.", actualOptional);
    }
}