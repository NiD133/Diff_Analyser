package com.google.common.reflect;

import static org.junit.Assert.assertFalse;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Tests for {@link Parameter}.
 */
public class ParameterTest {

    /**
     * The {@code equals} method should return false when the object is compared with null,
     * as per the general contract of {@link Object#equals(Object)}.
     */
    @Test
    public void equals_whenComparedWithNull_shouldReturnFalse() {
        // Arrange: Create a Parameter instance. The specific constructor arguments are not
        // relevant for this test, as any instance will do for a null comparison.
        Annotation[] emptyAnnotations = new Annotation[0];
        Parameter parameter = new Parameter(null, 0, null, emptyAnnotations, null);

        // Act & Assert: Verify that the parameter is not equal to null.
        assertFalse(parameter.equals(null));
    }
}