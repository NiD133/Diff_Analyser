package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.creation.instance.Instantiator;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTest extends TestBase {

    // A helper class with a specific constructor signature for the test.
    private static class ClassWithSingleStringConstructor {
        ClassWithSingleStringConstructor(String text) {
            // This constructor is used to verify that the instantiator can find
            // and invoke a constructor with matching argument types.
        }
    }

    @Test
    public void shouldInstantiateClassUsingConstructorWithMatchingArguments() {
        // Arrange
        // The class to be instantiated requires a single String argument for its constructor.
        Object[] constructorArgs = {"a-string-argument"};

        // The 'false' flag indicates that the target class is not an inner class
        // and does not require an outer instance for instantiation.
        Instantiator instantiator = new ConstructorInstantiator(false, constructorArgs);

        // Act
        ClassWithSingleStringConstructor instance =
                instantiator.newInstance(ClassWithSingleStringConstructor.class);

        // Assert
        // The instantiator should successfully create an object of the correct type.
        assertThat(instance).isInstanceOf(ClassWithSingleStringConstructor.class);
    }
}