package com.google.common.reflect;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Test for {@link Parameter#getAnnotatedType()}.
 *
 * NOTE: The original class name and inheritance were artifacts from a test generation tool.
 * In a typical, manually written test suite, this class would be named {@code ParameterTest}.
 */
public class Parameter_ESTestTest21 extends Parameter_ESTest_scaffolding {

    /**
     * Verifies that getAnnotatedType() throws a NullPointerException if the Parameter
     * was constructed with a null Invokable, as the method relies on it for its operation.
     */
    @Test(expected = NullPointerException.class)
    public void getAnnotatedType_whenConstructedWithNullInvokable_throwsNullPointerException() {
        // Arrange: Create a Parameter instance with a null Invokable.
        // The constructor allows this, but methods that rely on the Invokable should fail.
        Annotation[] noAnnotations = new Annotation[0];
        Parameter parameterWithNullInvokable = new Parameter(
            /* declaration */ null,
            /* position */ 0,
            /* type */ null,
            /* annotations */ noAnnotations,
            /* annotatedType */ null);

        // Act: Calling getAnnotatedType() is expected to throw a NullPointerException
        // because the declaring Invokable is null.
        parameterWithNullInvokable.getAnnotatedType();

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // in the @Test(expected=...) annotation.
    }
}