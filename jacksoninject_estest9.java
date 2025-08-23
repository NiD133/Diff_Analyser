package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link JacksonInject.Value} class, focusing on its immutability
 * and "wither" methods.
 */
public class JacksonInjectValueTest {

    @Test
    public void witherMethods_shouldReturnNewImmutableInstance_withUpdatedState() {
        // Arrange
        final JacksonInject.Value emptyValue = JacksonInject.Value.EMPTY;
        final Object injectionId = new Object();

        // Act: Chain "wither" methods to create a new configured instance.
        // First, create a new Value with 'useInput' set to false.
        JacksonInject.Value valueWithUseInputSet = emptyValue.withUseInput(Boolean.FALSE);
        // Then, use that new instance to create a final Value with an ID.
        JacksonInject.Value finalValue = valueWithUseInputSet.withId(injectionId);

        // Assert

        // 1. Verify that withUseInput() creates a new, distinct instance (immutability).
        assertNotSame("withUseInput() should return a new instance.", emptyValue, valueWithUseInputSet);
        assertNotEquals("The new instance should not be equal to the original.", emptyValue, valueWithUseInputSet);

        // 2. Verify the final instance has the correct ID set.
        assertTrue("hasId() should be true after setting an ID.", finalValue.hasId());
        assertEquals("The ID should match the object that was set.", injectionId, finalValue.getId());

        // 3. Verify the original EMPTY instance remains unchanged.
        assertNull("The original EMPTY instance should not be mutated.", emptyValue.getId());
        assertFalse("The original EMPTY instance should not have an ID.", emptyValue.hasId());
    }
}