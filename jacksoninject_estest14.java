package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link JacksonInject.Value} class, focusing on its
 * immutability and equality implementation.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that modifying a property on a {@link JacksonInject.Value} instance
     * using a "wither" method (e.g., {@code withUseInput()}) produces a new,
     * non-equal instance.
     * <p>
     * This test confirms the immutability of the {@code Value} class and the
     * correctness of its {@code equals()} implementation.
     */
    @Test
    public void withUseInput_whenValueChanges_shouldReturnNewUnequalInstance() {
        // Arrange: Start with the default EMPTY instance, where 'useInput' is null.
        JacksonInject.Value originalValue = JacksonInject.Value.EMPTY;

        // Act: Create a new instance by changing the 'useInput' property to false.
        JacksonInject.Value modifiedValue = originalValue.withUseInput(Boolean.FALSE);

        // Assert: The new instance with a different 'useInput' value should not be
        // equal to the original instance.
        assertNotEquals("A Value instance modified with a new 'useInput' should not be equal to the original.",
                originalValue, modifiedValue);
    }
}