package com.google.common.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Unit tests for {@link Parameter}.
 */
public class ParameterTest {

    @Test
    public void getAnnotationsByType_whenNoAnnotationsExist_returnsNewEmptyArray() {
        // Arrange: Create a Parameter instance that has no annotations.
        Invokable<?, ?> mockDeclaringInvokable = mock(Invokable.class);
        TypeToken<?> parameterType = TypeToken.of(String.class);
        Annotation[] emptyAnnotations = new Annotation[0];

        // The Parameter constructor is package-private, so this test must reside
        // in the same package.
        Parameter parameter = new Parameter(
                mockDeclaringInvokable,
                0, // position
                parameterType,
                emptyAnnotations,
                null // annotatedType
        );

        // Act: Request annotations of a specific type from the parameter.
        Annotation[] foundAnnotations = parameter.getAnnotationsByType(Annotation.class);

        // Assert: Verify that the result is a new, empty array.
        assertNotNull("The returned array should not be null.", foundAnnotations);
        assertEquals("The returned array should be empty.", 0, foundAnnotations.length);

        // This is the key assertion: the method should return a new array instance
        // to prevent modification of the Parameter's internal state.
        assertNotSame(
                "Should return a new array instance, not the internal one.",
                emptyAnnotations,
                foundAnnotations);
    }
}