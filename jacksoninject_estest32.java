package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for the {@link JacksonInject.Value} class.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that the factory method {@link JacksonInject.Value#from(JacksonInject)}
     * throws a NullPointerException if the source annotation returns null for its properties.
     * <p>
     * The {@code from()} method internally attempts to call methods on the results of
     * {@code useInput()} and {@code optional()}, and will fail if those methods return null
     * instead of a valid {@link OptBoolean} instance.
     */
    @Test(expected = NullPointerException.class)
    public void from_whenAnnotationReturnsNullProperties_shouldThrowNullPointerException() {
        // Arrange: Create a mock JacksonInject annotation that is misconfigured
        // to return null for its properties.
        JacksonInject mockAnnotation = mock(JacksonInject.class);
        when(mockAnnotation.value()).thenReturn(null);
        when(mockAnnotation.useInput()).thenReturn(null); // This is the direct cause of the NPE.

        // Act: Attempt to create a Value instance from the misconfigured annotation.
        JacksonInject.Value.from(mockAnnotation);

        // Assert: The test is expected to throw a NullPointerException, which is
        // declared in the @Test annotation.
    }
}