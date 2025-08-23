package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class, focusing on its immutability
 * and 'with' methods.
 */
public class SeparatorsTest {

    /**
     * Tests that the {@code withArrayValueSeparator} method correctly creates a new
     * {@link Separators} instance with the specified array value separator,
     * while leaving all other properties and the original instance unchanged.
     */
    @Test
    public void withArrayValueSeparator_shouldReturnNewInstanceWithChangedSeparator() {
        // Arrange: Create an initial Separators instance with custom values.
        final Separators.Spacing spacing = Separators.Spacing.AFTER;
        final Separators originalSeparators = new Separators(
            "",      // rootSeparator
            ')',     // objectFieldValueSeparator
            spacing, // objectFieldValueSpacing
            ')',     // objectEntrySeparator
            spacing, // objectEntrySpacing
            "",      // objectEmptySeparator
            'M',     // arrayValueSeparator
            spacing, // arrayValueSpacing
            ""       // arrayEmptySeparator
        );
        final char newArraySeparator = 'T';

        // Act: Call the method under test to create a new instance.
        Separators updatedSeparators = originalSeparators.withArrayValueSeparator(newArraySeparator);

        // Assert: Verify the new instance and the immutability of the original.

        // 1. The new instance should have the updated array separator.
        assertEquals(newArraySeparator, updatedSeparators.getArrayValueSeparator());

        // 2. The new instance should retain all other properties from the original.
        assertEquals(originalSeparators.getRootSeparator(), updatedSeparators.getRootSeparator());
        assertEquals(originalSeparators.getObjectFieldValueSeparator(), updatedSeparators.getObjectFieldValueSeparator());
        assertEquals(originalSeparators.getObjectEntrySeparator(), updatedSeparators.getObjectEntrySeparator());
        assertEquals(originalSeparators.getObjectEmptySeparator(), updatedSeparators.getObjectEmptySeparator());
        assertEquals(originalSeparators.getArrayEmptySeparator(), updatedSeparators.getArrayEmptySeparator());
        assertEquals(originalSeparators.getObjectFieldValueSpacing(), updatedSeparators.getObjectFieldValueSpacing());

        // 3. The original instance must not be modified, confirming immutability.
        assertEquals('M', originalSeparators.getArrayValueSeparator());
        assertNotSame("A new instance should have been created", originalSeparators, updatedSeparators);
    }
}