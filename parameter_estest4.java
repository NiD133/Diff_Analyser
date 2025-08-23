package com.google.common.reflect;

import org.junit.Test;
import java.lang.annotation.Annotation;

/**
 * Tests for {@link Parameter}.
 */
public class ParameterTest {

    /**
     * Verifies that getDeclaredAnnotation() throws a NullPointerException
     * when the annotation type argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void getDeclaredAnnotation_whenAnnotationTypeIsNull_throwsNullPointerException() {
        // Arrange: Create a Parameter instance. Its specific state is not relevant
        // for this test, as we are only checking a null-check precondition.
        TypeToken<?> dummyType = TypeToken.of(String.class);
        Annotation[] noAnnotations = new Annotation[0];
        int dummyPosition = 0;

        // The constructor is package-private. We pass null for dependencies
        // not used by the method's precondition check.
        Parameter parameter = new Parameter(
                /* declaration */ null,
                dummyPosition,
                dummyType,
                noAnnotations,
                /* annotatedType */ null);

        // Act & Assert: Calling the method with a null argument should throw.
        // The @Test(expected=...) annotation handles the assertion.
        parameter.getDeclaredAnnotation(null);
    }
}