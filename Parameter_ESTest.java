package com.google.common.reflect;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.reflect.Invokable;
import com.google.common.reflect.Parameter;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Test suite for the Parameter class from Google Guava's reflection utilities.
 * Tests cover parameter creation, annotation handling, equality, and error conditions.
 */
public class ParameterTest {

    // Test annotation for annotation-related tests
    @Retention(RetentionPolicy.RUNTIME)
    private @interface TestAnnotation {
        String value() default "";
    }

    // Common test data
    private static final int PARAMETER_INDEX = 0;
    private static final TypeToken<String> STRING_TYPE_TOKEN = TypeToken.of(String.class);
    private static final TypeToken<Object> OBJECT_TYPE_TOKEN = TypeToken.of(Object.class);
    private static final Annotation[] EMPTY_ANNOTATIONS = new Annotation[0];

    // Helper method to create a basic parameter for testing
    private Parameter createBasicParameter() {
        Invokable<?, ?> mockInvokable = mock(Invokable.class);
        return new Parameter(mockInvokable, PARAMETER_INDEX, STRING_TYPE_TOKEN, EMPTY_ANNOTATIONS, null);
    }

    // === Constructor Tests ===

    @Test
    public void constructor_withValidParameters_createsParameter() {
        // Given
        Invokable<?, ?> invokable = mock(Invokable.class);
        int position = 1;
        TypeToken<String> type = STRING_TYPE_TOKEN;
        Annotation[] annotations = EMPTY_ANNOTATIONS;

        // When
        Parameter parameter = new Parameter(invokable, position, type, annotations, null);

        // Then
        assertNotNull(parameter);
        assertEquals(invokable, parameter.getDeclaringInvokable());
        assertEquals(type, parameter.getType());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullAnnotationsArray_throwsNullPointerException() {
        // Given
        Invokable<?, ?> invokable = mock(Invokable.class);
        
        // When & Then
        new Parameter(invokable, 0, STRING_TYPE_TOKEN, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullAnnotationInArray_throwsNullPointerException() {
        // Given
        Invokable<?, ?> invokable = mock(Invokable.class);
        Annotation[] annotationsWithNull = new Annotation[1]; // Contains null element
        
        // When & Then
        new Parameter(invokable, 0, STRING_TYPE_TOKEN, annotationsWithNull, null);
    }

    // === Equality Tests ===

    @Test
    public void equals_sameInstance_returnsTrue() {
        // Given
        Parameter parameter = createBasicParameter();

        // When & Then
        assertTrue("Parameter should equal itself", parameter.equals(parameter));
    }

    @Test
    public void equals_withNull_returnsFalse() {
        // Given
        Parameter parameter = createBasicParameter();

        // When & Then
        assertFalse("Parameter should not equal null", parameter.equals(null));
    }

    @Test
    public void equals_differentInvokables_returnsFalse() {
        // Given
        Invokable<?, ?> invokable1 = mock(Invokable.class);
        Invokable<?, ?> invokable2 = mock(Invokable.class);
        
        Parameter parameter1 = new Parameter(invokable1, 0, STRING_TYPE_TOKEN, EMPTY_ANNOTATIONS, null);
        Parameter parameter2 = new Parameter(invokable2, 0, STRING_TYPE_TOKEN, EMPTY_ANNOTATIONS, null);

        // When & Then
        assertFalse("Parameters with different invokables should not be equal", 
                   parameter1.equals(parameter2));
    }

    @Test
    public void equals_differentPositions_returnsFalse() {
        // Given
        Invokable<?, ?> invokable = mock(Invokable.class);
        Parameter parameter1 = new Parameter(invokable, 0, STRING_TYPE_TOKEN, EMPTY_ANNOTATIONS, null);
        Parameter parameter2 = new Parameter(invokable, 1, STRING_TYPE_TOKEN, EMPTY_ANNOTATIONS, null);

        // When & Then
        assertFalse("Parameters with different positions should not be equal", 
                   parameter1.equals(parameter2));
    }

    // === Annotation Tests ===

    @Test
    public void getAnnotation_withNoAnnotations_returnsNull() {
        // Given
        Parameter parameter = createBasicParameter();

        // When
        TestAnnotation annotation = parameter.getAnnotation(TestAnnotation.class);

        // Then
        assertNull("Should return null when annotation is not present", annotation);
    }

    @Test(expected = NullPointerException.class)
    public void getAnnotation_withNullAnnotationType_throwsNullPointerException() {
        // Given
        Parameter parameter = createBasicParameter();

        // When & Then
        parameter.getAnnotation(null);
    }

    @Test
    public void isAnnotationPresent_withNoAnnotations_returnsFalse() {
        // Given
        Parameter parameter = createBasicParameter();

        // When & Then
        assertFalse("Should return false when annotation is not present", 
                   parameter.isAnnotationPresent(TestAnnotation.class));
    }

    @Test(expected = NullPointerException.class)
    public void isAnnotationPresent_withNullAnnotationType_throwsNullPointerException() {
        // Given
        Parameter parameter = createBasicParameter();

        // When & Then
        parameter.isAnnotationPresent(null);
    }

    @Test
    public void getAnnotations_withNoAnnotations_returnsEmptyArray() {
        // Given
        Parameter parameter = createBasicParameter();

        // When
        Annotation[] annotations = parameter.getAnnotations();

        // Then
        assertNotNull("Annotations array should not be null", annotations);
        assertEquals("Should return empty array when no annotations present", 0, annotations.length);
    }

    @Test
    public void getDeclaredAnnotations_withNoAnnotations_returnsEmptyArray() {
        // Given
        Parameter parameter = createBasicParameter();

        // When
        Annotation[] annotations = parameter.getDeclaredAnnotations();

        // Then
        assertNotNull("Declared annotations array should not be null", annotations);
        assertEquals("Should return empty array when no annotations present", 0, annotations.length);
    }

    @Test
    public void getAnnotationsByType_withNoAnnotations_returnsEmptyArray() {
        // Given
        Parameter parameter = createBasicParameter();

        // When
        TestAnnotation[] annotations = parameter.getAnnotationsByType(TestAnnotation.class);

        // Then
        assertNotNull("Annotations by type array should not be null", annotations);
        assertEquals("Should return empty array when no annotations present", 0, annotations.length);
    }

    @Test(expected = NullPointerException.class)
    public void getAnnotationsByType_withNullAnnotationType_throwsNullPointerException() {
        // Given
        Parameter parameter = createBasicParameter();

        // When & Then
        parameter.getAnnotationsByType(null);
    }

    @Test
    public void getDeclaredAnnotation_withNoAnnotations_returnsNull() {
        // Given
        Parameter parameter = createBasicParameter();

        // When
        TestAnnotation annotation = parameter.getDeclaredAnnotation(TestAnnotation.class);

        // Then
        assertNull("Should return null when declared annotation is not present", annotation);
    }

    @Test(expected = NullPointerException.class)
    public void getDeclaredAnnotation_withNullAnnotationType_throwsNullPointerException() {
        // Given
        Parameter parameter = createBasicParameter();

        // When & Then
        parameter.getDeclaredAnnotation(null);
    }

    @Test
    public void getDeclaredAnnotationsByType_withNoAnnotations_returnsEmptyArray() {
        // Given
        Parameter parameter = createBasicParameter();

        // When
        TestAnnotation[] annotations = parameter.getDeclaredAnnotationsByType(TestAnnotation.class);

        // Then
        assertNotNull("Declared annotations by type array should not be null", annotations);
        assertEquals("Should return empty array when no annotations present", 0, annotations.length);
    }

    @Test(expected = NullPointerException.class)
    public void getDeclaredAnnotationsByType_withNullAnnotationType_throwsNullPointerException() {
        // Given
        Parameter parameter = createBasicParameter();

        // When & Then
        parameter.getDeclaredAnnotationsByType(null);
    }

    // === Getter Tests ===

    @Test
    public void getDeclaringInvokable_returnsCorrectInvokable() {
        // Given
        Invokable<?, ?> expectedInvokable = mock(Invokable.class);
        Parameter parameter = new Parameter(expectedInvokable, 0, STRING_TYPE_TOKEN, EMPTY_ANNOTATIONS, null);

        // When
        Invokable<?, ?> actualInvokable = parameter.getDeclaringInvokable();

        // Then
        assertEquals("Should return the invokable passed to constructor", 
                    expectedInvokable, actualInvokable);
    }

    @Test
    public void getDeclaringInvokable_withNullInvokable_returnsNull() {
        // Given
        Parameter parameter = new Parameter(null, 0, STRING_TYPE_TOKEN, EMPTY_ANNOTATIONS, null);

        // When
        Invokable<?, ?> invokable = parameter.getDeclaringInvokable();

        // Then
        assertNull("Should return null when invokable is null", invokable);
    }

    @Test
    public void getType_returnsCorrectType() {
        // Given
        Parameter parameter = createBasicParameter();

        // When
        TypeToken<?> actualType = parameter.getType();

        // Then
        assertEquals("Should return the type passed to constructor", 
                    STRING_TYPE_TOKEN, actualType);
    }

    @Test
    public void getType_withNullType_returnsNull() {
        // Given
        Parameter parameter = new Parameter(null, 0, null, EMPTY_ANNOTATIONS, null);

        // When
        TypeToken<?> type = parameter.getType();

        // Then
        assertNull("Should return null when type is null", type);
    }

    // === AnnotatedType Tests ===

    @Test(expected = NullPointerException.class)
    public void getAnnotatedType_withNullAnnotatedType_throwsNullPointerException() {
        // Given
        Parameter parameter = new Parameter(null, 0, null, EMPTY_ANNOTATIONS, null);

        // When & Then
        parameter.getAnnotatedType();
    }

    @Test(expected = ClassCastException.class)
    public void getAnnotatedType_withInvalidAnnotatedType_throwsClassCastException() {
        // Given - Using a regular Object instead of AnnotatedType
        Object invalidAnnotatedType = new Object();
        Parameter parameter = new Parameter(null, 0, null, EMPTY_ANNOTATIONS, invalidAnnotatedType);

        // When & Then
        parameter.getAnnotatedType();
    }

    // === String Representation Tests ===

    @Test
    public void toString_withNullType_includesParameterIndex() {
        // Given
        int parameterIndex = 5;
        Parameter parameter = new Parameter(null, parameterIndex, null, EMPTY_ANNOTATIONS, null);

        // When
        String stringRepresentation = parameter.toString();

        // Then
        assertTrue("String representation should include parameter index", 
                  stringRepresentation.contains("arg" + parameterIndex));
    }

    // === Hash Code Tests ===

    @Test
    public void hashCode_doesNotThrowException() {
        // Given
        Parameter parameter = createBasicParameter();

        // When & Then - Should not throw any exception
        parameter.hashCode();
    }
}