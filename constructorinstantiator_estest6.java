package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.creation.instance.InstantiationException;

/**
 * Test suite for {@link ConstructorInstantiator}.
 */
public class ConstructorInstantiatorTest {

    /**
     * Verifies that {@code newInstance()} throws an {@link InstantiationException}
     * when it cannot find a constructor that matches the provided arguments.
     *
     * <p>In this scenario, the instantiator is configured to provide an outer class instance
     * for an inner class, but it is then asked to instantiate {@code Integer.class}, which is not
     * an inner class and has no such constructor. This mismatch should result in an exception.</p>
     */
    @Test(expected = InstantiationException.class)
    public void shouldThrowExceptionWhenNoMatchingConstructorIsFound() {
        // Given: An instantiator configured for an inner class (expects an outer instance)
        // but with no additional constructor arguments.
        boolean requiresOuterClassInstance = true;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(requiresOuterClassInstance, new Object[0]);

        // When & Then: Attempting to instantiate a class (Integer) that does not have a
        // constructor matching the configuration should throw an InstantiationException.
        instantiator.newInstance(Integer.class);
    }
}