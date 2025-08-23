package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the {@link JsonTypeInfo.Value} class.
 */
public class JsonTypeInfoValueTest {

    /**
     * Tests the reflexivity of the JsonTypeInfo.Value.equals() method.
     * According to the contract of equals(), an object must be equal to itself.
     */
    @Test
    public void equals_shouldReturnTrue_whenComparingAnInstanceToItself() {
        // Arrange: Create an instance of JsonTypeInfo.Value with clear, representative properties.
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        String propertyName = "customTypeProperty";
        Class<?> defaultImpl = Object.class;
        boolean idVisible = false;
        Boolean requireTypeIdForSubtypes = false;

        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
            idType,
            inclusion,
            propertyName,
            defaultImpl,
            idVisible,
            requireTypeIdForSubtypes
        );

        // Act: Compare the instance with itself.
        boolean isEqualToItself = value.equals(value);

        // Assert: The result must be true, confirming the reflexive property of equals().
        assertTrue("An instance should always be equal to itself.", isEqualToItself);

        // Also, verify the state of the constructed object to ensure the test setup is correct.
        assertEquals("The property name should match the value provided during construction.",
            propertyName, value.getPropertyName());
        assertFalse("The idVisible flag should be false as set during construction.",
            value.getIdVisible());
        assertEquals("The IdType should match the value provided during construction.",
            idType, value.getIdType());
    }
}