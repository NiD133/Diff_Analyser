package org.apache.commons.lang3.reflect;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

/**
 * This test class contains tests for the {@link ConstructorUtils} class.
 * This specific file focuses on the behavior of getAccessibleConstructor(Constructor).
 */
// The class name and inheritance are from the original EvoSuite generated test.
public class ConstructorUtils_ESTestTest4 extends ConstructorUtils_ESTest_scaffolding {

    /**
     * Tests that getAccessibleConstructor(Constructor) returns the same instance
     * when the provided constructor is already accessible.
     */
    @Test
    public void testGetAccessibleConstructorWithAlreadyAccessibleConstructorReturnsSameInstance() throws Exception {
        // Arrange: Get a constructor that is already accessible.
        // The public no-argument constructor for Object.class is a perfect example.
        final Constructor<Object> accessibleConstructor = Object.class.getConstructor();
        assertTrue("Precondition failed: The constructor should be accessible by default.", accessibleConstructor.isAccessible());

        // Act: Call the method under test.
        final Constructor<Object> result = ConstructorUtils.getAccessibleConstructor(accessibleConstructor);

        // Assert: The method should return the exact same constructor instance.
        assertSame("The returned constructor should be the same instance as the input",
            accessibleConstructor, result);
        assertTrue("The returned constructor should remain accessible", result.isAccessible());
    }
}