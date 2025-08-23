package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JacksonInject.Value} class, focusing on its
 * construction and equality logic.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that the static factory method {@code JacksonInject.Value.construct}
     * creates an instance that is equal to one created directly via its constructor
     * when provided with the same arguments.
     */
    @Test
    public void construct_shouldCreateValueEqualToInstanceFromConstructor() {
        // Arrange
        Object injectionId = new Object();
        Boolean useInput = Boolean.FALSE;
        Boolean isOptional = Boolean.FALSE;

        // Act
        // Create one instance using the constructor
        JacksonInject.Value valueFromConstructor = new JacksonInject.Value(injectionId, useInput, isOptional);
        // Create another instance using the static factory method
        JacksonInject.Value valueFromFactory = JacksonInject.Value.construct(injectionId, useInput, isOptional);

        // Assert
        // 1. The primary assertion: the two instances should be equal
        assertEquals("Instances from constructor and factory method should be equal.",
                valueFromConstructor, valueFromFactory);

        // 2. Per the equals/hashCode contract, their hash codes must also be equal
        assertEquals("Equal objects must have equal hash codes.",
                valueFromConstructor.hashCode(), valueFromFactory.hashCode());

        // 3. A sanity check to ensure the properties were set as expected
        assertTrue("Value should have an ID.", valueFromFactory.hasId());
    }
}