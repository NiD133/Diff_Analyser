package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class contains refactored tests for {@link JsonTypeInfo.Value}.
 * The original test was auto-generated and has been improved for clarity.
 */
public class JsonTypeInfo_ESTestTest3 extends JsonTypeInfo_ESTest_scaffolding {

    /**
     * Tests that creating a new {@link JsonTypeInfo.Value} instance from an existing one
     * using {@code withPropertyName} results in two objects that are not equal.
     */
    @Test
    public void withPropertyName_shouldCreateUnequalInstance() {
        // Arrange: Start with the default empty Value instance.
        final JsonTypeInfo.Value baseValue = JsonTypeInfo.Value.EMPTY;
        final String newPropertyName = "EXTERNAL_PROPERTY";

        // Act: Create a new instance with a different property name.
        JsonTypeInfo.Value modifiedValue = baseValue.withPropertyName(newPropertyName);

        // Assert: The new instance should not be equal to the original,
        // and its property name should be updated.
        assertNotEquals("Instances with different property names should not be equal", baseValue, modifiedValue);
        assertEquals(newPropertyName, modifiedValue.getPropertyName());
    }
}