package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link JacksonInject.Value} class, focusing on its immutability.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that the "wither" methods (`withOptional`, `withUseInput`)
     * create new, distinct instances of {@link JacksonInject.Value},
     * confirming the class's immutable design. It also ensures that the
     * original ID is preserved across these transformations.
     */
    @Test
    public void witherMethodsShouldCreateNewImmutableInstances() {
        // Arrange: Create an initial Value instance with a specific ID.
        Object injectionId = new Object();
        JacksonInject.Value initialValue = JacksonInject.Value.forId(injectionId);

        // Act: Create a new instance by setting the 'optional' property.
        JacksonInject.Value valueAfterOptional = initialValue.withOptional(Boolean.FALSE);

        // Assert: The new instance should be a different object and not equal to the original.
        assertNotSame("withOptional() should return a new instance.", initialValue, valueAfterOptional);
        assertNotEquals("The new instance from withOptional() should not be equal to the original.", initialValue, valueAfterOptional);

        // Act: Create a third instance by setting the 'useInput' property on the second one.
        JacksonInject.Value finalValue = valueAfterOptional.withUseInput(Boolean.FALSE);

        // Assert: The final instance should also be a different object and not equal to the intermediate one.
        assertNotSame("withUseInput() should return a new instance.", valueAfterOptional, finalValue);
        assertNotEquals("The new instance from withUseInput() should not be equal to the previous one.", valueAfterOptional, finalValue);

        // Assert: The ID should be preserved throughout the transformations.
        assertTrue("The final instance should still have an ID.", finalValue.hasId());
        assertEquals("The ID in the final instance should match the original ID.", injectionId, finalValue.getId());
    }
}