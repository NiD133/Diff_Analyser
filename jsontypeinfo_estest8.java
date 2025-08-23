package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonTypeInfo.Value} class, focusing on its equals() method contract.
 */
public class JsonTypeInfoValueTest {

    /**
     * Tests that the equals() method of JsonTypeInfo.Value correctly returns false
     * when compared with a null object, adhering to the general contract of Object.equals().
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedWithNull() {
        // Arrange: Get a constant instance of JsonTypeInfo.Value
        JsonTypeInfo.Value value = JsonTypeInfo.Value.EMPTY;

        // Act & Assert: Verify that comparing the instance to null returns false
        assertFalse("The equals() method must return false for a null comparison.", value.equals(null));
    }
}