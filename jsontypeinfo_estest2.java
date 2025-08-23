package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link JsonTypeInfo.Value} class.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that the `withPropertyName` method returns the same instance
     * if the property name is not changed. This is an important optimization
     * for immutable value objects to avoid unnecessary allocations.
     */
    @Test
    public void withPropertyName_whenNameIsUnchanged_shouldReturnSameInstance() {
        // Arrange: The EMPTY constant has a null property name by default.
        JsonTypeInfo.Value initialValue = JsonTypeInfo.Value.EMPTY;
        String currentPropertyName = null;

        // Act: Call the method with the same property name.
        JsonTypeInfo.Value resultValue = initialValue.withPropertyName(currentPropertyName);

        // Assert: The method should return the original instance, not a new one.
        assertSame("Expected the same instance to be returned for an unchanged property name",
                initialValue, resultValue);
    }
}