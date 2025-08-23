package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its
 * adherence to the {@code equals()} method contract.
 */
public class JsonSetterValueTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An instance of JsonSetter.Value should always be equal to itself.
     */
    @Test
    public void shouldBeEqualToItself() {
        // Arrange: Create an instance with default null handling settings.
        JsonSetter.Value valueInstance = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);

        // Act & Assert: An object must be equal to itself.
        assertTrue("An instance of JsonSetter.Value should always be equal to itself.",
                   valueInstance.equals(valueInstance));
    }
}