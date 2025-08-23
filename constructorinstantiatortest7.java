package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link ConstructorInstantiator} class, focusing on its ability
 * to create new object instances.
 */
public class ConstructorInstantiatorTest {

    /**
     * Verifies that `newInstance` can successfully create an object
     * when the target class has a public, default (no-argument) constructor.
     */
    @Test
    public void shouldInstantiateClassWithDefaultConstructor() {
        // Arrange: Create an instantiator configured for a class that is not an inner class
        // and requires no constructor arguments. The 'false' argument indicates there is no
        // outer class instance.
        Object[] noArguments = {};
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, noArguments);

        // Act: Attempt to create a new instance of the Object class, which has a
        // public no-argument constructor.
        Object instance = instantiator.newInstance(Object.class);

        // Assert: The instance should be successfully created and not null.
        assertNotNull("The created instance should not be null", instance);
    }
}