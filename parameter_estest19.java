package com.google.common.reflect;

import static org.junit.Assert.assertNull;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Tests for {@link Parameter}.
 */
public class ParameterTest {

    /**
     * Verifies that getDeclaringInvokable() returns the same Invokable instance
     * that was provided to the constructor. This specific case tests the null scenario.
     */
    @Test
    public void getDeclaringInvokable_whenConstructedWithNullInvokable_returnsNull() {
        // Arrange: Create a Parameter with a null Invokable.
        // The other constructor arguments are dummy values required to instantiate the object.
        Annotation[] noAnnotations = {};
        Parameter parameter = new Parameter(
                /* declaration */ null,
                /* position */ 0,
                /* type */ null,
                /* annotations */ noAnnotations,
                /* annotatedType */ null);

        // Act: Call the method under test.
        Invokable<?, ?> declaringInvokable = parameter.getDeclaringInvokable();

        // Assert: The returned Invokable should be null, matching the value passed to the constructor.
        assertNull("The declaring invokable should be null as provided in the constructor.", declaringInvokable);
    }
}