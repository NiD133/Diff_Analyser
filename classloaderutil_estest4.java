package org.apache.commons.jxpath.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link ClassLoaderUtil} class.
 */
public class ClassLoaderUtilTest {

    /**
     * Verifies that {@link ClassLoaderUtil#getClass(String, boolean)}
     * can correctly resolve the Class object for a primitive type name.
     */
    @Test
    public void getClassShouldResolvePrimitiveTypeName() throws ClassNotFoundException {
        // Arrange: Define the name of a primitive type.
        String primitiveName = "long";

        // Act: Call the method under test to get the class.
        Class<?> resolvedClass = ClassLoaderUtil.getClass(primitiveName, true);

        // Assert: The returned class should be exactly the primitive long class.
        assertEquals(long.class, resolvedClass);
    }
}