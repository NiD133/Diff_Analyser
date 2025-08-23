package com.google.common.reflect;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import org.junit.Test;

public class Parameter_ESTestTest7 extends Parameter_ESTest_scaffolding {

    /**
     * Tests that getAnnotatedType() throws a ClassCastException if the Parameter
     * was constructed with an object that is not an instance of AnnotatedType.
     *
     * <p>This is an important edge case because the package-private constructor accepts a plain
     * {@code Object} for the annotatedType field to maintain Android compatibility. This test ensures
     * that the public-facing getter correctly handles an invalid type being passed internally.
     */
    @Test
    public void getAnnotatedType_whenConstructedWithInvalidAnnotatedTypeObject_throwsClassCastException() {
        // Arrange: Create a Parameter using its package-private constructor, passing a plain
        // Object where an AnnotatedType is expected.
        Object invalidAnnotatedType = new Object();
        Parameter parameter = new Parameter(
                /* declaration= */ null,
                /* position= */ 0,
                /* type= */ null,
                /* annotations= */ new Annotation[0],
                /* annotatedType= */ invalidAnnotatedType);

        // Act & Assert
        try {
            parameter.getAnnotatedType();
            fail("Expected a ClassCastException because the internal annotatedType was not a "
                    + "java.lang.reflect.AnnotatedType");
        } catch (ClassCastException e) {
            // The exception is expected. We can optionally assert on the message for more rigor.
            String expectedMessageContent = "cannot be cast to java.lang.reflect.AnnotatedType";
            assertTrue(
                    "Exception message should indicate a failed cast to AnnotatedType",
                    e.getMessage().contains(expectedMessageContent));
        }
    }
}