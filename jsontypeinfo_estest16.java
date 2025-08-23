package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the {@link JsonTypeInfo.Value} class,
 * particularly its "wither" methods which create new instances with modified properties.
 */
public class JsonTypeInfo_ESTestTest16 extends JsonTypeInfo_ESTest_scaffolding {

    /**
     * Tests that calling {@code withRequireTypeIdForSubtypes} with the same value
     * that the object already holds results in a new, but logically equal, instance.
     * This verifies the immutability contract of the Value object.
     */
    @Test
    public void withRequireTypeIdForSubtypes_whenCalledWithSameValue_returnsEqualButNotSameInstance() {
        // Arrange: Create an initial JsonTypeInfo.Value instance.
        // The original test used a confusing string-to-boolean conversion;
        // using Boolean.FALSE is much clearer.
        final Boolean requireTypeId = Boolean.FALSE;
        final String propertyName = "@2LLQRbW9{J2*\"1GY";

        JsonTypeInfo.Value initialValue = JsonTypeInfo.Value.construct(
                JsonTypeInfo.Id.MINIMAL_CLASS,
                JsonTypeInfo.As.WRAPPER_OBJECT,
                propertyName,
                Object.class,
                false, // idVisible
                requireTypeId
        );

        // Act: Call the method under test with the same boolean value.
        JsonTypeInfo.Value updatedValue = initialValue.withRequireTypeIdForSubtypes(requireTypeId);

        // Assert: Verify that the new instance is equal but not the same object.
        // This confirms the immutable nature of the Value class.
        assertEquals("The new instance should be logically equal to the original.", initialValue, updatedValue);
        assertNotSame("The 'with...' method should always return a new instance.", initialValue, updatedValue);

        // Also, verify that other properties were not unintentionally changed.
        assertEquals(propertyName, updatedValue.getPropertyName());
        assertFalse(updatedValue.getIdVisible());
    }
}