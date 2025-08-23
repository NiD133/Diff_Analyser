package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.creation.instance.InstantiationException;

/**
 * Unit tests for {@link ConstructorInstantiator}.
 */
public class ConstructorInstantiatorTest {

    /**
     * Verifies that an InstantiationException is thrown when attempting to instantiate a class
     * with arguments that do not match any of its available constructors.
     */
    @Test(expected = InstantiationException.class)
    public void newInstance_shouldThrowException_whenNoMatchingConstructorIsFound() {
        // Arrange
        // The java.lang.Object class has only a single, parameterless constructor.
        // We will attempt to instantiate it with three arguments, which should fail.
        Object[] nonMatchingArgs = {new Object(), "some-string", 42};
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, nonMatchingArgs);

        // Act
        // This call is expected to fail because no constructor for Object matches the signature
        // of the provided arguments (Object, String, Integer).
        instantiator.newInstance(Object.class);

        // Assert is handled by the @Test(expected) annotation
    }
}