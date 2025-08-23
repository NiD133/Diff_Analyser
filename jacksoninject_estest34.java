package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JacksonInject.Value} class.
 */
public class JacksonInjectValueTest {

    /**
     * Tests that the {@code valueFor()} method on a {@link JacksonInject.Value}
     * instance correctly returns the {@link JacksonInject} annotation class.
     * This is part of the contract from the {@link JacksonAnnotationValue} interface.
     */
    @Test
    public void valueFor_shouldReturnJacksonInjectAnnotationClass() {
        // Arrange: Create an instance of the class under test.
        // The specific state (e.g., empty) of the Value instance does not affect this behavior.
        JacksonInject.Value injectValue = JacksonInject.Value.empty();

        // Act: Call the method being tested.
        Class<JacksonInject> annotationType = injectValue.valueFor();

        // Assert: Verify that the returned class is exactly the JacksonInject annotation class.
        assertEquals(JacksonInject.class, annotationType);
    }
}