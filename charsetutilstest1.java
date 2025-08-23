package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Note: The test class was renamed from CharSetUtilsTestTest1 to the conventional CharSetUtilsTest.
// The unused base class 'AbstractLangTest' was also removed for a more focused example.
public class CharSetUtilsTest {

    @Test
    @DisplayName("CharSetUtils should be a public, non-final, and instantiable utility class")
    void testClassDesignAndInstantiability() {
        // 1. Verify class modifiers
        assertTrue(Modifier.isPublic(CharSetUtils.class.getModifiers()), "Class must be public.");
        assertFalse(Modifier.isFinal(CharSetUtils.class.getModifiers()), "Class must not be final.");

        // 2. Verify constructor properties
        // A public constructor is provided for tools that require a JavaBean instance,
        // even though this is a utility class with static methods.
        final Constructor<?>[] constructors = CharSetUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length, "Should have exactly one constructor.");

        final Constructor<?> constructor = constructors[0];
        assertTrue(Modifier.isPublic(constructor.getModifiers()), "The constructor must be public.");
        assertEquals(0, constructor.getParameterCount(), "The constructor must be a no-arg constructor.");

        // 3. Verify that the class can be instantiated
        assertNotNull(new CharSetUtils(), "Instantiation should be possible via the public constructor.");
    }
}