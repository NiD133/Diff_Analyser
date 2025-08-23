package org.apache.commons.lang3.reflect;

import org.junit.Test;
import java.lang.reflect.Constructor;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ConstructorUtils}.
 * This class focuses on testing the getAccessibleConstructor(Constructor) method.
 */
public class ConstructorUtilsImprovedTest {

    /**
     * Tests that {@link ConstructorUtils#getAccessibleConstructor(Constructor)}
     * returns the same instance for a constructor that is already accessible.
     *
     * The public no-argument constructor of {@link Object} is used as a
     * well-known, guaranteed accessible constructor.
     */
    @Test
    public void getAccessibleConstructorForAlreadyAccessibleConstructorShouldReturnSameInstance() throws Exception {
        // Arrange: Obtain a constructor that is known to be accessible.
        // The public, no-argument constructor of the Object class is a perfect candidate.
        final Constructor<Object> accessibleConstructor = Object.class.getConstructor();

        // Act: Pass the accessible constructor to the method under test.
        final Constructor<Object> resultConstructor = ConstructorUtils.getAccessibleConstructor(accessibleConstructor);

        // Assert: The method should return the exact same constructor instance without modification.
        assertSame("Expected the same constructor instance to be returned",
                accessibleConstructor, resultConstructor);
    }
}