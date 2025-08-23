package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its core functionality.
 */
public class JsonSetterValueTest {

    /**
     * Tests that the {@link JsonSetter.Value#valueFor()} method correctly returns
     * the {@link JsonSetter} annotation class.
     */
    @Test
    public void valueFor_shouldReturnJsonSetterAnnotationClass() {
        // Arrange: Create a default instance of JsonSetter.Value.
        // The specific configuration of the instance does not affect the outcome of valueFor().
        JsonSetter.Value jsonSetterValue = JsonSetter.Value.empty();

        // Act: Call the method under test.
        Class<JsonSetter> annotationType = jsonSetterValue.valueFor();

        // Assert: Verify that the returned class is exactly JsonSetter.class.
        // This is a more direct and readable check than asserting on the integer
        // value of the class modifiers.
        assertEquals(JsonSetter.class, annotationType);
    }
}