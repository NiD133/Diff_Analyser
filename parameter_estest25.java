package com.google.common.reflect;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import org.junit.Test;

/**
 * Contains tests for the {@link Parameter} class, focusing on its hashCode implementation.
 */
public class ParameterTest {

    /** A simple class with a method used as a fixture for obtaining Parameter instances. */
    @SuppressWarnings("unused") // Used for reflection by the test.
    private static class TestFixture {
        void sampleMethod(String name) {}
    }

    @Test
    public void hashCode_whenParametersAreEqual_returnsSameHashCode() throws Exception {
        // Arrange: Create two separate but logically identical Parameter instances
        // by reflecting on the same method. This provides a solid basis for testing
        // the equals() and hashCode() contract.
        Method method = TestFixture.class.getDeclaredMethod("sampleMethod", String.class);
        
        Invokable<?, ?> invokable1 = TypeToken.of(TestFixture.class).method(method);
        Parameter parameter1 = invokable1.getParameters().get(0);

        Invokable<?, ?> invokable2 = TypeToken.of(TestFixture.class).method(method);
        Parameter parameter2 = invokable2.getParameters().get(0);

        // Act & Assert:
        // 1. First, verify the premise: the two Parameter objects are considered equal.
        assertEquals(
            "Two Parameter instances representing the same method parameter should be equal.",
            parameter1,
            parameter2);

        // 2. Then, verify the core hashCode contract: equal objects must have equal hash codes.
        assertEquals(
            "Equal Parameter instances must have the same hash code.",
            parameter1.hashCode(),
            parameter2.hashCode());
    }
}