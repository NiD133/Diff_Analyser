package com.google.common.reflect;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Unit tests for {@link Parameter}.
 */
public class ParameterTest {

    /**
     * Verifies that the Parameter constructor throws a NullPointerException
     * when the provided annotations array is null. This is expected because the constructor
     * uses this array to create an ImmutableList, which does not accept null collections.
     */
    @Test(expected = NullPointerException.class)
    public void constructor_withNullAnnotationsArray_throwsNullPointerException() {
        // The constructor is expected to fail immediately when processing the null annotations array.
        // Therefore, the other arguments can be dummy values for this specific test case.
        new Parameter(
            /* declaration */ null,
            /* position */ 0,
            /* type */ null,
            /* annotations */ (Annotation[]) null,
            /* annotatedType */ null);
    }
}