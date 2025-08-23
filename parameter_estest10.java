package com.google.common.reflect;

import static org.junit.Assert.assertNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.Test;

/**
 * Tests for {@link Parameter}.
 */
public class ParameterTest {

    /** A sample annotation used for testing purposes. */
    @Retention(RetentionPolicy.RUNTIME)
    private @interface TestAnnotation {}

    /**
     * Verifies that getAnnotation() returns null when the requested annotation type
     * is not present on the parameter.
     */
    @Test
    public void getAnnotation_whenAnnotationIsAbsent_returnsNull() {
        // Arrange: Create a Parameter instance that has no annotations.
        // Note: The Parameter constructor is package-private, so this test must reside
        // in the same package. The invokable, position, and annotatedType are not
        // relevant for this test and can be set to dummy values.
        TypeToken<String> type = TypeToken.of(String.class);
        Annotation[] noAnnotations = new Annotation[0];
        Parameter parameter = new Parameter(/* declaration */ null, /* position */ 0, type, noAnnotations, /* annotatedType */ null);

        // Act: Attempt to retrieve an annotation that does not exist.
        TestAnnotation foundAnnotation = parameter.getAnnotation(TestAnnotation.class);

        // Assert: Verify that the result is null.
        assertNull("Expected getAnnotation() to return null for an absent annotation.", foundAnnotation);
    }
}