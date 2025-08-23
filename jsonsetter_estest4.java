package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link JsonSetter.Value} class, focusing on its equality logic.
 */
public class JsonSetterValueTest {

    /**
     * Verifies that the {@code equals} method of {@link JsonSetter.Value} returns {@code false}
     * when compared with an object of a different, non-compatible type.
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange: Create a JsonSetter.Value instance and an object of a different type.
        final Nulls nullsHandling = Nulls.AS_EMPTY;
        final JsonSetter.Value jsonSetterValue = JsonSetter.Value.forValueNulls(nullsHandling, nullsHandling);
        final Object otherObject = new Object();

        // Act & Assert: The equals method should return false.
        assertFalse("JsonSetter.Value should not be equal to a generic Object.",
                jsonSetterValue.equals(otherObject));

        // Sanity check to ensure the value object was constructed as expected.
        assertEquals("The valueNulls property should be correctly initialized.",
                Nulls.AS_EMPTY, jsonSetterValue.getValueNulls());
        assertEquals("The contentNulls property should be correctly initialized.",
                Nulls.AS_EMPTY, jsonSetterValue.getContentNulls());
    }
}