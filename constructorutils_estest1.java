package org.apache.commons.lang3.reflect;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link ConstructorUtils} class.
 */
public class ConstructorUtilsTest {

    /**
     * Tests that {@link ConstructorUtils#invokeExactConstructor(Class, Object[], Class[])}
     * can successfully invoke the default (no-argument) constructor of the {@link Object} class.
     *
     * <p>This test specifically verifies that a {@code null} value for the parameter types array
     * is handled correctly, as the Javadoc states it should be treated as an empty array.</p>
     */
    @Test
    public void invokeExactConstructor_withNoArgsAndNullParamTypes_shouldCreateObjectInstance() throws Exception {
        // Arrange: Set up the class and arguments to call the no-argument constructor of Object.
        final Class<Object> classToInstantiate = Object.class;
        final Object[] constructorArgs = new Object[0];
        final Class<?>[] parameterTypes = null; // The method under test should handle a null array.

        // Act: Invoke the constructor using the utility method.
        final Object instance = ConstructorUtils.invokeExactConstructor(
                classToInstantiate,
                constructorArgs,
                parameterTypes);

        // Assert: Verify that a non-null instance was successfully created.
        assertNotNull("The created instance should not be null.", instance);
    }
}