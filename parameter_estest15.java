package com.google.common.reflect;

import static org.junit.Assert.assertNotEquals;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Tests for {@link Parameter#equals(Object)}.
 */
public class ParameterTest {

    @Test
    public void equals_whenParametersHaveDifferentPositions_returnsFalse() {
        // Arrange
        // Create two Parameter objects that are otherwise identical but have different positions.
        // The other constructor arguments (Invokable, TypeToken) are null because they are not
        // relevant to this specific test of the position field.
        Annotation[] noAnnotations = new Annotation[0];

        Parameter parameterAtPosition0 = new Parameter(
                /* declaration */ null,
                /* position */ 0,
                /* type */ null,
                noAnnotations,
                /* annotatedType */ null);

        Parameter parameterAtPosition1 = new Parameter(
                /* declaration */ null,
                /* position */ 1,
                /* type */ null,
                noAnnotations,
                /* annotatedType */ null);

        // Act & Assert
        // Two Parameter objects should not be equal if their positions differ,
        // even if they belong to the same declaring method or constructor.
        assertNotEquals(parameterAtPosition0, parameterAtPosition1);
    }
}