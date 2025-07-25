package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTest extends TestBase {

    // Simple class with a default constructor
    static class SomeClass {}

    // Inner class that requires an instance of the outer class
    class SomeInnerClass {}

    // Child class extending the test class
    class ChildOfThis extends ConstructorInstantiatorTest {}

    // Class with a constructor that takes a String argument
    static class SomeClass2 {
        SomeClass2(String x) {}
    }

    // Class with a constructor that takes an int argument
    static class SomeClass3 {
        SomeClass3(int i) {}
    }

    @Test
    public void shouldCreateInstanceOfClassWithDefaultConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, new Object[0]);
        Object instance = instantiator.newInstance(SomeClass.class);
        
        assertThat(instance.getClass()).isEqualTo(SomeClass.class);
    }

    @Test
    public void shouldCreateInstanceOfInnerClass() {
        ConstructorInstantiator instantiatorWithOuter = new ConstructorInstantiator(true, this);
        Object innerInstance = instantiatorWithOuter.newInstance(SomeInnerClass.class);
        
        assertThat(innerInstance.getClass()).isEqualTo(SomeInnerClass.class);

        ConstructorInstantiator instantiatorWithChild = new ConstructorInstantiator(true, new ChildOfThis());
        Object innerInstanceWithChild = instantiatorWithChild.newInstance(SomeInnerClass.class);
        
        assertThat(innerInstanceWithChild.getClass()).isEqualTo(SomeInnerClass.class);
    }

    @Test
    public void shouldCreateInstanceWithConstructorArguments() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, "someString");
        Object instance = instantiator.newInstance(SomeClass2.class);
        
        assertThat(instance.getClass()).isEqualTo(SomeClass2.class);
    }

    @Test
    public void shouldCreateInstanceWithNullArguments() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, new Object[] {null});
        Object instance = instantiator.newInstance(SomeClass2.class);
        
        assertThat(instance.getClass()).isEqualTo(SomeClass2.class);
    }

    @Test
    public void shouldCreateInstanceWithPrimitiveArguments() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(false, 123);
        Object instance = instantiator.newInstance(SomeClass3.class);
        
        assertThat(instance.getClass()).isEqualTo(SomeClass3.class);
    }

    @Test
    public void shouldFailWhenNullIsPassedForPrimitiveArgument() {
        assertThatThrownBy(() -> {
            ConstructorInstantiator instantiator = new ConstructorInstantiator(false, new Object[] {null});
            instantiator.newInstance(SomeClass3.class);
        })
        .isInstanceOf(org.mockito.creation.instance.InstantiationException.class)
        .hasMessageContaining("Unable to create instance of 'SomeClass3'.");
    }

    @Test
    public void shouldExplainWhenConstructorCannotBeFound() {
        try {
            ConstructorInstantiator instantiator = new ConstructorInstantiator(false, new Object[0]);
            instantiator.newInstance(SomeClass2.class);
            fail("Expected InstantiationException to be thrown");
        } catch (org.mockito.creation.instance.InstantiationException e) {
            assertThat(e)
                .hasMessageContaining("Unable to create instance of 'SomeClass2'.")
                .hasMessageContaining("Please ensure that the target class has a 0-arg constructor.");
        }
    }
}