package com.google.common.reflect;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Annotation;

/**
 * This class contains the refactored test case.
 * The original class name and extension are preserved to maintain context.
 */
public class Parameter_ESTestTest24 extends Parameter_ESTest_scaffolding {

    /**
     * Tests that getDeclaredAnnotation() returns null when the parameter has no annotations.
     */
    @Test
    public void getDeclaredAnnotation_whenNoAnnotationsPresent_returnsNull() {
        // Arrange: Create a Parameter instance with an empty annotation array.
        // The other constructor arguments are irrelevant to this test's purpose
        // (verifying annotation lookup) and are supplied with placeholder values.
        Annotation[] noAnnotations = new Annotation[0];
        Parameter parameterWithNoAnnotations = new Parameter(
                /* declaration= */ null,
                /* position= */ 0,
                /* type= */ null,
                /* annotations= */ noAnnotations,
                /* annotatedType= */ null);

        // Act: Attempt to retrieve an annotation from the parameter.
        Annotation foundAnnotation = parameterWithNoAnnotations.getDeclaredAnnotation(Annotation.class);

        // Assert: Verify that the result is null, as expected.
        assertNull(
            "Expected null when searching for an annotation on a parameter with no annotations.",
            foundAnnotation);
    }
}