package com.google.common.reflect;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Tests for {@link Parameter}.
 * This class contains the refactored test case.
 */
public class ParameterTest { // Renamed for clarity

    @Test
    public void equals_whenInvokableAndPositionDiffer_returnsFalse() {
        // Arrange: Set up two distinct Parameter objects.
        
        // 1. Define common properties for creating the parameters.
        TypeToken<Annotation> typeToken = TypeToken.of(Annotation.class);
        Annotation[] noAnnotations = new Annotation[0];
        Object dummyAnnotatedType = new Object();

        // 2. Create the first parameter with a mocked Invokable and a specific position.
        Invokable<?, ?> mockInvokable = mock(Invokable.class);
        int position1 = 1;
        Parameter parameter1 = new Parameter(
                mockInvokable, position1, typeToken, noAnnotations, dummyAnnotatedType);

        // 3. Create a second parameter that differs by its declaring invokable (null) and position.
        // The identity of a Parameter is primarily defined by its declaration and position.
        int position2 = 2;
        Parameter parameter2 = new Parameter(
                null, position2, typeToken, noAnnotations, dummyAnnotatedType);

        // Act: Compare the two objects for equality.
        boolean areEqual = parameter1.equals(parameter2);

        // Assert: Verify that they are not equal.
        assertFalse(
                "Parameters with different declaring invokables and positions should not be equal.",
                areEqual);
    }
}