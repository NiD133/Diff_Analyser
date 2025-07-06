package org.mockito;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

/**
 * Test suite for ArgumentCaptor class.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class ArgumentCaptorTest {

    /**
     * Test that newly created ArgumentCaptor returns null when capture() is called.
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testCaptureReturnsNull() throws Throwable {
        // Arrange
        Class<Object> clazz = Object.class;
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(clazz);

        // Act
        Object capturedValue = argumentCaptor.capture();

        // Assert
        assertNull(capturedValue);
    }

    /**
     * Test that ArgumentCaptor.forClass() throws NullPointerException when null class is passed.
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testForClassThrowsNullPointerException() throws Throwable {
        // Act and Assert
        try {
            ArgumentCaptor.forClass((Class<Object>) null);
            fail("Expecting NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    /**
     * Test that ArgumentCaptor.captor() throws IllegalArgumentException when array is passed.
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testCaptorThrowsIllegalArgumentExceptionForObjectArray() throws Throwable {
        // Act and Assert
        try {
            ArgumentCaptor.captor(new Object[4]);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test that ArgumentCaptor.captor() throws IllegalArgumentException when null array is passed.
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testCaptorThrowsIllegalArgumentExceptionForNullArray() throws Throwable {
        // Act and Assert
        try {
            ArgumentCaptor.captor((Integer[]) null);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test that newly created ArgumentCaptor returns empty list when getAllValues() is called.
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testGetAllValuesReturnsEmptyList() throws Throwable {
        // Arrange
        Class<Object> clazz = Object.class;
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(clazz);

        // Act
        List<Object> allValues = argumentCaptor.getAllValues();

        // Assert
        assertTrue(allValues.isEmpty());
    }

    /**
     * Test that ArgumentCaptor returns correct captor type.
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testGetCaptorType() throws Throwable {
        // Arrange
        Class<Integer> clazz = Integer.class;
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(clazz);

        // Act
        Class<? extends Integer> captorType = argumentCaptor.getCaptorType();

        // Assert
        assertFalse(captorType.isAnnotation());
    }

    /**
     * Test that ArgumentCaptor captures default value when array is empty.
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testCaptureDefaultValue() throws Throwable {
        // Arrange
        Integer[] integerArray = new Integer[0];
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.captor(integerArray);

        // Act
        Integer capturedValue = argumentCaptor.capture();

        // Assert
        assertEquals(0, (int) capturedValue);
    }

    /**
     * Test that ArgumentCaptor.getValue() throws RuntimeException when no values are captured.
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testGetValueThrowsRuntimeException() throws Throwable {
        // Arrange
        Integer[] integerArray = new Integer[0];
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.captor(integerArray);

        // Act and Assert
        try {
            argumentCaptor.getValue();
            fail("Expecting RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }
}