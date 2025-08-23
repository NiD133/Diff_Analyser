package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.creation.instance.Instantiator;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConstructorInstantiator} focusing on its ability to handle
 * constructors with primitive type arguments.
 */
public class ConstructorInstantiatorTest extends TestBase {

    // A simple class with a constructor that accepts a primitive int.
    static class ClassWithPrimitiveConstructor {
        ClassWithPrimitiveConstructor(int value) {
            // This constructor is the target for the test.
        }
    }

    @Test
    public void shouldInstantiateClassWithPrimitiveArgument() {
        // given
        // An instantiator configured with an argument that can be unboxed to a primitive 'int'.
        int constructorArg = 123;
        Instantiator instantiator = new ConstructorInstantiator(false, constructorArg);

        // when
        // We request a new instance of a class that has a matching primitive constructor.
        Object instance = instantiator.newInstance(ClassWithPrimitiveConstructor.class);

        // then
        // The instance is successfully created and is of the correct type.
        assertThat(instance).isInstanceOf(ClassWithPrimitiveConstructor.class);
    }
}