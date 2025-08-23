package com.google.common.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Unit tests for {@link Parameter}.
 */
public class ParameterTest {

    @Test
    public void getAnnotations_whenParameterHasNoAnnotations_returnsEmptyArray() {
        // Arrange: Create a Parameter instance that has no annotations.
        Invokable<?, ?> mockDeclaringInvokable = mock(Invokable.class);
        TypeToken<?> parameterType = TypeToken.of(String.class);
        Annotation[] noAnnotations = new Annotation[0];

        // The 'annotatedType' parameter is not relevant for this test, so we pass null.
        Parameter parameter = new Parameter(
            mockDeclaringInvokable,
            0, // position
            parameterType,
            noAnnotations,
            null // annotatedType
        );

        // Act: Call the method under test.
        Annotation[] actualAnnotations = parameter.getAnnotations();

        // Assert: Verify that the returned array is empty.
        assertNotNull("getAnnotations() should never return null.", actualAnnotations);
        assertEquals("Expected an empty array for a parameter with no annotations.", 0, actualAnnotations.length);
    }
}