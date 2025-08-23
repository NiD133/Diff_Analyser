package org.apache.commons.lang3.reflect;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ConstructorUtils#getMatchingAccessibleConstructor(Class, Class...)}.
 */
class ConstructorUtilsTest {

    /**
     * Tests that getMatchingAccessibleConstructor treats a null parameter type as a
     * wildcard that matches any reference type. For a single null parameter, this
     * should resolve to a constructor taking a single Object.
     */
    @Test
    void getMatchingAccessibleConstructor_withNullParameterType_shouldMatchObjectConstructor() {
        // Arrange
        // The MutableObject class has a constructor `public MutableObject(T value)`,
        // which due to type erasure, becomes `public MutableObject(Object value)`.
        final Class<MutableObject> classToInspect = MutableObject.class;
        final Class<?>[] requestedArgTypes = { null };
        final Class<?>[] expectedParameterTypes = { Object.class };

        // Act
        final Constructor<MutableObject> foundConstructor =
                ConstructorUtils.getMatchingAccessibleConstructor(classToInspect, requestedArgTypes);

        // Assert
        assertNotNull(foundConstructor, "A constructor should be found when using a null parameter type.");
        assertArrayEquals(expectedParameterTypes, foundConstructor.getParameterTypes(),
                "The constructor found should be the one that accepts a single Object.");
    }
}