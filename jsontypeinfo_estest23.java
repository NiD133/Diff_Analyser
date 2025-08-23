package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link JsonTypeInfo.Value} class.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that the factory method {@code JsonTypeInfo.Value.from()}
     * correctly handles a null input by returning null.
     */
    @Test
    public void from_shouldReturnNull_whenAnnotationIsNull() {
        // Arrange: No arrangement needed as we are testing a null input.
        JsonTypeInfo nullAnnotation = null;

        // Act: Call the factory method with the null input.
        JsonTypeInfo.Value result = JsonTypeInfo.Value.from(nullAnnotation);

        // Assert: The result should be null.
        assertNull("The factory method should return null when the source annotation is null.", result);
    }
}