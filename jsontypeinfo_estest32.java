package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link JsonTypeInfo.Value} class, focusing on its construction and property accessors.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that the {@code JsonTypeInfo.Value.construct} factory method correctly
     * creates an instance and that all its properties are accessible via their respective getters.
     */
    @Test
    public void construct_shouldCreateValueWithCorrectProperties() {
        // Arrange: Define the configuration parameters for creating a JsonTypeInfo.Value instance.
        JsonTypeInfo.Id expectedIdType = JsonTypeInfo.Id.CLASS;
        JsonTypeInfo.As expectedInclusionType = JsonTypeInfo.As.EXISTING_PROPERTY;
        String expectedPropertyName = "customTypeProperty";
        Class<?> expectedDefaultImpl = Object.class;
        boolean expectedIdVisible = true;
        Boolean expectedRequireTypeId = false;

        // Act: Create the JsonTypeInfo.Value instance using the static factory method.
        JsonTypeInfo.Value result = JsonTypeInfo.Value.construct(
                expectedIdType,
                expectedInclusionType,
                expectedPropertyName,
                expectedDefaultImpl,
                expectedIdVisible,
                expectedRequireTypeId
        );

        // Assert: Verify that all properties of the created instance match the expected values.
        assertNotNull("The constructed value should not be null.", result);
        assertEquals("The ID type should match the constructor argument.",
                expectedIdType, result.getIdType());
        assertEquals("The inclusion type should match the constructor argument.",
                expectedInclusionType, result.getInclusionType());
        assertEquals("The property name should match the constructor argument.",
                expectedPropertyName, result.getPropertyName());
        assertEquals("The default implementation class should match the constructor argument.",
                expectedDefaultImpl, result.getDefaultImpl());
        assertTrue("The ID visibility should be true as specified.", result.getIdVisible());
        assertEquals("The 'require type ID' flag should match the constructor argument.",
                expectedRequireTypeId, result.getRequireTypeIdForSubtypes());
    }
}