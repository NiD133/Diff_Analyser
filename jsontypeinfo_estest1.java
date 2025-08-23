package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link JsonTypeInfo.Id} enum.
 */
public class JsonTypeInfoIdTest {

    /**
     * Verifies that the CUSTOM Id type returns null for its default property name.
     * This is the expected behavior, as a custom type resolver is responsible for
     * defining its own property name and semantics.
     */
    @Test
    public void getDefaultPropertyName_forCustomId_shouldReturnNull() {
        // Arrange: The Id type under test is CUSTOM.
        JsonTypeInfo.Id customId = JsonTypeInfo.Id.CUSTOM;

        // Act: Retrieve the default property name.
        String propertyName = customId.getDefaultPropertyName();

        // Assert: The result should be null.
        assertNull("The default property name for Id.CUSTOM should be null.", propertyName);
    }
}