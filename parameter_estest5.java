package com.google.common.reflect;

import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link Parameter}.
 */
public class ParameterTest {

    @Test
    public void getAnnotationsByType_whenTypeIsNull_throwsNullPointerException() {
        // Arrange: Create a Parameter instance. The constructor arguments are mostly
        // irrelevant for this test, as we are only checking the null-handling of the method.
        Annotation[] noAnnotations = {};
        Parameter parameter = new Parameter(null, 0, null, noAnnotations, null);

        // Act & Assert: Verify that passing a null class throws a NullPointerException.
        // This is the expected behavior, as the contract is enforced by Preconditions.checkNotNull.
        assertThrows(NullPointerException.class, () -> {
            parameter.getAnnotationsByType(null);
        });
    }
}