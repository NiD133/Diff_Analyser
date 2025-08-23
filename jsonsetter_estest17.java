package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its "wither" methods.
 */
public class JsonSetterValueTest {

    /**
     * Tests that calling {@code withValueNulls(null)} returns the original instance.
     * This confirms the optimization that avoids creating a new object when no
     * actual change is requested.
     */
    @Test
    public void withValueNulls_shouldReturnSameInstance_whenGivenNull() {
        // Arrange: Create an initial JsonSetter.Value instance.
        JsonSetter.Value originalValue = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);

        // Act: Call the method under test with a null argument.
        JsonSetter.Value result = originalValue.withValueNulls(null);

        // Assert: The method should return the exact same instance, not a new one.
        assertSame("Expected the same instance to be returned when withValueNulls is called with null.",
                originalValue, result);
    }
}