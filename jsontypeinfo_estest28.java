package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonTypeInfo.Value} class, focusing on the
 * properties of its default instances.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that the default 'idVisible' property of the
     * {@link JsonTypeInfo.Value#EMPTY} constant is correctly set to false.
     */
    @Test
    public void emptyInstanceShouldHaveIdVisibleFalse() {
        // Arrange: The EMPTY constant is a pre-configured, default instance.
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;

        // Act: Retrieve the value of the idVisible property.
        boolean isIdVisible = emptyValue.getIdVisible();

        // Assert: The default value for idVisible should be false.
        assertFalse("The 'idVisible' property for JsonTypeInfo.Value.EMPTY should be false by default.", isIdVisible);
    }
}