package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTest extends TestBase {

    // Test classes for instantiation
    static class SomeClass {}

    class SomeInnerClass {}

    class ChildOfThis extends ConstructorInstantiatorTest {}

    static class SomeClassWithConstructor {
        SomeClassWithConstructor(String x) {}
    }

    static class SomeClassWithPrimitiveConstructor {
        SomeClassWithPrimitiveConstructor(int i) {}
    }

    @Test
    public void shouldCreateInstanceOfClassWithNoArgsConstructor() {
        // Arrange & Act
        Object instance = new ConstructorInstantiator(false, new Object[0])
                .newInstance(SomeClass.class);

        // Assert
        assertThat(instance.getClass()).isEqualTo(SomeClass.class);
    }

    @Test
    public void shouldCreateInstanceOfInnerClass() {
        // Arrange & Act
        Object instanceWithOuter = new ConstructorInstantiator(true, this)
                .newInstance(SomeInnerClass.class);
        Object instanceWithChild = new ConstructorInstantiator(true, new ChildOfThis())
                .newInstance(SomeInnerClass.class);

        // Assert
        assertThat(instanceWithOuter.getClass()).isEqualTo(SomeInnerClass.class);
        assertThat(instanceWithChild.getClass()).isEqualTo(SomeInnerClass.class);
    }

    @Test
    public void shouldCreateInstanceWithConstructorArguments() {
        // Arrange & Act
        Object instance = new ConstructorInstantiator(false, "someString")
                .newInstance(SomeClassWithConstructor.class);

        // Assert
        assertThat(instance.getClass()).isEqualTo(SomeClassWithConstructor.class);
    }

    @Test
    public void shouldCreateInstanceWithNullArguments() {
        // Arrange & Act
        Object instance = new ConstructorInstantiator(false, new Object[] {null})
                .newInstance(SomeClassWithConstructor.class);

        // Assert
        assertThat(instance.getClass()).isEqualTo(SomeClassWithConstructor.class);
    }

    @Test
    public void shouldCreateInstanceWithPrimitiveArguments() {
        // Arrange & Act
        Object instance = new ConstructorInstantiator(false, 123)
                .newInstance(SomeClassWithPrimitiveConstructor.class);

        // Assert
        assertThat(instance.getClass()).isEqualTo(SomeClassWithPrimitiveConstructor.class);
    }

    @Test
    public void shouldFailWhenNullPassedForPrimitive() {
        // Act & Assert
        assertThatThrownBy(() -> {
            new ConstructorInstantiator(false, new Object[] {null})
                    .newInstance(SomeClassWithPrimitiveConstructor.class);
        })
        .isInstanceOf(org.mockito.creation.instance.InstantiationException.class)
        .hasMessageContaining("Unable to create instance of 'SomeClassWithPrimitiveConstructor'.");
    }

    @Test
    public void shouldExplainWhenConstructorCannotBeFound() {
        try {
            // Act
            new ConstructorInstantiator(false, new Object[0]).newInstance(SomeClassWithConstructor.class);
            fail("Expected InstantiationException to be thrown");
        } catch (org.mockito.creation.instance.InstantiationException e) {
            // Assert
            assertThat(e).hasMessageContaining(
                    "Unable to create instance of 'SomeClassWithConstructor'.\n"
                            + "Please ensure that the target class has a 0-arg constructor.");
        }
    }
}