package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its factory methods.
 */
public class JsonSetterValueTest {

    /**
     * Verifies that the factory method {@link JsonSetter.Value#forValueNulls(Nulls, Nulls)}
     * correctly constructs an instance with the specified null-handling strategies
     * for both the main value and its content.
     */
    @Test
    public void shouldCreateValueWithSpecificValueAndContentNulls() {
        // Arrange: Define the desired null-handling strategy.
        final Nulls nullsStrategy = Nulls.AS_EMPTY;

        // Act: Create a JsonSetter.Value instance using the factory method.
        JsonSetter.Value setterValue = JsonSetter.Value.forValueNulls(nullsStrategy, nullsStrategy);

        // Assert: Verify that both value and content null-handling strategies are set as expected.
        assertEquals("The value nulls strategy should match the constructor argument.",
                nullsStrategy, setterValue.getValueNulls());
        assertEquals("The content nulls strategy should match the constructor argument.",
                nullsStrategy, setterValue.getContentNulls());
    }
}