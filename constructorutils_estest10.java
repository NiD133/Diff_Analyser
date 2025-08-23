package org.apache.commons.lang3.reflect;

import org.junit.Test;
import java.lang.reflect.InvocationTargetException;

/**
 * Unit tests for {@link org.apache.commons.lang3.reflect.ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    /**
     * Tests that invokeConstructor throws NoSuchMethodException when called with arguments
     * that do not match any existing constructor signature.
     */
    @Test(expected = NoSuchMethodException.class)
    public void invokeConstructorWithMismatchedArgumentsShouldThrowNoSuchMethodException()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Arrange: The java.lang.Object class only has a public no-argument constructor.
        // We will try to invoke a constructor that takes a single argument.
        final Class<Object> classToInstantiate = Object.class;
        final Object[] constructorArgs = new Object[]{ "an-argument" };

        // Act: Attempt to call a constructor on Object.class with an argument.
        // This should fail because no constructor matches the provided argument type (String).
        ConstructorUtils.invokeConstructor(classToInstantiate, constructorArgs);

        // Assert: The test expects a NoSuchMethodException, which is declared in the @Test annotation.
    }
}