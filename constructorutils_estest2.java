package org.apache.commons.lang3.reflect;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    /**
     * Tests that {@code invokeConstructor} can successfully create a new instance of
     * {@code Object} by calling its default, no-argument constructor.
     */
    @Test
    public void testInvokeConstructorWithNoArguments() throws Exception {
        // Arrange
        final Class<Object> classToInstantiate = Object.class;
        final Object[] emptyArgs = {};
        final Class<?>[] emptyParamTypes = {};

        // Act
        final Object newInstance = ConstructorUtils.invokeConstructor(classToInstantiate, emptyArgs, emptyParamTypes);

        // Assert
        assertNotNull("The created instance should not be null.", newInstance);
        assertEquals("The created instance should be of the correct class.", Object.class, newInstance.getClass());
    }
}