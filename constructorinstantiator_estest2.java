package org.mockito.internal.creation.instance;

import org.junit.Test;

/**
 * Tests for {@link ConstructorInstantiator}.
 */
public class ConstructorInstantiatorTest {

    @Test(expected = NullPointerException.class)
    public void newInstance_shouldThrowNullPointerException_whenConstructedWithNullArguments() {
        // Arrange: Create an instantiator with a null array for constructor arguments.
        // The varargs parameter requires an explicit cast to null to avoid ambiguity.
        Object[] nullConstructorArgs = null;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, nullConstructorArgs);

        // Act & Assert: Attempting to create a new instance should fail with an NPE
        // because the internal logic will try to access the null arguments array.
        instantiator.newInstance(Object.class);
    }
}