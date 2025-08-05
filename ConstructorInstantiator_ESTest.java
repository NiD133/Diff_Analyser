package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.creation.instance.InstantiationException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Test suite for {@link ConstructorInstantiator}.
 */
public class ConstructorInstantiatorTest {

    // --- Helper classes for testing various instantiation scenarios ---

    private static class ClassWithDefaultConstructor {}

    private static class ClassWithParameterizedConstructor {
        private final String value;
        public ClassWithParameterizedConstructor(String value) {
            this.value = value;
        }
    }

    private static class ClassWithoutDefaultConstructor {
        public ClassWithoutDefaultConstructor(String arg) {}
    }

    private static class ClassWithThrowingConstructor {
        public ClassWithThrowingConstructor() {
            throw new UnsupportedOperationException("Constructor failed");
        }
    }

    private static class Outer {
        class Inner {
            // A constructor that implicitly takes an Outer instance.
            public Inner() {}

            public Outer getOuter() {
                return Outer.this;
            }
        }
    }

    // --- Test cases ---

    @Test
    public void shouldInstantiateClassWithDefaultConstructor() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, new Object[0]);

        // Act
        ClassWithDefaultConstructor instance = instantiator.newInstance(ClassWithDefaultConstructor.class);

        // Assert
        assertNotNull(instance);
    }

    @Test
    public void shouldInstantiateClassWithMatchingParameterizedConstructor() {
        // Arrange
        String argument = "test-arg";
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, argument);

        // Act
        ClassWithParameterizedConstructor instance = instantiator.newInstance(ClassWithParameterizedConstructor.class);

        // Assert
        assertNotNull(instance);
        assertSame(argument, instance.value);
    }

    @Test
    public void shouldInstantiateInnerClassUsingOuterInstance() {
        // Arrange
        Outer outerInstance = new Outer();
        // The outer instance is passed as the first constructor argument for inner classes.
        ConstructorInstantiator instantiator = new ConstructorInstantiator(true, outerInstance);

        // Act
        Outer.Inner innerInstance = instantiator.newInstance(Outer.Inner.class);

        // Assert
        assertNotNull(innerInstance);
        assertSame(outerInstance, innerInstance.getOuter());
    }

    @Test(expected = InstantiationException.class)
    public void shouldThrowExceptionWhenNoConstructorMatchesArguments() {
        // Arrange: ClassWithParameterizedConstructor requires a String, but we provide an Integer.
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, 123);

        // Act
        instantiator.newInstance(ClassWithParameterizedConstructor.class);

        // Assert: InstantiationException is expected.
    }

    @Test(expected = InstantiationException.class)
    public void shouldThrowExceptionForClassWithoutDefaultConstructorWhenNoArgsProvided() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, new Object[0]);

        // Act
        instantiator.newInstance(ClassWithoutDefaultConstructor.class);

        // Assert: InstantiationException is expected.
    }

    @Test(expected = InstantiationException.class)
    public void shouldWrapExceptionWhenTargetConstructorThrows() {
        // Arrange
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, new Object[0]);

        // Act
        instantiator.newInstance(ClassWithThrowingConstructor.class);

        // Assert: InstantiationException is expected, wrapping the original constructor's exception.
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenConstructorArgumentsArrayIsNull() {
        // Arrange: This tests an invalid usage scenario where the arguments array is null.
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, (Object[]) null);

        // Act
        instantiator.newInstance(Object.class);

        // Assert: NullPointerException is expected due to current implementation.
    }
}