package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the {@link JsonTypeInfo.Value} class, verifying its
 * data-holding capabilities.
 */
public class JsonTypeInfoValueTest {

    /**
     * Tests that the constructor of {@link JsonTypeInfo.Value} correctly initializes
     * its properties, and that the corresponding getter methods return the
     * exact values provided during construction.
     */
    @Test
    public void gettersShouldReturnValuesPassedToConstructor() {
        // Arrange: Define the configuration for a JsonTypeInfo.Value instance.
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.DEDUCTION;
        JsonTypeInfo.As inclusionMechanism = JsonTypeInfo.As.WRAPPER_OBJECT;
        String propertyName = "H:a";
        Class<Integer> defaultImplementation = Integer.class;
        boolean isIdVisible = true;
        Boolean requireTypeId = Boolean.TRUE;

        // Act: Create the JsonTypeInfo.Value instance using the defined configuration.
        JsonTypeInfo.Value jsonTypeInfoValue = new JsonTypeInfo.Value(
                idType,
                inclusionMechanism,
                propertyName,
                defaultImplementation,
                isIdVisible,
                requireTypeId
        );

        // Assert: Verify that each getter returns the value provided to the constructor.
        assertEquals("The ID type should match the constructor argument.",
                idType, jsonTypeInfoValue.getIdType());
        assertEquals("The inclusion mechanism should match the constructor argument.",
                inclusionMechanism, jsonTypeInfoValue.getInclusionType());
        assertEquals("The property name should match the constructor argument.",
                propertyName, jsonTypeInfoValue.getPropertyName());
        assertEquals("The default implementation class should match the constructor argument.",
                defaultImplementation, jsonTypeInfoValue.getDefaultImpl());
        assertTrue("The ID visibility should match the constructor argument.",
                jsonTypeInfoValue.getIdVisible());
        assertEquals("The 'require type ID' flag should match the constructor argument.",
                requireTypeId, jsonTypeInfoValue.getRequireTypeIdForSubtypes());
    }
}