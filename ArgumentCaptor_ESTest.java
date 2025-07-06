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
 * Test suite for the ArgumentCaptor class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ArgumentCaptor_ESTest extends ArgumentCaptor_ESTest_scaffolding {

    /**
     * Test capturing an Object argument and verify it returns null.
     */
    @Test(timeout = 4000)
    public void testCaptureObjectReturnsNull() throws Throwable {
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        Object capturedObject = captor.capture();
        assertNull("Captured object should be null", capturedObject);
    }

    /**
     * Test ArgumentCaptor.forClass with a null class should throw NullPointerException.
     */
    @Test(timeout = 4000)
    public void testForClassWithNullThrowsException() throws Throwable {
        try {
            ArgumentCaptor.forClass((Class<Object>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    /**
     * Test ArgumentCaptor.captor with an array should throw IllegalArgumentException.
     */
    @Test(timeout = 4000)
    public void testCaptorWithArrayThrowsException() throws Throwable {
        Object[] objectArray = new Object[4];
        try {
            ArgumentCaptor.captor(objectArray);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.mockito.ArgumentCaptor", e);
        }
    }

    /**
     * Test ArgumentCaptor.captor with null should throw IllegalArgumentException.
     */
    @Test(timeout = 4000)
    public void testCaptorWithNullThrowsException() throws Throwable {
        try {
            ArgumentCaptor.captor((Integer[]) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.mockito.ArgumentCaptor", e);
        }
    }

    /**
     * Test capturing all values with an empty list.
     */
    @Test(timeout = 4000)
    public void testGetAllValuesReturnsEmptyList() throws Throwable {
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        List<Object> capturedValues = captor.getAllValues();
        assertTrue("Captured values list should be empty", capturedValues.isEmpty());
    }

    /**
     * Test getCaptorType returns the correct class type.
     */
    @Test(timeout = 4000)
    public void testGetCaptorTypeReturnsCorrectClass() throws Throwable {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        Class<? extends Integer> captorType = captor.getCaptorType();
        assertFalse("Captor type should not be an annotation", captorType.isAnnotation());
    }

    /**
     * Test capturing an Integer argument with an empty array.
     */
    @Test(timeout = 4000)
    public void testCaptureIntegerWithEmptyArray() throws Throwable {
        Integer[] integerArray = new Integer[0];
        ArgumentCaptor<Integer> captor = ArgumentCaptor.captor(integerArray);
        Integer capturedInteger = captor.capture();
        assertEquals("Captured integer should be 0", 0, (int) capturedInteger);
    }

    /**
     * Test getValue on an empty captor should throw RuntimeException.
     */
    @Test(timeout = 4000)
    public void testGetValueOnEmptyCaptorThrowsException() throws Throwable {
        Integer[] integerArray = new Integer[0];
        ArgumentCaptor<Integer> captor = ArgumentCaptor.captor(integerArray);
        try {
            captor.getValue();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }
}