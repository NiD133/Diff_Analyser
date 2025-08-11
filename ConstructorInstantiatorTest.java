/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTest extends TestBase {

    static class ClassWithDefaultConstructor {}
    
    class InnerClass {}
    
    class ChildOfThis extends ConstructorInstantiatorTest {}
    
    static class ClassWithStringConstructor {
        ClassWithStringConstructor(String x) {}
    }
    
    static class ClassWithPrimitiveConstructor {
        ClassWithPrimitiveConstructor(int i) {}
    }

    @Test
    public void shouldInstantiateClassWithDefaultConstructor() {
        // Arrange
        ConstructorInstantiator instantiator = createInstantiator(false);
        
        // Act
        Object instance = instantiator.newInstance(ClassWithDefaultConstructor.class);
        
        // Assert
        assertThat(instance).isExactlyInstanceOf(ClassWithDefaultConstructor.class);
    }

    @Test
    public void shouldInstantiateInnerClassWhenOuterInstanceIsProvided() {
        // Arrange
        ConstructorInstantiator instantiatorWithThis = createInstantiator(true, this);
        ConstructorInstantiator instantiatorWithChild = createInstantiator(true, new ChildOfThis());
        
        // Act
        Object instance1 = instantiatorWithThis.newInstance(InnerClass.class);
        Object instance2 = instantiatorWithChild.newInstance(InnerClass.class);
        
        // Assert
        assertThat(instance1).isExactlyInstanceOf(InnerClass.class);
        assertThat(instance2).isExactlyInstanceOf(InnerClass.class);
    }

    @Test
    public void shouldInstantiateClassUsingStringArgument() {
        // Arrange
        ConstructorInstantiator instantiator = createInstantiator(false, "arg");
        
        // Act
        Object instance = instantiator.newInstance(ClassWithStringConstructor.class);
        
        // Assert
        assertThat(instance).isExactlyInstanceOf(ClassWithStringConstructor.class);
    }

    @Test
    public void shouldInstantiateClassUsingNullArgument() {
        // Arrange
        ConstructorInstantiator instantiator = createInstantiator(false, new Object[] { null });
        
        // Act
        Object instance = instantiator.newInstance(ClassWithStringConstructor.class);
        
        // Assert
        assertThat(instance).isExactlyInstanceOf(ClassWithStringConstructor.class);
    }

    @Test
    public void shouldInstantiateClassUsingPrimitiveArgument() {
        // Arrange
        ConstructorInstantiator instantiator = createInstantiator(false, 123);
        
        // Act
        Object instance = instantiator.newInstance(ClassWithPrimitiveConstructor.class);
        
        // Assert
        assertThat(instance).isExactlyInstanceOf(ClassWithPrimitiveConstructor.class);
    }

    @Test
    public void shouldThrowExceptionWhenNullProvidedForPrimitiveParameter() {
        // Arrange
        ConstructorInstantiator instantiator = createInstantiator(false, new Object[] { null });
        
        // Act & Assert
        assertThatThrownBy(() -> instantiator.newInstance(ClassWithPrimitiveConstructor.class))
            .isInstanceOf(org.mockito.creation.instance.InstantiationException.class)
            .hasMessageContaining("Unable to create instance of 'ClassWithPrimitiveConstructor'");
    }

    @Test
    public void shouldThrowDescriptiveExceptionWhenNoMatchingConstructorFound() {
        // Arrange
        ConstructorInstantiator instantiator = createInstantiator(false);
        Class<?> targetClass = ClassWithStringConstructor.class;
        
        // Act
        try {
            instantiator.newInstance(targetClass);
            fail("Expected InstantiationException");
        } 
        catch (org.mockito.creation.instance.InstantiationException e) {
            // Assert
            assertThat(e)
                .hasMessageContaining("Unable to create instance of 'ClassWithStringConstructor'")
                .hasMessageContaining("0-arg constructor");
        }
    }

    // Helper method for consistent instantiator creation
    private ConstructorInstantiator createInstantiator(boolean requiresOuterInstance, Object... args) {
        return new ConstructorInstantiator(requiresOuterInstance, args);
    }
}