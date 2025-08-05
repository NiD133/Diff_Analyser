package org.apache.commons.lang3.reflect;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ConstructorUtils_ESTest extends ConstructorUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testInvokeExactConstructorWithNoArguments() throws Throwable {
        // Test invoking the exact constructor of Object class with no arguments
        Class<Object> objectClass = Object.class;
        Object[] emptyArgs = new Object[0];
        Object result = ConstructorUtils.invokeExactConstructor(objectClass, emptyArgs, (Class<?>[]) null);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testInvokeConstructorWithNoArguments() throws Throwable {
        // Test invoking a constructor of Object class with no arguments
        Class<Object> objectClass = Object.class;
        Object[] emptyArgs = new Object[0];
        Class<Integer>[] emptyParamTypes = (Class<Integer>[]) Array.newInstance(Class.class, 0);
        Object result = ConstructorUtils.invokeConstructor(objectClass, emptyArgs, (Class<?>[]) emptyParamTypes);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testInvokeConstructorWithNoParameterTypes() throws Throwable {
        // Test invoking a constructor of Object class with no parameter types
        Class<Object> objectClass = Object.class;
        Class<Object>[] emptyParamTypes = (Class<Object>[]) Array.newInstance(Class.class, 0);
        Object result = ConstructorUtils.invokeConstructor(objectClass, (Object[]) emptyParamTypes);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testGetAccessibleConstructor() throws Throwable {
        // Test getting an accessible constructor of Object class with no parameter types
        Class<Object> objectClass = Object.class;
        Class<Annotation>[] emptyParamTypes = (Class<Annotation>[]) Array.newInstance(Class.class, 0);
        Constructor<Object> constructor = ConstructorUtils.getAccessibleConstructor(objectClass, (Class<?>[]) emptyParamTypes);
        AccessibleObject[] accessibleObjects = new AccessibleObject[8];
        for (int i = 0; i < accessibleObjects.length; i++) {
            accessibleObjects[i] = constructor;
        }
        AccessibleObject.setAccessible(accessibleObjects, true);
        Constructor<Object> accessibleConstructor = ConstructorUtils.getAccessibleConstructor(constructor);
        assertSame(constructor, accessibleConstructor);
    }

    @Test(timeout = 4000)
    public void testInvokeExactConstructorWithNullClass() {
        // Test invoking an exact constructor with null class, expecting NullPointerException
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 3);
        try {
            ConstructorUtils.invokeExactConstructor(null, (Object[]) paramTypes, (Class<?>[]) paramTypes);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeExactConstructorWithNullArguments() {
        // Test invoking an exact constructor with null arguments, expecting NullPointerException
        try {
            ConstructorUtils.invokeExactConstructor(null, (Object[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructorWithNullClass() {
        // Test invoking a constructor with null class, expecting NullPointerException
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 5);
        try {
            ConstructorUtils.invokeConstructor(null, (Object[]) paramTypes, (Class<?>[]) paramTypes);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructorWithNullArguments() {
        // Test invoking a constructor with null arguments, expecting IllegalArgumentException
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        Class<Integer> integerClass = Integer.class;
        try {
            ConstructorUtils.invokeConstructor(integerClass, (Object[]) null, (Class<?>[]) paramTypes);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructorWithNullArgumentsAndClass() {
        // Test invoking a constructor with null arguments and class, expecting NullPointerException
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 7);
        try {
            ConstructorUtils.invokeConstructor(null, (Object[]) paramTypes);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructorWithIncompatibleTypes() {
        // Test invoking a constructor with incompatible parameter types, expecting NoSuchMethodException
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        Class<Object> objectClass = Object.class;
        try {
            ConstructorUtils.invokeConstructor(objectClass, (Object[]) paramTypes);
            fail("Expecting exception: NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            verifyException("org.apache.commons.lang3.reflect.ConstructorUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetMatchingAccessibleConstructorWithNullClass() {
        // Test getting a matching accessible constructor with null class, expecting NullPointerException
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        try {
            ConstructorUtils.getMatchingAccessibleConstructor(null, (Class<?>[]) paramTypes);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAccessibleConstructorWithNullConstructor() {
        // Test getting an accessible constructor with null constructor, expecting NullPointerException
        try {
            ConstructorUtils.getAccessibleConstructor((Constructor<Integer>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAccessibleConstructorWithNullClass() {
        // Test getting an accessible constructor with null class, expecting NullPointerException
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        try {
            ConstructorUtils.getAccessibleConstructor(null, (Class<?>[]) paramTypes);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeExactConstructorWithIncompatibleTypes() {
        // Test invoking an exact constructor with incompatible parameter types, expecting NoSuchMethodException
        Class<Object>[] paramTypes = (Class<Object>[]) Array.newInstance(Class.class, 9);
        Class<Integer> integerClass = Integer.class;
        try {
            ConstructorUtils.invokeExactConstructor(integerClass, (Object[]) paramTypes, (Class<?>[]) paramTypes);
            fail("Expecting exception: NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            verifyException("org.apache.commons.lang3.reflect.ConstructorUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeExactConstructorWithMismatchedParameterTypes() {
        // Test invoking an exact constructor with mismatched parameter types, expecting IllegalArgumentException
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 19);
        Class<Object> objectClass = Object.class;
        Class<Integer>[] emptyParamTypes = (Class<Integer>[]) Array.newInstance(Class.class, 0);
        try {
            ConstructorUtils.invokeExactConstructor(objectClass, (Object[]) paramTypes, (Class<?>[]) emptyParamTypes);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructorWithInvocationTargetException() {
        // Test invoking a constructor that throws InvocationTargetException
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        Class<Integer> integerClass = Integer.class;
        try {
            ConstructorUtils.invokeConstructor(integerClass, (Object[]) paramTypes, (Class<?>[]) paramTypes);
            fail("Expecting exception: InvocationTargetException");
        } catch (InvocationTargetException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetMatchingAccessibleConstructor() {
        // Test getting a matching accessible constructor for Integer class
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        Class<Integer> integerClass = Integer.class;
        Constructor<Integer> constructor = ConstructorUtils.getMatchingAccessibleConstructor(integerClass, (Class<?>[]) paramTypes);
        assertNotNull(constructor);
        assertEquals("public java.lang.Integer(java.lang.String) throws java.lang.NumberFormatException", constructor.toString());
    }

    @Test(timeout = 4000)
    public void testGetMatchingAccessibleConstructorWithNoMatch() {
        // Test getting a matching accessible constructor with no match, expecting null
        Class<Object> objectClass = Object.class;
        Class<Object>[] paramTypes = (Class<Object>[]) Array.newInstance(Class.class, 1);
        Constructor<Object> constructor = ConstructorUtils.getMatchingAccessibleConstructor(objectClass, (Class<?>[]) paramTypes);
        assertNull(constructor);
    }

    @Test(timeout = 4000)
    public void testGetAccessibleConstructorAndVerifySame() {
        // Test getting an accessible constructor and verifying it is the same
        Class<Object> objectClass = Object.class;
        Class<Annotation>[] emptyParamTypes = (Class<Annotation>[]) Array.newInstance(Class.class, 0);
        Constructor<Object> constructor = ConstructorUtils.getAccessibleConstructor(objectClass, (Class<?>[]) emptyParamTypes);
        Constructor<Object> accessibleConstructor = ConstructorUtils.getAccessibleConstructor(constructor);
        assertSame(constructor, accessibleConstructor);
    }

    @Test(timeout = 4000)
    public void testInvokeExactConstructorWithIncompatibleTypesAndNoMatch() {
        // Test invoking an exact constructor with incompatible types and no match, expecting NoSuchMethodException
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        Class<Object> objectClass = Object.class;
        try {
            ConstructorUtils.invokeExactConstructor(objectClass, (Object[]) paramTypes);
            fail("Expecting exception: NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            verifyException("org.apache.commons.lang3.reflect.ConstructorUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructorWithAnnotationClass() {
        // Test invoking a constructor with Annotation class, expecting NoSuchMethodException
        Class<Object> objectClass = Object.class;
        Class<Annotation>[] emptyParamTypes = (Class<Annotation>[]) Array.newInstance(Class.class, 0);
        Constructor<Object> constructor = ConstructorUtils.getAccessibleConstructor(objectClass, (Class<?>[]) emptyParamTypes);
        Annotation[] annotations = constructor.getAnnotations();
        Class<Annotation> annotationClass = Annotation.class;
        Class<Object>[] paramTypes = (Class<Object>[]) Array.newInstance(Class.class, 4);
        try {
            ConstructorUtils.invokeConstructor(annotationClass, (Object[]) annotations, (Class<?>[]) paramTypes);
            fail("Expecting exception: NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            verifyException("org.apache.commons.lang3.reflect.ConstructorUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructorWithInvocationTargetExceptionAndNoArgs() {
        // Test invoking a constructor that throws InvocationTargetException with no arguments
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        Class<Integer> integerClass = Integer.class;
        try {
            ConstructorUtils.invokeConstructor(integerClass, (Object[]) paramTypes);
            fail("Expecting exception: InvocationTargetException");
        } catch (InvocationTargetException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConstructorUtilsInstantiation() {
        // Test instantiation of ConstructorUtils (not recommended in standard programming)
        ConstructorUtils constructorUtils = new ConstructorUtils();
    }

    @Test(timeout = 4000)
    public void testInvokeExactConstructorWithExceptionTypes() throws Throwable {
        // Test invoking an exact constructor using exception types as arguments
        Class<Object> objectClass = Object.class;
        Class<Annotation>[] emptyParamTypes = (Class<Annotation>[]) Array.newInstance(Class.class, 0);
        Constructor<Object> constructor = ConstructorUtils.getAccessibleConstructor(objectClass, (Class<?>[]) emptyParamTypes);
        Class<?>[] exceptionTypes = constructor.getExceptionTypes();
        Object result = ConstructorUtils.invokeExactConstructor(objectClass, (Object[]) exceptionTypes);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testGetAccessibleConstructorWithNoMatch() {
        // Test getting an accessible constructor with no match, expecting null
        Class<Integer>[] paramTypes = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        Class<Object> objectClass = Object.class;
        Constructor<Object> constructor = ConstructorUtils.getAccessibleConstructor(objectClass, (Class<?>[]) paramTypes);
        assertNull(constructor);
    }
}