package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.creation.instance.Instantiator;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConstructorInstantiator}.
 */
public class ConstructorInstantiatorTest extends TestBase {

    // A helper class with a constructor that accepts a single reference type.
    // This is used to verify that the instantiator can match it with a null argument.
    static class ClassWithSingleReferenceConstructor {
        ClassWithSingleReferenceConstructor(String anyString) {
            // The constructor logic is not relevant for this test.
        }
    }

    @Test
    public void shouldInstantiateClassWhenConstructorArgumentIsNull() {
        // Arrange: Create an instantiator configured to use a single 'null' argument.
        // The 'false' flag indicates that the target class is not an inner class
        // that requires an outer instance.
        Instantiator instantiator = new ConstructorInstantiator(false, new Object[]{null});

        // Act: Attempt to create an instance of a class whose constructor accepts a reference type.
        Object instance = instantiator.newInstance(ClassWithSingleReferenceConstructor.class);

        // Assert: The instantiator should successfully create the instance because 'null'
        // is a valid value for any reference type parameter (like String).
        assertThat(instance).isInstanceOf(ClassWithSingleReferenceConstructor.class);
    }
}