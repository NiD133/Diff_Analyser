package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.creation.instance.Instantiator;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConstructorInstantiator} focused on creating instances of non-static inner classes.
 */
public class ConstructorInstantiatorInnerClassTest extends TestBase {

    // A clear, self-contained outer class for testing purposes.
    static class Outer {
        // A non-static inner class that requires an instance of Outer to be created.
        class Inner {
            // The implicit constructor is Inner(Outer outer)
        }
    }

    // A subclass of Outer to test polymorphism during instantiation.
    static class SubclassOfOuter extends Outer {
    }

    @Test
    public void shouldInstantiateInnerClass_whenCorrectOuterInstanceIsProvided() {
        // Arrange
        Outer outerInstance = new Outer();
        // The 'true' flag indicates that an outer instance is being provided as the first constructor argument.
        Instantiator instantiator = new ConstructorInstantiator(true, outerInstance);

        // Act
        Outer.Inner instance = instantiator.newInstance(Outer.Inner.class);

        // Assert
        assertThat(instance).isInstanceOf(Outer.Inner.class);
    }

    @Test
    public void shouldInstantiateInnerClass_whenSubclassOfOuterInstanceIsProvided() {
        // Arrange
        SubclassOfOuter subclassInstance = new SubclassOfOuter();
        // The instantiator should be able to use a subclass of the required outer class.
        Instantiator instantiator = new ConstructorInstantiator(true, subclassInstance);

        // Act
        Outer.Inner instance = instantiator.newInstance(Outer.Inner.class);

        // Assert
        assertThat(instance).isInstanceOf(Outer.Inner.class);
    }
}