package com.google.common.reflect;

import com.google.common.reflect.Invokable;
import com.google.common.reflect.Parameter;
import com.google.common.reflect.TypeToken;
import org.junit.Test;
import javax.annotation.Nullable; // A standard, simple annotation for the test case
import java.lang.annotation.Annotation;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class Parameter_ESTestTest18 extends Parameter_ESTest_scaffolding {

    /**
     * This test was refactored to improve its clarity and maintainability.
     * Original name: test17
     */
    @Test
    public void isAnnotationPresent_whenParameterHasNoAnnotations_returnsFalse() {
        // Arrange: Create a Parameter instance that has an empty array of annotations.
        Invokable<?, ?> mockDeclaringInvokable = mock(Invokable.class);
        TypeToken<?> parameterType = TypeToken.of(String.class);
        Annotation[] noAnnotations = new Annotation[0];

        // The constructor is package-private. The last argument is 'annotatedType'.
        Parameter parameter = new Parameter(
                mockDeclaringInvokable,
                0, // position
                parameterType,
                noAnnotations,
                null // annotatedType
        );

        // Act: Check for the presence of an arbitrary annotation.
        boolean isPresent = parameter.isAnnotationPresent(Nullable.class);

        // Assert: The method should return false because no annotations were provided.
        assertFalse("Expected isAnnotationPresent to return false for a parameter with no annotations.", isPresent);
    }
}