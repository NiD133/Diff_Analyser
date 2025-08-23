package org.apache.commons.jxpath.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for ClassLoaderUtil.getClass.
 * These tests cover:
 * - Null input handling
 * - Primitive and array type resolution
 * - Invalid class names resulting in ClassNotFoundException
 */
public class ClassLoaderUtilTest {

    @Test
    public void getClass_nullName_throwsNullPointerException() {
        NullPointerException npe = assertThrows(
            NullPointerException.class,
            () -> ClassLoaderUtil.getClass(null, false)
        );
        // Implementation uses Objects.requireNonNull with the parameter name as the message.
        assertEquals("className", npe.getMessage());
    }

    @Test
    public void getClass_primitiveArrayByName_returnsArrayClass() throws ClassNotFoundException {
        Class<?> clazz = ClassLoaderUtil.getClass("short[]", false);

        assertTrue("Expected an array class", clazz.isArray());
        assertSame("Expected component type short", short.class, clazz.getComponentType());
    }

    @Test
    public void getClass_nonExistingClassName_throwsClassNotFoundException() {
        assertThrows(
            ClassNotFoundException.class,
            () -> ClassLoaderUtil.getClass("UQR~q#m;I=WyC", true)
        );
    }

    @Test
    public void getClass_primitiveByName_returnsPrimitiveClass() throws ClassNotFoundException {
        Class<?> clazz = ClassLoaderUtil.getClass("long", true);

        assertSame("Expected primitive long", long.class, clazz);
        assertFalse("Primitive types are not annotations", clazz.isAnnotation());
    }

    @Test
    public void getClass_malformedArrayClassName_throwsClassNotFoundException() {
        assertThrows(
            ClassNotFoundException.class,
            () -> ClassLoaderUtil.getClass("9[]", false)
        );
    }
}