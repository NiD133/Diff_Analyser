package com.google.common.reflect;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.Parameter;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the Parameter class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Parameter_ESTest extends Parameter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testParameterEqualityWithDifferentInvokables() {
        Invokable<Object, Annotation> invokableMock = mock(Invokable.class, new ViolatedAssumptionAnswer());
        TypeToken<Annotation> typeToken = TypeToken.of(Annotation.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter1 = new Parameter(invokableMock, 2473, typeToken, annotations, new Object());
        Parameter parameter2 = new Parameter(null, 1021, typeToken, annotations, parameter1);
        
        boolean areEqual = parameter1.equals(parameter2);
        assertFalse(areEqual);
    }

    @Test(timeout = 4000)
    public void testGetDeclaredAnnotationsByTypeReturnsDifferentArray() {
        TypeToken<Object> typeToken = TypeToken.of(Object.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(null, 2844, typeToken, annotations, null);
        Annotation[] declaredAnnotations = parameter.getDeclaredAnnotationsByType(Annotation.class);
        
        assertNotSame(annotations, declaredAnnotations);
    }

    @Test(timeout = 4000)
    public void testIsAnnotationPresentThrowsNullPointerException() {
        Annotation[] annotations = new Annotation[0];
        Parameter parameter = new Parameter(null, 1296, null, annotations, null);
        
        try {
            parameter.isAnnotationPresent(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetDeclaredAnnotationThrowsNullPointerException() {
        TypeToken<Object> typeToken = TypeToken.of(Object.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(null, 2844, typeToken, annotations, null);
        
        try {
            parameter.getDeclaredAnnotation(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAnnotationsByTypeThrowsNullPointerException() {
        Annotation[] annotations = new Annotation[0];
        Parameter parameter = new Parameter(null, -2148, null, annotations, null);
        
        try {
            parameter.getAnnotationsByType(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAnnotationThrowsNullPointerException() {
        Invokable<Object, Annotation> invokableMock = mock(Invokable.class, new ViolatedAssumptionAnswer());
        TypeToken<Annotation> typeToken = TypeToken.of(Annotation.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(invokableMock, 0, typeToken, annotations, invokableMock);
        
        try {
            parameter.getAnnotation(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAnnotatedTypeThrowsClassCastException() {
        Annotation[] annotations = new Annotation[0];
        Object object = new Object();
        
        Parameter parameter = new Parameter(null, -948, null, annotations, object);
        
        try {
            parameter.getAnnotatedType();
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("com.google.common.reflect.Parameter", e);
        }
    }

    @Test(timeout = 4000)
    public void testParameterConstructorThrowsNullPointerException() {
        try {
            new Parameter(null, 14, null, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.collect.ImmutableList", e);
        }
    }

    @Test(timeout = 4000)
    public void testParameterConstructorWithNonEmptyAnnotationsThrowsNullPointerException() {
        Annotation[] annotations = new Annotation[1];
        
        try {
            new Parameter(null, -3591, null, annotations, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAnnotationReturnsNull() {
        TypeToken<Object> typeToken = TypeToken.of(Object.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(null, 2844, typeToken, annotations, null);
        Annotation annotation = parameter.getAnnotation(Annotation.class);
        
        assertNull(annotation);
    }

    @Test(timeout = 4000)
    public void testGetDeclaredAnnotationsReturnsEmptyArray() {
        TypeToken<Object> typeToken = TypeToken.of(Object.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(null, 2844, typeToken, annotations, null);
        Annotation[] declaredAnnotations = parameter.getDeclaredAnnotations();
        
        assertEquals(0, declaredAnnotations.length);
    }

    @Test(timeout = 4000)
    public void testParameterEqualityWithSelf() {
        Invokable<Object, Annotation> invokableMock = mock(Invokable.class, new ViolatedAssumptionAnswer());
        TypeToken<Annotation> typeToken = TypeToken.of(Annotation.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(invokableMock, 1563, typeToken, annotations, Annotation.class);
        boolean areEqual = parameter.equals(parameter);
        
        assertTrue(areEqual);
    }

    @Test(timeout = 4000)
    public void testParameterEqualityWithDifferentInvokablesAndSameAttributes() {
        Invokable<Object, Annotation> invokableMock1 = mock(Invokable.class, new ViolatedAssumptionAnswer());
        Invokable<Parameter, Annotation> invokableMock2 = mock(Invokable.class, new ViolatedAssumptionAnswer());
        TypeToken<Annotation> typeToken = TypeToken.of(Annotation.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter1 = new Parameter(invokableMock1, 1563, typeToken, annotations, Annotation.class);
        Parameter parameter2 = new Parameter(invokableMock2, 1563, typeToken, annotations, Annotation.class);
        
        boolean areEqual = parameter1.equals(parameter2);
        assertFalse(areEqual);
    }

    @Test(timeout = 4000)
    public void testParameterEqualityWithDifferentPositions() {
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter1 = new Parameter(null, -3558, null, annotations, null);
        Parameter parameter2 = new Parameter(null, 1, null, annotations, parameter1);
        
        boolean areEqual = parameter1.equals(parameter2);
        assertFalse(areEqual);
    }

    @Test(timeout = 4000)
    public void testParameterEqualityWithNullObject() {
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(null, -3591, null, annotations, null);
        boolean areEqual = parameter.equals(null);
        
        assertFalse(areEqual);
    }

    @Test(timeout = 4000)
    public void testIsAnnotationPresentReturnsFalse() {
        Invokable<Object, Annotation> invokableMock = mock(Invokable.class, new ViolatedAssumptionAnswer());
        TypeToken<Annotation> typeToken = TypeToken.of(Annotation.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(invokableMock, 0, typeToken, annotations, invokableMock);
        boolean isPresent = parameter.isAnnotationPresent(Annotation.class);
        
        assertFalse(isPresent);
    }

    @Test(timeout = 4000)
    public void testGetDeclaringInvokableReturnsNull() {
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(null, -3591, null, annotations, null);
        Invokable<?, ?> declaringInvokable = parameter.getDeclaringInvokable();
        
        assertNull(declaringInvokable);
    }

    @Test(timeout = 4000)
    public void testGetAnnotationsReturnsEmptyArray() {
        Invokable<Object, Annotation> invokableMock = mock(Invokable.class, new ViolatedAssumptionAnswer());
        TypeToken<Annotation> typeToken = TypeToken.of(Annotation.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(invokableMock, 0, typeToken, annotations, invokableMock);
        Annotation[] allAnnotations = parameter.getAnnotations();
        
        assertEquals(0, allAnnotations.length);
    }

    @Test(timeout = 4000)
    public void testGetAnnotatedTypeThrowsNullPointerException() {
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(null, -3591, null, annotations, null);
        
        try {
            parameter.getAnnotatedType();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetTypeReturnsNull() {
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(null, -3591, null, annotations, null);
        TypeToken<?> typeToken = parameter.getType();
        
        assertNull(typeToken);
    }

    @Test(timeout = 4000)
    public void testToStringReturnsCorrectFormat() {
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(null, 11, null, annotations, null);
        String parameterString = parameter.toString();
        
        assertEquals("null arg11", parameterString);
    }

    @Test(timeout = 4000)
    public void testGetDeclaredAnnotationReturnsNull() {
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(null, -3591, null, annotations, null);
        Annotation declaredAnnotation = parameter.getDeclaredAnnotation(Annotation.class);
        
        assertNull(declaredAnnotation);
    }

    @Test(timeout = 4000)
    public void testHashCodeDoesNotThrowException() {
        Invokable<Object, Annotation> invokableMock = mock(Invokable.class, new ViolatedAssumptionAnswer());
        TypeToken<Object> typeToken = TypeToken.of(Object.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(invokableMock, 8, typeToken, annotations, Object.class);
        parameter.hashCode();
    }

    @Test(timeout = 4000)
    public void testGetAnnotationsByTypeReturnsDifferentArray() {
        Invokable<Object, Annotation> invokableMock = mock(Invokable.class, new ViolatedAssumptionAnswer());
        TypeToken<Annotation> typeToken = TypeToken.of(Annotation.class);
        Annotation[] annotations = new Annotation[0];
        
        Parameter parameter = new Parameter(invokableMock, 0, typeToken, annotations, invokableMock);
        Annotation[] annotationsByType = parameter.getAnnotationsByType(Annotation.class);
        
        assertNotSame(annotations, annotationsByType);
    }
}