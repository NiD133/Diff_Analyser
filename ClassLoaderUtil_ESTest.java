package org.apache.commons.jxpath.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ClassLoaderUtil}.
 *
 * This test suite verifies the functionality of the {@code getClass} method,
 * ensuring it can correctly load various types of classes by name and handle
 * invalid inputs gracefully.
 */
public class ClassLoaderUtilTest {

    @Test
    public void getClass_withPrimitiveName_returnsPrimitiveClass() throws ClassNotFoundException {
        // When loading a class by its primitive name ("long")
        Class<?> loadedClass = ClassLoaderUtil.getClass("long", true);

        // Then the corresponding primitive class should be returned
        assertEquals(long.class, loadedClass);
    }

    @Test
    public void getClass_withPrimitiveArrayName_returnsArrayClass() throws ClassNotFoundException {
        // When loading a class using the primitive array syntax ("short[]")
        Class<?> loadedClass = ClassLoaderUtil.getClass("short[]", false);

        // Then the corresponding primitive array class should be returned
        assertEquals(short[].class, loadedClass);
    }

    @Test
    public void getClass_withObjectClassName_returnsObjectClass() throws ClassNotFoundException {
        // When loading a standard object class by its fully qualified name
        Class<?> loadedClass = ClassLoaderUtil.getClass("java.lang.String", false);

        // Then the correct class should be returned
        assertEquals(String.class, loadedClass);
    }

    @Test
    public void getClass_withObjectArrayClassName_returnsArrayClass() throws ClassNotFoundException {
        // When loading a class using the object array syntax ("java.lang.Object[]")
        Class<?> loadedClass = ClassLoaderUtil.getClass("java.lang.Object[]", false);

        // Then the corresponding object array class should be returned
        assertEquals(Object[].class, loadedClass);
    }

    @Test(expected = ClassNotFoundException.class)
    public void getClass_withNonExistentClassName_throwsClassNotFoundException() throws ClassNotFoundException {
        // When attempting to load a class that does not exist
        // Then a ClassNotFoundException should be thrown
        ClassLoaderUtil.getClass("org.apache.commons.jxpath.NonExistentClass", true);
    }

    @Test(expected = ClassNotFoundException.class)
    public void getClass_withMalformedArrayName_throwsClassNotFoundException() throws ClassNotFoundException {
        // When attempting to load a class with a malformed array name
        // Then a ClassNotFoundException should be thrown
        ClassLoaderUtil.getClass("Invalid[]", false);
    }

    @Test(expected = NullPointerException.class)
    public void getClass_withNullClassName_throwsNullPointerException() throws ClassNotFoundException {
        // When calling getClass with a null class name
        // Then a NullPointerException should be thrown
        // The ClassNotFoundException is declared because the method signature requires it,
        // but the NullPointerException is expected to occur first.
        ClassLoaderUtil.getClass(null, false);
    }
}