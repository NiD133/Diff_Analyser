package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link JacksonInject.Value} class, focusing on its
 * immutable factory methods.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that the wither-style method {@code withUseInput} returns the
     * same instance if the new value is identical to the existing one.
     * This is an important optimization for immutable objects to avoid
     * unnecessary allocations.
     */
    @Test
    public void withUseInput_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange: Create an empty Value instance. By default, its 'useInput' property is null.
        JacksonInject.Value initialValue = JacksonInject.Value.empty();

        // Act: Call withUseInput with null, which does not change the object's state.
        JacksonInject.Value resultValue = initialValue.withUseInput(null);

        // Assert: The method should return the original instance, not a new one.
        assertSame("Expected the same instance to be returned for an unchanged value",
                initialValue, resultValue);
    }
}