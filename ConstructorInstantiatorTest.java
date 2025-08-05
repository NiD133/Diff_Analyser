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

    // Test fixture classes for different scenarios
    static class ClassWithDefaultConstructor {}

    class InnerClass {}

    class ChildOfTestClass extends ConstructorInstantiatorTest {}

    static class ClassWithStringConstructor {
        ClassWithStringConstructor(String parameter) {}
    }

    static class ClassWithPrimitiveConstructor {
        ClassWithPrimitiveConstructor(int parameter) {}
    }

    @Test
    public void should_create_instance_using_default_constructor() {
        // Given
        ConstructorInstantiator instantiator = createInstantiatorWithNoArguments();
        
        // When
        Object instance = instantiator.newInstance(ClassWithDefaultConstructor.class);
        
        // Then
        assertThat(instance).isInstanceOf(ClassWithDefaultConstructor.class);
    }

    @Test
    public void should_create_inner_class_instances_with_outer_class_reference() {
        // Given - instantiator configured for inner classes with outer instance
        ConstructorInstantiator instantiatorWithThisReference = 
            createInstantiatorForInnerClass(this);
        ConstructorInstantiator instantiatorWithChildReference = 
            createInstantiatorForInnerClass(new ChildOfTestClass());
        
        // When & Then - both outer instances should work for creating inner class
        Object instanceFromThis = instantiatorWithThisReference.newInstance(InnerClass.class);
        assertThat(instanceFromThis).isInstanceOf(InnerClass.class);
        
        Object instanceFromChild = instantiatorWithChildReference.newInstance(InnerClass.class);
        assertThat(instanceFromChild).isInstanceOf(InnerClass.class);
    }

    @Test
    public void should_create_instance_with_string_constructor_argument() {
        // Given
        String constructorArgument = "test string";
        ConstructorInstantiator instantiator = createInstantiatorWithArguments(constructorArgument);
        
        // When
        Object instance = instantiator.newInstance(ClassWithStringConstructor.class);
        
        // Then
        assertThat(instance).isInstanceOf(ClassWithStringConstructor.class);
    }

    @Test
    public void should_create_instance_with_null_constructor_argument() {
        // Given
        Object[] nullArgument = {null};
        ConstructorInstantiator instantiator = createInstantiatorWithArguments(nullArgument);
        
        // When
        Object instance = instantiator.newInstance(ClassWithStringConstructor.class);
        
        // Then
        assertThat(instance).isInstanceOf(ClassWithStringConstructor.class);
    }

    @Test
    public void should_create_instance_with_primitive_constructor_argument() {
        // Given
        int primitiveArgument = 123;
        ConstructorInstantiator instantiator = createInstantiatorWithArguments(primitiveArgument);
        
        // When
        Object instance = instantiator.newInstance(ClassWithPrimitiveConstructor.class);
        
        // Then
        assertThat(instance).isInstanceOf(ClassWithPrimitiveConstructor.class);
    }

    @Test
    public void should_throw_exception_when_null_passed_for_primitive_parameter() {
        // Given
        Object[] nullArgument = {null};
        ConstructorInstantiator instantiator = createInstantiatorWithArguments(nullArgument);
        
        // When & Then
        assertThatThrownBy(() -> instantiator.newInstance(ClassWithPrimitiveConstructor.class))
                .isInstanceOf(org.mockito.creation.instance.InstantiationException.class)
                .hasMessageContaining("Unable to create instance of 'ClassWithPrimitiveConstructor'.");
    }

    @Test
    public void should_provide_helpful_error_when_no_matching_constructor_found() {
        // Given - instantiator with no arguments, but target class requires arguments
        ConstructorInstantiator instantiator = createInstantiatorWithNoArguments();
        
        // When & Then
        try {
            instantiator.newInstance(ClassWithStringConstructor.class);
            fail("Expected InstantiationException to be thrown");
        } catch (org.mockito.creation.instance.InstantiationException exception) {
            assertThat(exception)
                    .hasMessageContaining("Unable to create instance of 'ClassWithStringConstructor'")
                    .hasMessageContaining("Please ensure that the target class has a 0-arg constructor.");
        }
    }

    // Helper methods to improve test readability
    
    private ConstructorInstantiator createInstantiatorWithNoArguments() {
        return new ConstructorInstantiator(false, new Object[0]);
    }
    
    private ConstructorInstantiator createInstantiatorForInnerClass(Object outerInstance) {
        return new ConstructorInstantiator(true, outerInstance);
    }
    
    private ConstructorInstantiator createInstantiatorWithArguments(Object... arguments) {
        return new ConstructorInstantiator(false, arguments);
    }
}