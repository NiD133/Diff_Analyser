package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JacksonInject.Value} class, focusing on its properties and behavior.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that an empty {@link JacksonInject.Value} instance, created via the
     * {@code empty()} factory method, correctly reports that it does not have an ID.
     */
    @Test
    public void hasId_shouldReturnFalse_forEmptyValue() {
        // Arrange: Create an empty JacksonInject.Value instance.
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();

        // Act: Check if the instance has an ID.
        boolean hasId = emptyValue.hasId();

        // Assert: The result should be false, as an empty value has no ID.
        assertFalse("An empty JacksonInject.Value should not have an ID.", hasId);
    }
}