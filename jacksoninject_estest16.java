package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JacksonInject.Value} class, focusing on its
 * construction and equality logic.
 */
public class JacksonInjectValueTest {

    /**
     * Tests that a {@link JacksonInject.Value} instance constructed with an ID
     * is not considered equal to the default empty instance.
     * It also verifies that the constructed instance correctly reports having an ID.
     */
    @Test
    public void constructedValueWithIdShouldNotBeEqualToEmptyValue() {
        // Arrange
        // The 'empty' instance serves as a baseline, with all properties (id, useInput, optional) being null.
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();
        
        // For this test, we'll use the 'emptyValue' object itself as the ID for a new Value instance.
        // Any non-null object can serve as an ID.
        Object injectionId = emptyValue;

        // Act
        // Construct a new instance, providing the ID and specific boolean flags.
        JacksonInject.Value valueWithId = JacksonInject.Value.construct(injectionId, false, false);

        // Assert
        // The new value should not be equal to the empty value because its ID is set, while the empty value's ID is null.
        assertFalse("Value with an ID should not be equal to the empty value",
                valueWithId.equals(emptyValue));
        
        // The new value should correctly report that it has an ID.
        assertTrue("Value constructed with an ID should report hasId() as true",
                valueWithId.hasId());
    }
}