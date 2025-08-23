package com.google.common.reflect;

import java.lang.reflect.Method;
import org.junit.Test;

/**
 * Unit tests for {@link Parameter}.
 */
public class ParameterTest {

    /** A simple fixture class to obtain a real method and parameter for testing. */
    private static class MethodFixture {
        @SuppressWarnings("unused") // This method is used via reflection.
        void sampleMethod(String aParameter) {}
    }

    @Test(expected = NullPointerException.class)
    public void isAnnotationPresent_whenAnnotationTypeIsNull_throwsNullPointerException() throws Exception {
        // Arrange: Create a valid Parameter instance from a real method.
        Method method = MethodFixture.class.getDeclaredMethod("sampleMethod", String.class);
        Invokable<?, ?> invokable = Invokable.from(method);
        Parameter parameter = invokable.getParameters().get(0);

        // Act: Call the method under test with a null argument.
        // The @Test(expected) annotation handles the assertion.
        parameter.isAnnotationPresent(null);
    }
}