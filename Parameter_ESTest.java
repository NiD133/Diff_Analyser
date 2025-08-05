package com.google.common.reflect;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.reflect.Invokable;
import com.google.common.reflect.Parameter;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link Parameter}.
 */
public class ParameterTest {

    @Retention(RetentionPolicy.RUNTIME)
    private @interface TestAnnotation {}

    private Invokable<?, ?> mockInvokable;
    private static final Annotation[] NO_ANNOTATIONS = new Annotation[0];

    @Before
    public void setUp() {
        mockInvokable = mock(Invokable.class);
        // Provide a helpful toString() for mock, improving test failure messages.
        when(mockInvokable.toString()).thenReturn("mockInvokable");
    }

    // --- Constructor Tests ---

    @Test(expected = NullPointerException.class)
    public void constructor_nullAnnotationsArray_throwsException() {
        new Parameter(mockInvokable, 0, TypeToken.of(String.class), null, null);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_annotationsArrayWithNullElement_throwsException() {
        new Parameter(mockInvokable, 0, TypeToken.of(String.class), new Annotation[]{null}, null);
    }

    // --- Getters ---

    @Test
    public void getDeclaringInvokable_returnsCorrectInvokable() {
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertSame(mockInvokable, parameter.getDeclaringInvokable());
    }

    @Test
    public void getType_returnsCorrectTypeToken() {
        TypeToken<String> stringToken = TypeToken.of(String.class);
        Parameter parameter = new Parameter(mockInvokable, 0, stringToken, NO_ANNOTATIONS, null);
        assertSame(stringToken, parameter.getType());
    }

    // --- Annotation Methods ---

    @Test
    public void isAnnotationPresent_whenPresent_returnsTrue() throws Exception {
        Annotation annotation = TestAnnotation.class.getConstructor().newInstance();
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), new Annotation[]{annotation}, null);
        assertTrue(parameter.isAnnotationPresent(TestAnnotation.class));
    }

    @Test
    public void isAnnotationPresent_whenNotPresent_returnsFalse() {
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertFalse(parameter.isAnnotationPresent(TestAnnotation.class));
    }

    @Test
    public void getAnnotation_whenPresent_returnsAnnotation() throws Exception {
        Annotation annotation = TestAnnotation.class.getConstructor().newInstance();
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), new Annotation[]{annotation}, null);
        assertNotNull(parameter.getAnnotation(TestAnnotation.class));
    }

    @Test
    public void getAnnotation_whenNotPresent_returnsNull() {
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertNull(parameter.getAnnotation(TestAnnotation.class));
    }

    @Test
    public void getAnnotations_returnsAllAnnotations() throws Exception {
        Annotation annotation = TestAnnotation.class.getConstructor().newInstance();
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), new Annotation[]{annotation}, null);
        assertEquals(1, parameter.getAnnotations().length);
        assertEquals(annotation, parameter.getAnnotations()[0]);
    }

    @Test
    public void getDeclaredAnnotations_whenNoAnnotations_returnsEmptyArray() {
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertEquals(0, parameter.getDeclaredAnnotations().length);
    }

    @Test
    public void getAnnotationsByType_whenNoAnnotations_returnsEmptyArray() {
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertEquals(0, parameter.getAnnotationsByType(TestAnnotation.class).length);
    }

    @Test(expected = NullPointerException.class)
    public void getAnnotation_withNullType_throwsException() {
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        parameter.getAnnotation(null);
    }

    // --- AnnotatedType Method ---

    @Test
    public void getAnnotatedType_returnsCorrectAnnotatedType() {
        AnnotatedType mockAnnotatedType = mock(AnnotatedType.class);
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, mockAnnotatedType);
        assertSame(mockAnnotatedType, parameter.getAnnotatedType());
    }

    @Test(expected = NullPointerException.class)
    public void getAnnotatedType_whenConstructedWithNull_throwsException() {
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        parameter.getAnnotatedType();
    }

    /**
     * This tests a specific implementation detail where the internal `annotatedType` field is an
     * Object to support older Android versions. A ClassCastException is expected if it's not a
     * valid AnnotatedType.
     */
    @Test(expected = ClassCastException.class)
    public void getAnnotatedType_whenConstructedWithNonAnnotatedTypeObject_throwsException() {
        Parameter parameter = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, new Object());
        parameter.getAnnotatedType();
    }

    // --- Equals and HashCode ---

    @Test
    public void equals_sameInstance_isTrue() {
        Parameter param = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertTrue(param.equals(param));
    }

    @Test
    public void equals_equalInstances_isTrue() {
        Parameter param1 = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        Parameter param2 = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertTrue(param1.equals(param2));
    }

    @Test
    public void equals_differentInvokable_isFalse() {
        Invokable<?, ?> anotherMockInvokable = mock(Invokable.class);
        Parameter param1 = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        Parameter param2 = new Parameter(anotherMockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertFalse(param1.equals(param2));
    }

    @Test
    public void equals_differentPosition_isFalse() {
        Parameter param1 = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        Parameter param2 = new Parameter(mockInvokable, 1, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertFalse(param1.equals(param2));
    }

    @Test
    public void equals_differentType_isFalse() {
        Parameter param1 = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        Parameter param2 = new Parameter(mockInvokable, 0, TypeToken.of(Integer.class), NO_ANNOTATIONS, null);
        assertFalse(param1.equals(param2));
    }

    @Test
    public void equals_nullObject_isFalse() {
        Parameter param = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertFalse(param.equals(null));
    }

    @Test
    public void equals_differentClass_isFalse() {
        Parameter param = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertFalse(param.equals("a string"));
    }

    @Test
    public void hashCode_forEqualInstances_isSame() {
        Parameter param1 = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        Parameter param2 = new Parameter(mockInvokable, 0, TypeToken.of(String.class), NO_ANNOTATIONS, null);
        assertEquals(param1.hashCode(), param2.hashCode());
    }

    // --- ToString ---

    @Test
    public void toString_returnsReadableRepresentation() {
        TypeToken<String> stringToken = TypeToken.of(String.class);
        Parameter parameter = new Parameter(mockInvokable, 1, stringToken, NO_ANNOTATIONS, null);
        assertEquals("java.lang.String arg1", parameter.toString());
    }

    @Test
    public void toString_withNullInvokable_handlesGracefully() {
        TypeToken<String> stringToken = TypeToken.of(String.class);
        Parameter parameter = new Parameter(null, 0, stringToken, NO_ANNOTATIONS, null);
        assertEquals("java.lang.String arg0", parameter.toString());
    }
}