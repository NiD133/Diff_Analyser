package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.creation.instance.InstantiationException;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link ConstructorInstantiator}.
 *
 * This class focuses on scenarios involving constructor argument matching and instantiation failures.
 */
public class ConstructorInstantiatorTest extends TestBase {

    // A simple class with a constructor that accepts a primitive type.
    // Used to test argument type mismatch scenarios.
    static class ClassWithPrimitiveConstructor {
        ClassWithPrimitiveConstructor(int value) {
            // This constructor is not expected to be successfully invoked in the test.
        }
    }

    /**
     * This test verifies that the ConstructorInstantiator throws an InstantiationException
     * when attempting to instantiate a class with a primitive constructor argument (e.g., int)
     * by passing 'null'. Since 'null' cannot be unboxed to a primitive type, this should
     * lead to an internal reflection error, which is wrapped in Mockito's exception.
     */
    @Test
    public void shouldThrowExceptionWhenPassingNullForPrimitiveArgument() {
        // Arrange: Create an instantiator with a 'null' argument, intended for a primitive parameter.
        // The 'hasOuterClassInstance' flag is false because the target class is a static inner class.
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, new Object[]{null});

        // Act & Assert: Verify that attempting to instantiate the class throws the expected exception.
        assertThatThrownBy(() -> instantiator.newInstance(ClassWithPrimitiveConstructor.class))
            .isInstanceOf(InstantiationException.class)
            .hasMessageContaining("Unable to create instance of 'ClassWithPrimitiveConstructor'.");
    }
}