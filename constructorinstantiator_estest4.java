package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.creation.instance.InstantiationException;

/**
 * This test class contains tests for {@link ConstructorInstantiator}.
 * This particular test was improved for understandability.
 */
public class ConstructorInstantiator_ESTestTest4 extends ConstructorInstantiator_ESTest_scaffolding {

    /**
     * Verifies that if a suitable constructor is found, but its invocation throws an exception,
     * the ConstructorInstantiator wraps and re-throws it as an InstantiationException.
     *
     * In this scenario, the instantiator will select the Integer(String) constructor.
     * Calling `new Integer((String) null)` throws a NumberFormatException, which Mockito
     * should wrap in its own InstantiationException.
     */
    @Test(expected = InstantiationException.class)
    public void shouldThrowExceptionWhenConstructorInvocationFails() {
        // Arrange
        // We provide a single null argument. This will match the Integer(String) constructor.
        Object[] constructorArgs = new Object[]{null};
        Class<Integer> classToInstantiate = Integer.class;

        // The 'hasOuterClassInstance' flag is not relevant here as Integer is not an inner class.
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, constructorArgs);

        // Act & Assert
        // The newInstance call is expected to throw an InstantiationException because the
        // underlying constructor call (new Integer(null)) will fail.
        instantiator.newInstance(classToInstantiate);
    }
}