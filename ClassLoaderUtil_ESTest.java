package org.apache.commons.jxpath.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.util.ClassLoaderUtil;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the ClassLoaderUtil class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ClassLoaderUtil_ESTest extends ClassLoaderUtil_ESTest_scaffolding {

    /**
     * Test that a NullPointerException is thrown when a null class name is provided.
     */
    @Test(timeout = 4000)
    public void testGetClassWithNullClassName() {
        try {
            ClassLoaderUtil.getClass((String) null, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify that the exception is thrown due to a null class name
            verifyException("java.util.Objects", e);
        }
    }

    /**
     * Test that the method correctly identifies an array class.
     */
    @Test(timeout = 4000)
    public void testGetClassWithArrayType() throws ClassNotFoundException {
        Class<Object> clazz = ClassLoaderUtil.getClass("short[]", false);
        assertTrue("Expected class to be an array type", clazz.isArray());
    }

    /**
     * Test that a ClassNotFoundException is thrown for an invalid class name.
     */
    @Test(timeout = 4000)
    public void testGetClassWithInvalidClassName() {
        try {
            ClassLoaderUtil.getClass("UQR~q#m;I=WyC", true);
            fail("Expecting exception: ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            // Expected exception for an invalid class name
        }
    }

    /**
     * Test that the method correctly identifies a non-annotation class.
     */
    @Test(timeout = 4000)
    public void testGetClassWithPrimitiveType() throws ClassNotFoundException {
        Class<String> clazz = ClassLoaderUtil.getClass("long", true);
        assertFalse("Expected class not to be an annotation type", clazz.isAnnotation());
    }

    /**
     * Test that a ClassNotFoundException is thrown for an invalid array class name.
     */
    @Test(timeout = 4000)
    public void testGetClassWithInvalidArrayClassName() {
        try {
            ClassLoaderUtil.getClass("9[]", false);
            fail("Expecting exception: ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            // Expected exception for an invalid array class name
        }
    }
}