package org.apache.commons.lang3.reflect;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    /**
     * Tests that {@link ConstructorUtils#invokeExactConstructor(Class, Object...)}
     * can successfully create an instance of a class using its default, no-argument constructor.
     */
    @Test
    public void invokeExactConstructor_withNoArguments_shouldCreateNewInstance() throws Exception {
        // Arrange: Define the class to instantiate and the arguments for its constructor.
        // An empty array signifies a call to the no-argument constructor.
        final Class<Object> classToInstantiate = Object.class;
        final Object[] noArguments = {};

        // Act: Invoke the constructor via the utility method.
        final Object newInstance = ConstructorUtils.invokeExactConstructor(classToInstantiate, noArguments);

        // Assert: The invocation should succeed and return a valid, non-null instance.
        assertNotNull("The created instance should not be null", newInstance);
        assertTrue("The instance should be of the expected type", newInstance instanceof Object);
    }
}