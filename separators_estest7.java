package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class, focusing on its immutability
 * and "with-er" methods.
 */
public class SeparatorsTest {

    /**
     * Tests that `withObjectFieldValueSeparator()` creates a new instance with the
     * specified separator, leaving the original instance unchanged.
     */
    @Test
    public void withObjectFieldValueSeparatorShouldReturnModifiedCopy() {
        // Arrange: Create an initial Separators instance with clear, distinct values.
        final char initialFieldValueSep = ':';
        final char initialObjectEntrySep = ',';
        final char initialArrayValueSep = ';';
        final Separators.Spacing spacing = Separators.Spacing.AFTER;

        Separators originalSeparators = new Separators(
            " ", // rootSeparator
            initialFieldValueSep,
            spacing,
            initialObjectEntrySep,
            spacing,
            initialArrayValueSep,
            spacing
        );

        // Act: Create a new instance by changing the object field-value separator.
        final char newFieldValueSep = '=';
        Separators updatedSeparators = originalSeparators.withObjectFieldValueSeparator(newFieldValueSep);

        // Assert: Verify the new instance is correctly modified and the original is unchanged.

        // 1. A new, distinct instance should be returned.
        assertNotSame("A new instance should be created", originalSeparators, updatedSeparators);

        // 2. The new instance should have the updated field-value separator.
        assertEquals("Field value separator should be updated in the new instance",
            newFieldValueSep, updatedSeparators.getObjectFieldValueSeparator());

        // 3. Other properties in the new instance should remain the same.
        assertEquals("Object entry separator should be unchanged in the new instance",
            initialObjectEntrySep, updatedSeparators.getObjectEntrySeparator());
        assertEquals("Array value separator should be unchanged in the new instance",
            initialArrayValueSep, updatedSeparators.getArrayValueSeparator());

        // 4. The original instance must remain unmodified.
        assertEquals("Original field value separator should not be modified",
            initialFieldValueSep, originalSeparators.getObjectFieldValueSeparator());
    }
}