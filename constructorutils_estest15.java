package org.apache.commons.lang3.reflect;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.InstantiationException;

/**
 * This test class contains tests for {@link ConstructorUtils}.
 * The original test class name and inheritance are kept as per the problem description.
 */
public class ConstructorUtils_ESTestTest15 extends ConstructorUtils_ESTest_scaffolding {

    /**
     * Tests that {@link ConstructorUtils#invokeExactConstructor(Class, Object[], Class[])}
     * throws an IllegalArgumentException when the number of arguments provided does not
     * match the number of parameter types specified.
     */
    @Test(expected = IllegalArgumentException.class)
    public void invokeExactConstructorShouldThrowExceptionForMismatchedArgumentAndParameterCounts()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Arrange: Define the class to instantiate and create arguments and parameter
        // types with different lengths. The method's contract states this is an illegal state.
        final Class<Object> classToInstantiate = Object.class;
        final Object[] constructorArgs = new Object[19];
        final Class<?>[] parameterTypes = new Class<?>[0]; // An empty array

        // Act & Assert: Call the method and expect an IllegalArgumentException.
        // The assertion is handled declaratively by the `expected` attribute of the @Test annotation.
        ConstructorUtils.invokeExactConstructor(classToInstantiate, constructorArgs, parameterTypes);
    }
}