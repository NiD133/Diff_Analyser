package org.apache.commons.lang3.reflect;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class ConstructorUtils_ESTest extends ConstructorUtils_ESTest_scaffolding {

    // ========================================================================
    // POSITIVE TESTS: SUCCESSFUL CONSTRUCTOR INVOCATIONS
    // ========================================================================

    @Test(timeout = 4000)
    public void testInvokeExactConstructor_ObjectClassWithNoArgs_Success() throws Throwable {
        // Verify Object's no-arg constructor can be invoked
        Object instance = ConstructorUtils.invokeExactConstructor(Object.class, new Object[0], null);
        assertNotNull(instance);
    }

    @Test(timeout = 4000)
    public void testInvokeConstructor_ObjectClassWithEmptyArgsAndParamTypes_Success() throws Throwable {
        // Verify Object's constructor with empty args and param types
        Class<?>[] paramTypes = new Class<?>[0];
        Object instance = ConstructorUtils.invokeConstructor(Object.class, new Object[0], paramTypes);
        assertNotNull(instance);
    }

    @Test(timeout = 4000)
    public void testInvokeConstructor_ObjectClassWithNoArgs_Success() throws Throwable {
        // Verify Object's no-arg constructor using empty args array
        Object instance = ConstructorUtils.invokeConstructor(Object.class, new Object[0]);
        assertNotNull(instance);
    }

    @Test(timeout = 4000)
    public void testGetAccessibleConstructor_ObjectClassNoArgs_ThenSetAccessible() throws Throwable {
        // Verify accessible constructor remains accessible after configuration
        Class<?>[] noParams = new Class<?>[0];
        Constructor<Object> constructor = ConstructorUtils.getAccessibleConstructor(Object.class, noParams);
        
        // Set accessibility on array containing the constructor
        AccessibleObject[] objects = new AccessibleObject[8];
        java.util.Arrays.fill(objects, constructor);
        AccessibleObject.setAccessible(objects, true);
        
        Constructor<Object> accessibleConstructor = ConstructorUtils.getAccessibleConstructor(constructor);
        assertSame(constructor, accessibleConstructor);
    }

    @Test(timeout = 4000)
    public void testInvokeExactConstructor_UsingExceptionTypesAsArgs_Success() throws Throwable {
        // Verify constructor invocation using exception types as arguments
        Class<?>[] noParams = new Class<?>[0];
        Constructor<Object> constructor = ConstructorUtils.getAccessibleConstructor(Object.class, noParams);
        Class<?>[] exceptionTypes = constructor.getExceptionTypes();
        
        Object instance = ConstructorUtils.invokeExactConstructor(Object.class, exceptionTypes);
        assertNotNull(instance);
    }

    // ========================================================================
    // NEGATIVE TESTS: NULL INPUT CHECKS
    // ========================================================================

    @Test(timeout = 4000)
    public void testInvokeExactConstructor_NullClass_ThrowsNullPointerException() {
        // Null class should throw NullPointerException
        Class<?>[] params = new Class<?>[3];
        Object[] args = new Object[3];
        try {
            ConstructorUtils.invokeExactConstructor(null, args, params);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("cls", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testInvokeExactConstructor_NullClassWithNullArgs_ThrowsNullPointerException() {
        try {
            ConstructorUtils.invokeExactConstructor(null, (Object[]) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("cls", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructor_NullClass_ThrowsNullPointerException() {
        Class<?>[] params = new Class<?>[5];
        Object[] args = new Object[5];
        try {
            ConstructorUtils.invokeConstructor(null, args, params);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("cls", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructor_NullClassWithArgs_ThrowsNullPointerException() {
        Class<?>[] args = new Class<?>[7];
        try {
            ConstructorUtils.invokeConstructor(null, args);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("cls", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetMatchingAccessibleConstructor_NullClass_ThrowsNullPointerException() {
        Class<?>[] paramTypes = new Class<?>[1];
        try {
            ConstructorUtils.getMatchingAccessibleConstructor(null, paramTypes);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("cls", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetAccessibleConstructor_NullConstructor_ThrowsNullPointerException() {
        try {
            ConstructorUtils.getAccessibleConstructor((Constructor<?>) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("ctor", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetAccessibleConstructor_NullClassWithParamTypes_ThrowsNullPointerException() {
        Class<?>[] paramTypes = new Class<?>[1];
        try {
            ConstructorUtils.getAccessibleConstructor(null, paramTypes);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("cls", e.getMessage());
        }
    }

    // ========================================================================
    // NEGATIVE TESTS: INVALID INPUTS/EXPECTED EXCEPTIONS
    // ========================================================================

    @Test(timeout = 4000)
    public void testInvokeConstructor_NullArgsWithNonEmptyParamTypes_ThrowsIllegalArgumentException() {
        // Null args with non-empty param types should fail
        Class<?>[] paramTypes = new Class<?>[1];
        try {
            ConstructorUtils.invokeConstructor(Integer.class, null, paramTypes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException | NoSuchMethodException | IllegalAccessException | 
                 InvocationTargetException | InstantiationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructor_ObjectClassWithSingleArg_ThrowsNoSuchMethodException() {
        // Object doesn't have a single-arg constructor
        Class<?>[] args = new Class<?>[1];
        try {
            ConstructorUtils.invokeConstructor(Object.class, (Object[]) args);
            fail("Expected NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            assertEquals("No such accessible constructor on object: java.lang.Object", e.getMessage());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeExactConstructor_IntegerWithManyArgs_ThrowsNoSuchMethodException() {
        // Integer has no constructor matching 9 parameters
        Class<?>[] params = new Class<?>[9];
        Object[] args = new Object[9];
        try {
            ConstructorUtils.invokeExactConstructor(Integer.class, args, params);
            fail("Expected NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            assertEquals("No such accessible constructor on object: java.lang.Integer", e.getMessage());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeExactConstructor_ArgLengthMismatch_ThrowsIllegalArgumentException() {
        // Empty paramTypes but non-empty args should fail
        Class<?>[] emptyParamTypes = new Class<?>[0];
        Class<?>[] nonEmptyArgs = new Class<?>[19];
        try {
            ConstructorUtils.invokeExactConstructor(Object.class, nonEmptyArgs, emptyParamTypes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException | NoSuchMethodException | IllegalAccessException | 
                 InvocationTargetException | InstantiationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructor_IntegerWithSingleStringArg_Success() throws Throwable {
        // Integer has a constructor that takes a single String
        Class<?>[] paramTypes = new Class<?>[]{String.class};
        Object[] args = new Object[]{"123"};
        Integer instance = ConstructorUtils.invokeConstructor(Integer.class, args, paramTypes);
        assertEquals(123, instance.intValue());
    }

    @Test(timeout = 4000)
    public void testGetMatchingAccessibleConstructor_IntegerWithSingleParam_ReturnsStringConstructor() {
        // Should find Integer's String constructor
        Class<?>[] paramTypes = new Class<?>[1];
        Constructor<Integer> ctor = ConstructorUtils.getMatchingAccessibleConstructor(Integer.class, paramTypes);
        assertNotNull(ctor);
        assertTrue(ctor.toString().contains("public java.lang.Integer(java.lang.String)"));
    }

    @Test(timeout = 4000)
    public void testGetMatchingAccessibleConstructor_ObjectWithSingleParam_ReturnsNull() {
        // Object has no single-arg constructor
        Class<?>[] paramTypes = new Class<?>[1];
        Constructor<Object> ctor = ConstructorUtils.getMatchingAccessibleConstructor(Object.class, paramTypes);
        assertNull(ctor);
    }

    @Test(timeout = 4000)
    public void testGetAccessibleConstructor_ObjectClassNoArgs_ReturnsSameInstance() {
        // Should return the same constructor instance
        Class<?>[] noParams = new Class<?>[0];
        Constructor<Object> ctor = ConstructorUtils.getAccessibleConstructor(Object.class, noParams);
        Constructor<Object> accessibleCtor = ConstructorUtils.getAccessibleConstructor(ctor);
        assertSame(ctor, accessibleCtor);
    }

    @Test(timeout = 4000)
    public void testInvokeExactConstructor_ObjectClassWithSingleArg_ThrowsNoSuchMethodException() {
        // Object has no single-arg constructor
        Class<?>[] args = new Class<?>[1];
        try {
            ConstructorUtils.invokeExactConstructor(Object.class, (Object[]) args);
            fail("Expected NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            assertEquals("No such accessible constructor on object: java.lang.Object", e.getMessage());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructor_AnnotationClass_ThrowsNoSuchMethodException() {
        // Annotation interface cannot be instantiated
        Class<?>[] noParams = new Class<?>[0];
        Constructor<Object> ctor = ConstructorUtils.getAccessibleConstructor(Object.class, noParams);
        Annotation[] annotations = ctor.getAnnotations();
        Class<?>[] paramTypes = new Class<?>[4];
        try {
            ConstructorUtils.invokeConstructor(Annotation.class, (Object[]) annotations, paramTypes);
            fail("Expected NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            assertEquals("No such accessible constructor on object: java.lang.annotation.Annotation", e.getMessage());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test(timeout = 4000)
    public void testInvokeConstructor_IntegerWithInvalidString_ThrowsInvocationTargetException() {
        // Integer constructor throws NumberFormatException when parsing invalid string
        Class<?>[] paramTypes = new Class<?>[1];
        Object[] args = new Object[]{new Object()}; // Invalid argument
        try {
            ConstructorUtils.invokeConstructor(Integer.class, args, paramTypes);
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            // Expected - caused by NumberFormatException
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAccessibleConstructor_ObjectClassWithSingleParam_ReturnsNull() {
        // Object has no accessible single-arg constructor
        Class<?>[] paramTypes = new Class<?>[1];
        Constructor<Object> ctor = ConstructorUtils.getAccessibleConstructor(Object.class, paramTypes);
        assertNull(ctor);
    }
}