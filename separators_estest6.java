package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class, focusing on its immutability.
 */
public class SeparatorsTest {

    /**
     * Tests that the {@code withObjectFieldValueSeparator} method correctly creates a new
     * {@link Separators} instance with the specified separator, while leaving the
     * original instance unchanged.
     */
    @Test
    public void withObjectFieldValueSeparator_shouldCreateNewInstanceWithUpdatedSeparator() {
        // Arrange: Create an initial Separators instance with known values.
        Separators originalSeparators = new Separators()
                .withObjectEntrySeparator(',')
                .withArrayValueSeparator(';');

        final char newFieldValueSeparator = '?';
        final char originalFieldValueSeparator = originalSeparators.getObjectFieldValueSeparator();

        // Act: Call the method under test to create a new, modified instance.
        Separators updatedSeparators = originalSeparators.withObjectFieldValueSeparator(newFieldValueSeparator);

        // Assert: Verify the properties of both the new and original instances.

        // 1. A new instance must be returned, confirming immutability.
        assertNotSame("A new instance should be returned", originalSeparators, updatedSeparators);

        // 2. The new instance should have the updated field value separator.
        assertEquals("The new instance should have the updated separator",
                newFieldValueSeparator, updatedSeparators.getObjectFieldValueSeparator());

        // 3. Other properties in the new instance should be copied from the original.
        assertEquals("Object entry separator should be unchanged in the new instance",
                originalSeparators.getObjectEntrySeparator(), updatedSeparators.getObjectEntrySeparator());
        assertEquals("Array value separator should be unchanged in the new instance",
                originalSeparators.getArrayValueSeparator(), updatedSeparators.getArrayValueSeparator());

        // 4. The original instance must not be modified.
        assertEquals("The original instance's separator should remain unchanged",
                originalFieldValueSeparator, originalSeparators.getObjectFieldValueSeparator());
    }
}