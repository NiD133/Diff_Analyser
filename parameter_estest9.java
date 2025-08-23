package com.google.common.reflect;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * An improved test for the {@link Parameter} class constructor.
 * This test case focuses on constructor argument validation.
 */
public class ParameterTest {

    /**
     * Verifies that the Parameter constructor throws a NullPointerException
     * when the declaring Invokable is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_whenDeclaringInvokableIsNull_throwsNullPointerException() {
        // The Parameter constructor is package-private, so this test must reside in the same package.
        // It is expected to throw a NullPointerException because the first argument (the 'declaration'
        // Invokable) is non-nullable. The other arguments are provided with valid dummy values.
        new Parameter(
            /* declaration */ null,
            /* position */ 0,
            /* type */ TypeToken.of(String.class),
            /* annotations */ new Annotation[0],
            /* annotatedType */ null);
    }
}