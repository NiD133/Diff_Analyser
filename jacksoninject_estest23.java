package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite focuses on the behavior of the {@link JacksonInject.Value} class,
 * specifically its factory and equality methods.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that the equals() method correctly distinguishes between two
     * JacksonInject.Value instances when their 'optional' property differs.
     */
    @Test
    public void equals_shouldReturnFalse_whenOptionalPropertyIsDifferent() {
        // Arrange: Create an initial Value instance with a specific ID.
        // By default, its 'optional' property is null.
        Object id = new Object();
        JacksonInject.Value initialValue = JacksonInject.Value.forId(id);

        // Act: Create a new Value instance by modifying the 'optional' property.
        // The withOptional() method is immutable and returns a new instance.
        JacksonInject.Value modifiedValue = initialValue.withOptional(Boolean.FALSE);

        // Assert: The two instances should not be considered equal.
        // We use assertNotEquals for better readability than assertFalse(a.equals(b)).
        assertNotEquals("Instances with different 'optional' values should not be equal",
                initialValue, modifiedValue);

        // Sanity check: Ensure the ID was preserved in the new instance.
        assertTrue("The new instance should still have an ID", modifiedValue.hasId());
        assertEquals("The ID in the new instance should match the original",
                id, modifiedValue.getId());
    }
}