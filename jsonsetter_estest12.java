package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link JsonSetter.Value} class.
 */
public class JsonSetterValueTest {

    /**
     * Tests that the wither-method {@code withContentNulls()} returns the same
     * instance if the new value is identical to the current value.
     * This is a common optimization for immutable objects.
     */
    @Test
    public void withContentNulls_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        // JsonSetter.Value.empty() creates an instance with default values,
        // where contentNulls is Nulls.DEFAULT.
        JsonSetter.Value initialValue = JsonSetter.Value.empty();

        // Act
        // Attempt to set contentNulls to the same value it already has.
        JsonSetter.Value result = initialValue.withContentNulls(Nulls.DEFAULT);

        // Assert
        // The method should return the same instance, not a new one.
        assertSame("Expected the same instance when the value is unchanged", initialValue, result);
    }
}