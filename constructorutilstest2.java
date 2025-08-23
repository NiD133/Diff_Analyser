package org.apache.commons.lang3.reflect;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link ConstructorUtils#getAccessibleConstructor(java.lang.reflect.Constructor)}
 * and {@link ConstructorUtils#getAccessibleConstructor(Class, Class...)}.
 */
class ConstructorUtilsTest {

    // Test fixture: A private static nested class for accessibility tests.
    private static class PrivateClass {
        // This constructor is public, but its declaring class is private.
        public PrivateClass() {
        }

        // Test fixture: A public inner class within a private class.
        public static class PublicInnerClass {
            public PublicInnerClass() {
            }
        }
    }

    @Nested
    @DisplayName("getAccessibleConstructor(Constructor<T>)")
    class GetAccessibleConstructorByConstructor {

        @Test
        void shouldReturnConstructorWhenItIsPubliclyAccessible() throws NoSuchMethodException {
            // Arrange
            final Constructor<Object> publicConstructor = Object.class.getConstructor();

            // Act
            final Constructor<Object> result = ConstructorUtils.getAccessibleConstructor(publicConstructor);

            // Assert
            assertNotNull(result, "A public constructor should be considered accessible.");
            assertEquals(publicConstructor, result);
        }

        @Test
        void shouldReturnNullWhenConstructorIsInaccessibleDueToPrivateClass() throws NoSuchMethodException {
            // Arrange
            final Constructor<PrivateClass> constructorInPrivateClass = PrivateClass.class.getConstructor();

            // Act
            final Constructor<PrivateClass> result = ConstructorUtils.getAccessibleConstructor(constructorInPrivateClass);

            // Assert
            assertNull(result, "A constructor in a private class should not be accessible.");
        }
    }

    @Nested
    @DisplayName("getAccessibleConstructor(Class<T>, Class<?>...)")
    class GetAccessibleConstructorByClass {

        @Test
        void shouldFindAndReturnPublicConstructor() {
            // Act
            final Constructor<String> result = ConstructorUtils.getAccessibleConstructor(String.class);

            // Assert
            assertNotNull(result, "Should find the public no-arg constructor of String.");
            assertEquals(0, result.getParameterCount());
        }

        @Test
        void shouldReturnNullForPublicInnerClassOfPrivateClass() {
            // Arrange: PublicInnerClass is public, but its enclosing class (PrivateClass) is private.
            final Class<PrivateClass.PublicInnerClass> classInPrivateClass = PrivateClass.PublicInnerClass.class;

            // Act
            final Constructor<PrivateClass.PublicInnerClass> result = ConstructorUtils.getAccessibleConstructor(classInPrivateClass);

            // Assert
            assertNull(result, "A constructor in a public inner class of a private class should not be accessible.");
        }

        @Test
        void shouldReturnNullForNonExistentConstructor() {
            // Act
            final Constructor<String> result = ConstructorUtils.getAccessibleConstructor(String.class, Integer.class);

            // Assert
            assertNull(result, "Should return null if no constructor matches the given parameter types.");
        }
    }
}