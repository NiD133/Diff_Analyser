package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link JacksonInject.Value} class, focusing on its factory and
 * mutant factory methods.
 */
public class JacksonInjectValueTest {

    /**
     * Tests that calling {@code withOptional(null)} on an empty {@link JacksonInject.Value}
     * instance returns the same instance, confirming it's an identity operation.
     */
    @Test
    public void withOptional_whenCalledWithNullOnEmptyValue_returnsSameInstance() {
        // Arrange: Start with the singleton empty instance.
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();

        // Act: Attempt to modify the 'optional' property with a null value.
        JacksonInject.Value result = emptyValue.withOptional(null);

        // Assert: The method should recognize that no change is being made
        // and return the original instance as an optimization.
        assertSame("Expected withOptional(null) on an empty value to be a no-op and return the same instance.",
                emptyValue, result);
    }
}