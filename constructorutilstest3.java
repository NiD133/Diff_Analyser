package org.apache.commons.lang3.reflect;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Constructor;
import org.apache.commons.lang3.AbstractLangTest;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ConstructorUtils#getAccessibleConstructor(Class, Class...)}.
 */
public class ConstructorUtilsGetAccessibleConstructorTest extends AbstractLangTest {

    /**
     * A private static nested class for testing accessibility.
     * Even though its constructor is public, the class itself is not,
     * so it should not be considered "accessible" by ConstructorUtils.
     */
    private static class PrivateClass {
        public PrivateClass() {
        }
    }

    @Test
    void getAccessibleConstructor_forPublicClass_shouldReturnConstructor() {
        // Arrange: Object is a public class with a public no-arg constructor.
        final Class<?>[] parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;

        // Act: Attempt to get the accessible constructor.
        final Constructor<Object> constructor = ConstructorUtils.getAccessibleConstructor(Object.class, parameterTypes);

        // Assert: The constructor should be found.
        assertNotNull(constructor, "Should have found the public constructor for Object");
    }

    @Test
    void getAccessibleConstructor_forNonPublicClass_shouldReturnNull() {
        // Arrange: PrivateClass is not public, so its constructors are not considered accessible.
        final Class<?>[] parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;

        // Act: Attempt to get the constructor for a non-public class.
        final Constructor<PrivateClass> constructor = ConstructorUtils.getAccessibleConstructor(PrivateClass.class, parameterTypes);

        // Assert: The constructor should not be found.
        assertNull(constructor, "Should not have found a constructor for a non-public class");
    }
}