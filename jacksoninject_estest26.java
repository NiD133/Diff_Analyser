package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JacksonInject.Value} class, focusing on its immutability and equality logic.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that two JacksonInject.Value instances are not considered equal
     * if they have different 'useInput' properties.
     */
    @Test
    public void testEquals_whenUseInputDiffers_shouldReturnFalse() {
        // Arrange
        Object id = new Object();
        JacksonInject.Value baseValue = JacksonInject.Value.forId(id);

        // Act: Create a new instance from the base one, but with a different 'useInput' value.
        // The 'with...' methods should create a new, distinct object.
        JacksonInject.Value modifiedValue = baseValue.withUseInput(Boolean.FALSE);

        // Assert
        // The two instances should not be equal because their 'useInput' property differs.
        assertNotEquals(baseValue, modifiedValue);

        // For completeness, ensure the equals contract is symmetrical.
        assertNotEquals(modifiedValue, baseValue);

        // Sanity check to ensure the ID was preserved in the new instance.
        assertTrue(modifiedValue.hasId());
    }
}