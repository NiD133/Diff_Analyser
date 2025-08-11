/*
 * Test suite for ClassLoaderUtil - validates class loading functionality
 * including primitive types, arrays, and error handling scenarios.
 */

package org.apache.commons.jxpath.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.jxpath.util.ClassLoaderUtil;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class ClassLoaderUtil_ESTest extends ClassLoaderUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testGetClass_WithNullClassName_ThrowsNullPointerException() throws Throwable {
        // Given: A null class name
        String nullClassName = null;
        boolean initialize = false;
        
        // When & Then: Attempting to get class should throw NullPointerException
        try { 
            ClassLoaderUtil.getClass(nullClassName, initialize);
            fail("Expected NullPointerException when className is null");
        } catch(NullPointerException e) {
            // Verify the exception comes from null validation
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetClass_WithPrimitiveArrayType_ReturnsArrayClass() throws Throwable {
        // Given: A primitive array type name
        String primitiveArrayClassName = "short[]";
        boolean initialize = false;
        
        // When: Getting the class for primitive array
        Class<Object> resultClass = ClassLoaderUtil.getClass(primitiveArrayClassName, initialize);
        
        // Then: Should return an array class
        assertTrue("Expected class to be an array type", resultClass.isArray());
    }

    @Test(timeout = 4000)
    public void testGetClass_WithInvalidClassName_ThrowsClassNotFoundException() throws Throwable {
        // Given: An invalid/non-existent class name
        String invalidClassName = "UQR~q#m;I=WyC";
        boolean initialize = true;
        
        // When & Then: Attempting to get invalid class should throw ClassNotFoundException
        try { 
            ClassLoaderUtil.getClass(invalidClassName, initialize);
            fail("Expected ClassNotFoundException for invalid class name: " + invalidClassName);
        } catch(ClassNotFoundException e) {
            // Expected exception - test passes
        }
    }

    @Test(timeout = 4000)
    public void testGetClass_WithPrimitiveType_ReturnsValidClass() throws Throwable {
        // Given: A primitive type name
        String primitiveTypeName = "long";
        boolean initialize = true;
        
        // When: Getting the class for primitive type
        Class<String> resultClass = ClassLoaderUtil.getClass(primitiveTypeName, initialize);
        
        // Then: Should return a valid class that is not an annotation
        assertNotNull("Expected non-null class for primitive type", resultClass);
        assertFalse("Expected primitive class to not be an annotation", resultClass.isAnnotation());
    }

    @Test(timeout = 4000)
    public void testGetClass_WithMalformedArraySyntax_ThrowsClassNotFoundException() throws Throwable {
        // Given: A malformed array class name (invalid syntax)
        String malformedArrayClassName = "9[]";
        boolean initialize = false;
        
        // When & Then: Attempting to get class with malformed syntax should throw ClassNotFoundException
        try { 
            ClassLoaderUtil.getClass(malformedArrayClassName, initialize);
            fail("Expected ClassNotFoundException for malformed array syntax: " + malformedArrayClassName);
        } catch(ClassNotFoundException e) {
            // Expected exception - test passes
        }
    }
}