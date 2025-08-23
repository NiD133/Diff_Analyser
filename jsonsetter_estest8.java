package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on the
 * behavior of its property accessors.
 */
public class JsonSetterValueTest {

    /**
     * Tests that nonDefaultValueNulls() returns null when the instance
     * is constructed with the default Nulls value.
     */
    @Test
    public void nonDefaultValueNulls_shouldReturnNull_whenValueIsDefault() {
        // Arrange: Create a JsonSetter.Value instance where valueNulls is Nulls.DEFAULT.
        JsonSetter.Value setterValue = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);

        // Act: Call the method under test.
        Nulls result = setterValue.nonDefaultValueNulls();

        // Assert: The method should return null for a default value, as per its contract.
        assertNull("Expected null when valueNulls is DEFAULT", result);
    }

    /**
     * Tests that nonDefaultValueNulls() returns the actual value when the instance
     * is constructed with a non-default Nulls value.
     */
    @Test
    public void nonDefaultValueNulls_shouldReturnValue_whenValueIsNonDefault() {
        // Arrange: Create a JsonSetter.Value instance with a specific, non-default value.
        Nulls expectedNulls = Nulls.SET;
        JsonSetter.Value setterValue = JsonSetter.Value.construct(expectedNulls, Nulls.DEFAULT);

        // Act: Call the method under test.
        Nulls result = setterValue.nonDefaultValueNulls();

        // Assert: The method should return the specific value it was configured with.
        assertEquals("Expected the non-default Nulls value to be returned", expectedNulls, result);
    }
}