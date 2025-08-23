package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for the {@link Separators} class, focusing on its immutability
 * and "wither" methods.
 */
public class SeparatorsTest {

    /**
     * Tests that the {@code withObjectFieldValueSeparator} method correctly creates a new
     * instance with the updated separator, while leaving the original instance unchanged.
     */
    @Test
    public void shouldCreateNewInstanceWithUpdatedObjectFieldValueSeparator() {
        // Arrange: Create a default Separators instance.
        Separators defaultSeparators = Separators.createDefaultInstance();
        char newSeparator = '|';

        // Act: Call the "wither" method to get a new, modified instance.
        Separators modifiedSeparators = defaultSeparators.withObjectFieldValueSeparator(newSeparator);

        // Assert:

        // 1. The new instance is a different object.
        assertNotSame("A new instance should be created", defaultSeparators, modifiedSeparators);

        // 2. The new instance has the updated property value.
        assertEquals("The object field value separator should be updated",
                newSeparator, modifiedSeparators.getObjectFieldValueSeparator());

        // 3. Other properties on the new instance remain unchanged from the original.
        assertEquals("Object entry separator should be preserved",
                defaultSeparators.getObjectEntrySeparator(), modifiedSeparators.getObjectEntrySeparator());
        assertEquals("Array value separator should be preserved",
                defaultSeparators.getArrayValueSeparator(), modifiedSeparators.getArrayValueSeparator());
        assertEquals("Object field value spacing should be preserved",
                defaultSeparators.getObjectFieldValueSpacing(), modifiedSeparators.getObjectFieldValueSpacing());

        // 4. The original instance remains immutable.
        assertEquals("Original instance's separator should not change",
                ':', defaultSeparators.getObjectFieldValueSeparator());
    }
}