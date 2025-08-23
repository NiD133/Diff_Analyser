package com.google.common.reflect;

import org.junit.Test;
import java.lang.annotation.Annotation;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link Parameter}.
 */
public class ParameterTest {

    /**
     * Verifies that two Parameter instances are not equal if they belong to different
     * declaring invokables, even when all other properties (position, type) are identical.
     */
    @Test
    public void equals_returnsFalse_whenDeclaringInvokablesDiffer() {
        // Arrange
        // 1. Create two distinct mock Invokable instances to represent different methods/constructors.
        Invokable<?, ?> declaringInvokableOne = mock(Invokable.class);
        Invokable<?, ?> declaringInvokableTwo = mock(Invokable.class);

        // 2. Define common properties for the parameters.
        int parameterPosition = 0;
        TypeToken<Annotation> parameterType = TypeToken.of(Annotation.class);
        Annotation[] noAnnotations = new Annotation[0];
        Class<Annotation> annotatedType = Annotation.class; // Used for an internal constructor argument.

        // 3. Create two Parameter instances. They are identical in every way except for their
        //    declaring Invokable.
        Parameter parameterOne = new Parameter(
                declaringInvokableOne, parameterPosition, parameterType, noAnnotations, annotatedType);
        Parameter parameterTwo = new Parameter(
                declaringInvokableTwo, parameterPosition, parameterType, noAnnotations, annotatedType);

        // Act & Assert
        // The equals method should return false because the parameters belong to different invokables.
        assertFalse("Parameters with different declaring invokables should not be equal",
                parameterOne.equals(parameterTwo));
    }
}