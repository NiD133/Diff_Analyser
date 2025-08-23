package com.google.common.reflect;

import static org.junit.Assert.assertThrows;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Tests for {@link Parameter}, focusing on its behavior in edge cases.
 */
public class ParameterTest {

    /**
     * Verifies that calling equals() on a Parameter instance created with a null
     * declaring Invokable throws a NullPointerException. The equals() implementation
     * internally dereferences the Invokable, leading to this expected exception.
     */
    @Test
    public void equals_whenDeclaringInvokableIsNull_throwsNullPointerException() {
        // Arrange: Create a Parameter instance with a null Invokable.
        // Note: The Parameter constructor is package-private, so this test must
        // reside in the com.google.common.reflect package to compile.
        Annotation[] noAnnotations = new Annotation[0];
        Parameter parameterWithNullInvokable = new Parameter(
                /* declaration */ null,
                /* position */ 0,
                /* type */ null,
                noAnnotations,
                /* annotatedType */ null);

        // Act & Assert: Expect a NullPointerException when equals() is called.
        assertThrows(
                "Expected equals() to throw NullPointerException when the declaring invokable is null",
                NullPointerException.class,
                () -> parameterWithNullInvokable.equals(parameterWithNullInvokable));
    }
}