package org.apache.commons.lang3.reflect;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang3.reflect.ConstructorUtils;

/**
 * Test suite for ConstructorUtils class functionality.
 * Tests constructor invocation, accessibility checks, and error handling.
 */
public class ConstructorUtilsTest {

    // ========== Successful Constructor Invocation Tests ==========

    @Test
    public void invokeExactConstructor_WithEmptyArgs_ShouldCreateObjectInstance() throws Exception {
        // Given: Object class with no constructor arguments
        Object[] emptyArgs = new Object[0];
        
        // When: invoking exact constructor with empty args
        Object result = ConstructorUtils.invokeExactConstructor(Object.class, emptyArgs, (Class<?>[]) null);
        
        // Then: should create a valid Object instance
        assertNotNull("Should create a non-null Object instance", result);
        assertTrue("Should be instance of Object", result instanceof Object);
    }

    @Test
    public void invokeConstructor_WithEmptyArgsAndTypes_ShouldCreateObjectInstance() throws Exception {
        // Given: Object class with empty argument arrays
        Object[] emptyArgs = new Object[0];
        Class<?>[] emptyTypes = new Class<?>[0];
        
        // When: invoking constructor with empty args and types
        Object result = ConstructorUtils.invokeConstructor(Object.class, emptyArgs, emptyTypes);
        
        // Then: should create a valid Object instance
        assertNotNull("Should create a non-null Object instance", result);
    }

    @Test
    public void invokeConstructor_WithEmptyArgs_ShouldCreateObjectInstance() throws Exception {
        // Given: Object class with empty Class array as arguments
        Class<?>[] emptyClassArray = new Class<?>[0];
        
        // When: invoking constructor with empty class array
        Object result = ConstructorUtils.invokeConstructor(Object.class, (Object[]) emptyClassArray);
        
        // Then: should create a valid Object instance
        assertNotNull("Should create a non-null Object instance", result);
    }

    // ========== Constructor Accessibility Tests ==========

    @Test
    public void getAccessibleConstructor_WithValidConstructor_ShouldReturnSameConstructor() throws Exception {
        // Given: a valid accessible constructor for Object class
        Class<?>[] emptyTypes = new Class<?>[0];
        Constructor<Object> originalConstructor = ConstructorUtils.getAccessibleConstructor(Object.class, emptyTypes);
        
        // When: checking accessibility of the constructor
        Constructor<Object> accessibleConstructor = ConstructorUtils.getAccessibleConstructor(originalConstructor);
        
        // Then: should return the same constructor
        assertSame("Should return the same constructor instance", originalConstructor, accessibleConstructor);
    }

    @Test
    public void getMatchingAccessibleConstructor_WithStringParameter_ShouldFindIntegerConstructor() throws Exception {
        // Given: Integer class which has a constructor that accepts String
        Class<?>[] stringParameterType = new Class<?>[]{null}; // Simulating a single parameter
        
        // When: finding matching accessible constructor
        Constructor<Integer> constructor = ConstructorUtils.getMatchingAccessibleConstructor(Integer.class, stringParameterType);
        
        // Then: should find the String constructor for Integer
        assertNotNull("Should find a matching constructor", constructor);
        assertEquals("Should be the String constructor", 
                    "public java.lang.Integer(java.lang.String) throws java.lang.NumberFormatException", 
                    constructor.toString());
    }

    @Test
    public void getMatchingAccessibleConstructor_WithIncompatibleParameters_ShouldReturnNull() throws Exception {
        // Given: Object class with incompatible parameter type
        Class<?>[] incompatibleTypes = new Class<?>[]{Object.class};
        
        // When: trying to find matching constructor
        Constructor<Object> constructor = ConstructorUtils.getMatchingAccessibleConstructor(Object.class, incompatibleTypes);
        
        // Then: should return null as no matching constructor exists
        assertNull("Should return null for incompatible parameters", constructor);
    }

    // ========== Null Parameter Validation Tests ==========

    @Test(expected = NullPointerException.class)
    public void invokeExactConstructor_WithNullClass_ShouldThrowNullPointerException() throws Exception {
        // Given: null class parameter
        Class<?>[] someArgs = new Class<?>[3];
        
        // When: invoking constructor with null class
        // Then: should throw NullPointerException
        ConstructorUtils.invokeExactConstructor(null, (Object[]) someArgs, someArgs);
    }

    @Test(expected = NullPointerException.class)
    public void invokeExactConstructor_WithNullClassAndNullArgs_ShouldThrowNullPointerException() throws Exception {
        // When: invoking constructor with null class and null args
        // Then: should throw NullPointerException
        ConstructorUtils.invokeExactConstructor(null, (Object[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void invokeConstructor_WithNullClass_ShouldThrowNullPointerException() throws Exception {
        // Given: null class with valid arguments
        Class<?>[] someArgs = new Class<?>[5];
        
        // When: invoking constructor with null class
        // Then: should throw NullPointerException
        ConstructorUtils.invokeConstructor(null, (Object[]) someArgs, someArgs);
    }

    @Test(expected = NullPointerException.class)
    public void getAccessibleConstructor_WithNullConstructor_ShouldThrowNullPointerException() throws Exception {
        // When: checking accessibility of null constructor
        // Then: should throw NullPointerException
        ConstructorUtils.getAccessibleConstructor((Constructor<Integer>) null);
    }

    @Test(expected = NullPointerException.class)
    public void getAccessibleConstructor_WithNullClass_ShouldThrowNullPointerException() throws Exception {
        // Given: null class with parameter types
        Class<?>[] paramTypes = new Class<?>[1];
        
        // When: getting accessible constructor with null class
        // Then: should throw NullPointerException
        ConstructorUtils.getAccessibleConstructor(null, paramTypes);
    }

    @Test(expected = NullPointerException.class)
    public void getMatchingAccessibleConstructor_WithNullClass_ShouldThrowNullPointerException() throws Exception {
        // Given: null class with parameter types
        Class<?>[] paramTypes = new Class<?>[1];
        
        // When: getting matching constructor with null class
        // Then: should throw NullPointerException
        ConstructorUtils.getMatchingAccessibleConstructor(null, paramTypes);
    }

    // ========== Error Handling Tests ==========

    @Test(expected = IllegalArgumentException.class)
    public void invokeConstructor_WithMismatchedArgsAndTypes_ShouldThrowIllegalArgumentException() throws Exception {
        // Given: mismatched arguments and parameter types
        Class<?>[] oneParameterType = new Class<?>[1];
        
        // When: invoking constructor with null args but non-empty parameter types
        // Then: should throw IllegalArgumentException
        ConstructorUtils.invokeConstructor(Integer.class, null, oneParameterType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeExactConstructor_WithMismatchedArgsAndTypes_ShouldThrowIllegalArgumentException() throws Exception {
        // Given: mismatched number of arguments and parameter types
        Object[] manyArgs = new Object[19];
        Class<?>[] fewTypes = new Class<?>[0];
        
        // When: invoking exact constructor with mismatched args/types
        // Then: should throw IllegalArgumentException
        ConstructorUtils.invokeExactConstructor(Object.class, manyArgs, fewTypes);
    }

    @Test(expected = NoSuchMethodException.class)
    public void invokeConstructor_WithNonExistentConstructor_ShouldThrowNoSuchMethodException() throws Exception {
        // Given: Object class with incompatible argument (Object doesn't have single-arg constructor)
        Object[] incompatibleArgs = new Object[1];
        
        // When: trying to invoke non-existent constructor
        // Then: should throw NoSuchMethodException
        ConstructorUtils.invokeConstructor(Object.class, incompatibleArgs);
    }

    @Test(expected = NoSuchMethodException.class)
    public void invokeExactConstructor_WithNonExistentConstructor_ShouldThrowNoSuchMethodException() throws Exception {
        // Given: Integer class with too many arguments (no such constructor exists)
        Object[] tooManyArgs = new Object[9];
        Class<?>[] correspondingTypes = new Class<?>[9];
        
        // When: trying to invoke non-existent exact constructor
        // Then: should throw NoSuchMethodException
        ConstructorUtils.invokeExactConstructor(Integer.class, tooManyArgs, correspondingTypes);
    }

    @Test(expected = InvocationTargetException.class)
    public void invokeConstructor_WithInvalidArgument_ShouldThrowInvocationTargetException() throws Exception {
        // Given: Integer constructor with invalid argument (null Class instead of String)
        Object[] invalidArgs = new Object[]{null};
        Class<?>[] paramTypes = new Class<?>[]{null};
        
        // When: invoking constructor with invalid argument
        // Then: should throw InvocationTargetException (wrapping NumberFormatException)
        ConstructorUtils.invokeConstructor(Integer.class, invalidArgs, paramTypes);
    }

    // ========== Utility Tests ==========

    @Test
    public void constructorUtils_ShouldBeInstantiable() {
        // When: creating ConstructorUtils instance
        ConstructorUtils utils = new ConstructorUtils();
        
        // Then: should create instance successfully
        assertNotNull("ConstructorUtils should be instantiable", utils);
    }

    @Test
    public void invokeExactConstructor_WithDynamicArgs_ShouldCreateObjectInstance() throws Exception {
        // Given: dynamically created empty arguments from constructor exception types
        Constructor<Object> constructor = ConstructorUtils.getAccessibleConstructor(Object.class, new Class<?>[0]);
        Class<?>[] exceptionTypes = constructor.getExceptionTypes();
        
        // When: using exception types array as arguments (which will be empty for Object constructor)
        Object result = ConstructorUtils.invokeExactConstructor(Object.class, (Object[]) exceptionTypes);
        
        // Then: should create valid Object instance
        assertNotNull("Should create Object instance with dynamic args", result);
    }

    @Test
    public void getAccessibleConstructor_WithIncompatibleParameterTypes_ShouldReturnNull() throws Exception {
        // Given: Object class with incompatible parameter type
        Class<?>[] incompatibleTypes = new Class<?>[]{Integer.class};
        
        // When: trying to get accessible constructor with incompatible types
        Constructor<Object> constructor = ConstructorUtils.getAccessibleConstructor(Object.class, incompatibleTypes);
        
        // Then: should return null
        assertNull("Should return null for incompatible parameter types", constructor);
    }
}