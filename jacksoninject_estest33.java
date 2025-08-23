package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link JacksonInject.Value} class.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that the predefined {@code JacksonInject.Value.EMPTY} constant
     * has a null ID, as it represents a default, unconfigured state.
     */
    @Test
    public void getId_onEmptyInstance_shouldReturnNull() {
        // Arrange
        JacksonInject.Value emptyValue = JacksonInject.Value.EMPTY;

        // Act
        Object actualId = emptyValue.getId();

        // Assert
        assertNull("The ID for the EMPTY value should be null.", actualId);
    }
}