package com.google.common.reflect;

import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Tests for {@link Parameter}.
 */
public class ParameterTest {

    @Test(expected = NullPointerException.class)
    public void getDeclaredAnnotationsByType_whenAnnotationTypeIsNull_throwsNullPointerException() {
        // Arrange: Create a Parameter instance.
        // The specific constructor arguments are not relevant to this test, as we are only
        // verifying the null-check on the method's input parameter.
        Invokable<?, ?> mockInvokable = mock(Invokable.class);
        TypeToken<?> typeToken = TypeToken.of(String.class);
        Annotation[] annotations = new Annotation[0];
        Parameter parameter = new Parameter(mockInvokable, 0, typeToken, annotations, null);

        // Act: Call the method with a null argument.
        // The @Test(expected=...) annotation will assert that a NullPointerException is thrown.
        parameter.getDeclaredAnnotationsByType(null);
    }
}