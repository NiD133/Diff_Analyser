package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class.
 */
public class SeparatorsTest {

    /**
     * Verifies that the {@code withObjectEntrySeparator} method creates a new,
     * modified instance of {@link Separators} rather than mutating the original.
     * This test confirms the immutability of the class.
     */
    @Test
    public void withObjectEntrySeparator_shouldCreateModifiedCopyAndNotChangeOriginal() {
        // Arrange: Create an initial Separators instance with distinct values.
        Separators originalSeparators = new Separators(
            null,                      // rootSeparator
            ':',                       // objectFieldValueSeparator
            Separators.Spacing.NONE,   // objectFieldValueSpacing
            ',',                       // objectEntrySeparator
            Separators.Spacing.NONE,   // objectEntrySpacing
            null,                      // objectEmptySeparator
            ';',                       // arrayValueSeparator
            Separators.Spacing.NONE,   // arrayValueSpacing
            null                       // arrayEmptySeparator
        );

        // Act: Call the 'with' method to create a new instance with a different separator.
        char newEntrySeparator = '>';
        Separators updatedSeparators = originalSeparators.withObjectEntrySeparator(newEntrySeparator);

        // Assert:
        // 1. A new instance must be returned.
        assertNotSame("A new instance should be created, not the same one modified.",
                originalSeparators, updatedSeparators);

        // 2. The new instance should have the updated value.
        assertEquals("The new instance should reflect the changed entry separator.",
                newEntrySeparator, updatedSeparators.getObjectEntrySeparator());

        // 3. The original instance must remain unchanged.
        assertEquals("Original object field separator should be unchanged.",
                ':', originalSeparators.getObjectFieldValueSeparator());
        assertEquals("Original object entry separator should be unchanged.",
                ',', originalSeparators.getObjectEntrySeparator());
        assertEquals("Original array value separator should be unchanged.",
                ';', originalSeparators.getArrayValueSeparator());

        // 4. Other properties in the new instance should be copied from the original.
        assertEquals("Other properties like object field separator should be copied.",
                ':', updatedSeparators.getObjectFieldValueSeparator());
    }
}