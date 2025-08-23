package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Test suite for the {@link JsonTypeInfo.Value} class, focusing on its equality logic.
 */
public class JsonTypeInfoValueTest {

    /**
     * Tests that a {@link JsonTypeInfo.Value} instance constructed with specific
     * properties is not considered equal to the static {@code EMPTY} instance.
     */
    @Test
    public void equalsShouldReturnFalseForDistinctInstances() {
        // Arrange
        // The predefined EMPTY value instance, which serves as a baseline for comparison.
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;

        // A new Value instance constructed with non-default properties.
        JsonTypeInfo.Value constructedValue = JsonTypeInfo.Value.construct(
                JsonTypeInfo.Id.DEDUCTION,
                JsonTypeInfo.As.WRAPPER_OBJECT,
                "", // propertyName
                Object.class, // defaultImpl
                false, // idVisible
                Boolean.FALSE // requireTypeIdForSubtypes
        );

        // Act & Assert
        // The primary assertion: the two distinct instances should not be equal.
        assertNotEquals("A constructed value should not be equal to the EMPTY value.",
                emptyValue, constructedValue);

        // Secondary assertion: verify a property of the constructed object to ensure
        // it was created correctly.
        assertFalse("The 'idVisible' property should be false as specified during construction.",
                constructedValue.getIdVisible());
    }
}