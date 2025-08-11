package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ConstructorInstantiator_ESTest extends ConstructorInstantiator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testNewInstanceWithDuplicateObjects_ThrowsRuntimeException() throws Throwable {
        // Arrange
        Object[] objectArray = new Object[3];
        Object object = new Object();
        objectArray[0] = object;
        objectArray[1] = objectArray[0];
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(false, objectArray);
        Class<Object> targetClass = Object.class;

        // Act & Assert
        try {
            constructorInstantiator.newInstance(targetClass);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNewInstanceWithNullConstructorArgs_ThrowsNullPointerException() throws Throwable {
        // Arrange
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(false, (Object[]) null);
        Class<Object> targetClass = Object.class;

        // Act & Assert
        try {
            constructorInstantiator.newInstance(targetClass);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify exception details
            verifyException("org.mockito.internal.creation.instance.ConstructorInstantiator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNewInstanceWithClassAsConstructorArg_ThrowsRuntimeException() throws Throwable {
        // Arrange
        Class<Integer> targetClass = Integer.class;
        Object[] objectArray = new Object[1];
        objectArray[0] = (Object) targetClass;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(false, objectArray);

        // Act & Assert
        try {
            constructorInstantiator.newInstance(targetClass);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNewInstanceWithOuterClassInstance_ThrowsRuntimeException() throws Throwable {
        // Arrange
        Object[] objectArray = new Object[1];
        Class<Integer> targetClass = Integer.class;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(true, objectArray);

        // Act & Assert
        try {
            constructorInstantiator.newInstance(targetClass);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNewInstanceWithMultipleConstructorArgs_ThrowsRuntimeException() throws Throwable {
        // Arrange
        Object[] objectArray = new Object[7];
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(true, objectArray);
        Class<Object> targetClass = Object.class;

        // Act & Assert
        try {
            constructorInstantiator.newInstance(targetClass);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNewInstanceWithEmptyConstructorArgs_ThrowsRuntimeException() throws Throwable {
        // Arrange
        Object[] objectArray = new Object[0];
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(true, objectArray);
        Class<Integer> targetClass = Integer.class;

        // Act & Assert
        try {
            constructorInstantiator.newInstance(targetClass);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNewInstanceWithEmptyConstructorArgs_ReturnsNonNullObject() throws Throwable {
        // Arrange
        Object[] objectArray = new Object[0];
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(true, objectArray);
        Class<Object> targetClass = Object.class;

        // Act
        Object result = constructorInstantiator.newInstance(targetClass);

        // Assert
        assertNotNull(result);
    }
}