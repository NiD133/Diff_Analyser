package org.joda.time.convert;

import junit.framework.TestCase;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Tests the design of the StringConverter class, ensuring it is correctly
 * implemented as a package-private singleton.
 *
 * This test uses reflection because it needs to verify non-public members
 * and modifiers, which are not accessible through the public API.
 */
public class StringConverterTest extends TestCase {

    /**
     * Verifies that StringConverter is a package-private singleton by checking
     * the modifiers of the class, its constructor, and its INSTANCE field.
     */
    public void testSingletonDesignIsEnforced() throws Exception {
        Class<?> cls = StringConverter.class;

        // 1. Verify the class is package-private.
        // This prevents instantiation or extension from other packages.
        assertFalse("Class must not be public", Modifier.isPublic(cls.getModifiers()));
        assertFalse("Class must not be protected", Modifier.isProtected(cls.getModifiers()));
        assertFalse("Class must not be private", Modifier.isPrivate(cls.getModifiers()));

        // 2. Verify there is a single, protected, no-arg constructor.
        // A protected constructor prevents instantiation except by subclasses or
        // classes within the same package.
        Constructor<?>[] constructors = cls.getDeclaredConstructors();
        assertEquals("There must be exactly one constructor", 1, constructors.length);

        Constructor<?> constructor = constructors[0];
        assertTrue("The constructor must be protected", Modifier.isProtected(constructor.getModifiers()));
        assertEquals("The constructor must not accept any arguments", 0, constructor.getParameterTypes().length);

        // 3. Verify the INSTANCE field is a package-private, static, and final field.
        Field instanceField = cls.getDeclaredField("INSTANCE");
        int modifiers = instanceField.getModifiers();

        // Check for package-private access to the singleton instance.
        assertFalse("INSTANCE field must not be public", Modifier.isPublic(modifiers));
        assertFalse("INSTANCE field must not be protected", Modifier.isProtected(modifiers));
        assertFalse("INSTANCE field must not be private", Modifier.isPrivate(modifiers));

        // Check that the instance is static and final, as expected for a singleton.
        assertTrue("INSTANCE field must be static", Modifier.isStatic(modifiers));
        assertTrue("INSTANCE field must be final", Modifier.isFinal(modifiers));
    }
}