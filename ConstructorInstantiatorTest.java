/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.mockito.creation.instance.InstantiationException;
import org.mockitoutil.TestBase;

/**
 * Tests for {@link ConstructorInstantiator}.
 */
public class ConstructorInstantiatorTest extends TestBase {

    // --- Test fixture classes ---

    static class ClassWithDefaultConstructor {}

    static class ClassWithParameterizedConstructor {
        ClassWithParameterizedConstructor(String arg) {}
    }

    static class ClassWithPrimitiveConstructor {
        ClassWithPrimitiveConstructor(int arg) {}
    }

    class InnerClass {}

    class ChildOfThis extends ConstructorInstantiatorTest {}

    // --- Test cases ---

    @Test
    public void should_create_instance_of_class_with_default_constructor() {
        // Arrange
        Instantiator instantiator = new ConstructorInstantiator(false, new Object[0]);

        // Act
        Object instance = instantiator.newInstance(ClassWithDefaultConstructor.class);

        // Assert
        assertThat(instance).isInstanceOf(ClassWithDefaultConstructor.class);
    }

    @Test
    public void should_create_instance_of_inner_class() {
        // Arrange
        Instantiator instantiatorWithThis = new ConstructorInstantiator(true, this);
        Instantiator instantiatorWithChild = new ConstructorInstantiator(true, new ChildOfThis());

        // Act & Assert
        assertThat(instantiatorWithThis.newInstance(InnerClass.class)).isInstanceOf(InnerClass.class);
        assertThat(instantiatorWithChild.newInstance(InnerClass.class)).isInstanceOf(InnerClass.class);
    }

    @Test
    public void should_create_instance_using_constructor_with_matching_argument() {
        // Arrange
        Instantiator instantiator = new ConstructorInstantiator(false, "someString");

        // Act
        Object instance = instantiator.newInstance(ClassWithParameterizedConstructor.class);

        // Assert
        assertThat(instance).isInstanceOf(ClassWithParameterizedConstructor.class);
    }

    @Test
    public void should_create_instance_when_constructor_argument_is_null() {
        // Arrange
        Instantiator instantiator = new ConstructorInstantiator(false, new Object[] {null});

        // Act
        Object instance = instantiator.newInstance(ClassWithParameterizedConstructor.class);

        // Assert
        assertThat(instance).isInstanceOf(ClassWithParameterizedConstructor.class);
    }

    @Test
    public void should_create_instance_using_constructor_with_primitive_argument() {
        // Arrange
        Instantiator instantiator = new ConstructorInstantiator(false, 123);

        // Act
        Object instance = instantiator.newInstance(ClassWithPrimitiveConstructor.class);

        // Assert
        assertThat(instance).isInstanceOf(ClassWithPrimitiveConstructor.class);
    }

    @Test
    public void should_throw_exception_when_null_is_passed_for_primitive_argument() {
        // Arrange
        Instantiator instantiator = new ConstructorInstantiator(false, new Object[] {null});

        // Act & Assert
        assertThatThrownBy(() -> instantiator.newInstance(ClassWithPrimitiveConstructor.class))
                .isInstanceOf(InstantiationException.class)
                .hasMessageContaining("Unable to create instance of 'ClassWithPrimitiveConstructor'.");
    }

    @Test
    public void should_throw_exception_with_helpful_message_when_no_matching_constructor_is_found() {
        // Arrange
        Instantiator instantiator = new ConstructorInstantiator(false, new Object[0]);

        // Act & Assert
        assertThatThrownBy(() -> instantiator.newInstance(ClassWithParameterizedConstructor.class))
                .isInstanceOf(InstantiationException.class)
                .hasMessageContaining(
                        "Unable to create instance of 'ClassWithParameterizedConstructor'.\n"
                                + "Please ensure that the target class has a 0-arg constructor.");
    }
}