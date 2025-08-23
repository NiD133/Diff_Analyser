package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.*;

public class JacksonInject_ESTestTest1 extends JacksonInject_ESTest_scaffolding {

    /**
     * Tests that the `withOptional()` method creates a new, distinct `JacksonInject.Value`
     * instance when the optional flag is changed. It also verifies that other properties
     * are correctly carried over to the new instance.
     */
    @Test
    public void withOptional_whenChanged_createsNewUnequalInstance() {
        // Arrange: Create an initial JacksonInject.Value instance.
        Object injectionId = new Object();
        // The original instance has useInput=false and optional=true.
        JacksonInject.Value initialValue = new JacksonInject.Value(injectionId, Boolean.FALSE, Boolean.TRUE);

        // Act: Create a new instance by changing the 'optional' property to false.
        JacksonInject.Value updatedValue = initialValue.withOptional(Boolean.FALSE);

        // Assert: Verify the state of both the original and new instances.

        // 1. The two instances should not be equal because their 'optional' value differs.
        assertNotEquals(initialValue, updatedValue);

        // 2. Verify properties of the original instance remain unchanged.
        assertTrue("Original value should have an ID", initialValue.hasId());
        assertFalse("Original 'useInput' should be false", initialValue.getUseInput());
        assertTrue("Original 'optional' should be true", initialValue.getOptional());

        // 3. Verify properties of the new instance.
        assertTrue("New value should have an ID", updatedValue.hasId());
        assertEquals("ID should be carried over to the new instance", injectionId, updatedValue.getId());
        assertFalse("New 'useInput' should be false", updatedValue.getUseInput());
        assertFalse("New 'optional' should be false", updatedValue.getOptional());
    }
}