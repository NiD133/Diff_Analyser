/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.mockitoutil.TestBase;
import org.mockito.creation.instance.InstantiationException;

/**
 * Tests for ConstructorInstantiator that favor clarity and minimal duplication.
 * Helper methods encapsulate the "hasOuterClassInstance" and "args" wiring so that
 * individual tests read as intent-first.
 */
public class ConstructorInstantiatorTest extends TestBase {

    // Simple types used by the tests

    static class SomeClass {}

    class SomeInnerClass {}

    class ChildOfThis extends ConstructorInstantiatorTest {}

    static class SomeClass2 {
        SomeClass2(String x) {}
    }

    static class SomeClass3 {
        SomeClass3(int i) {}
    }

    // -- Helper methods -------------------------------------------------------

    /**
     * Instantiate a top-level class (no outer class instance).
     */
    private static <T> T instantiate(Class<T> type, Object... args) {
        return new ConstructorInstantiator(false, args).newInstance(type);
    }

    /**
     * Instantiate a non-static inner class. The first constructor argument must be the
     * outer instance (the declaring type or one of its subclasses).
     */
    private static <T> T instantiateInner(Class<T> innerType, Object outerInstance) {
        return new ConstructorInstantiator(true, outerInstance).newInstance(innerType);
    }

    // -- Tests ----------------------------------------------------------------

    @Test
    public void creates_instances_using_default_constructor() {
        assertThat(instantiate(SomeClass.class)).isInstanceOf(SomeClass.class);
    }

    @Test
    public void creates_instances_of_non_static_inner_classes_when_outer_instance_is_provided() {
        // Works when the outer instance is 'this' (declaring type)
        assertThat(instantiateInner(SomeInnerClass.class, this)).isInstanceOf(SomeInnerClass.class);

        // Also works when the outer instance is a subclass of the declaring type
        assertThat(instantiateInner(SomeInnerClass.class, new ChildOfThis()))
                .isInstanceOf(SomeInnerClass.class);
    }

    @Test
    public void creates_instances_with_arguments() {
        assertThat(instantiate(SomeClass2.class, "someString")).isInstanceOf(SomeClass2.class);
    }

    @Test
    public void creates_instances_with_null_arguments_for_reference_types() {
        assertThat(instantiate(SomeClass2.class, new Object[] {null})).isInstanceOf(SomeClass2.class);
    }

    @Test
    public void creates_instances_with_primitive_arguments() {
        assertThat(instantiate(SomeClass3.class, 123)).isInstanceOf(SomeClass3.class);
    }

    @Test
    public void explains_failure_when_null_is_passed_for_primitive_parameter() {
        assertThatThrownBy(() -> instantiate(SomeClass3.class, new Object[] {null}))
                .isInstanceOf(InstantiationException.class)
                .hasMessageContaining("Unable to create instance of 'SomeClass3'.");
    }

    @Test
    public void explains_failure_when_no_matching_constructor_is_found() {
        // SomeClass2 has no 0-arg constructor; calling with no args should fail clearly.
        assertThatThrownBy(() -> instantiate(SomeClass2.class))
                .isInstanceOf(InstantiationException.class)
                .hasMessageContaining("Unable to create instance of 'SomeClass2'.")
                .hasMessageContaining("Please ensure that the target class has a 0-arg constructor.");
    }
}