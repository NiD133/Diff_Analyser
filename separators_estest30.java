package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Separators} class, focusing on its immutability
 * and 'with' methods.
 */
public class SeparatorsTest {

    /**
     * Verifies that the {@code withObjectFieldValueSpacing} method correctly creates a new
     * {@link Separators} instance with the updated spacing for object field values,
     * while ensuring all other properties are copied from the original instance.
     * It also confirms the immutability of the original instance.
     */
    @Test
    public void withObjectFieldValueSpacing_shouldReturnNewInstanceWithUpdatedSpacing() {
        // Arrange: Set up the initial Separators object with specific, non-default values.
        Separators.Spacing initialSpacing = Separators.Spacing.NONE;
        Separators initialSeparators = new Separators(
            /* rootSeparator */ "",
            /* objectFieldValueSeparator */ 'J',
            /* objectFieldValueSpacing */ initialSpacing,
            /* objectEntrySeparator */ 'J',
            /* objectEntrySpacing */ Separators.Spacing.NONE,
            /* objectEmptySeparator */ "",
            /* arrayValueSeparator */ 'J',
            /* arrayValueSpacing */ Separators.Spacing.NONE,
            /* arrayEmptySeparator */ "2U#AD9"
        );
        Separators.Spacing newSpacing = Separators.Spacing.AFTER;

        // Act: Call the method under test to create a new, modified instance.
        Separators updatedSeparators = initialSeparators.withObjectFieldValueSpacing(newSpacing);

        // Assert: Verify the new instance is correct and the original is unchanged.

        // 1. A new, distinct instance should have been created.
        assertNotSame("The 'with' method should create a new instance.", initialSeparators, updatedSeparators);

        // 2. The specific property being changed should be updated in the new instance.
        assertEquals("Object field value spacing should be updated to the new value.",
            newSpacing, updatedSeparators.getObjectFieldValueSpacing());

        // 3. All other properties in the new instance should be copied from the original.
        assertEquals("Root separator should remain unchanged.",
            initialSeparators.getRootSeparator(), updatedSeparators.getRootSeparator());
        assertEquals("Object field value separator char should remain unchanged.",
            initialSeparators.getObjectFieldValueSeparator(), updatedSeparators.getObjectFieldValueSeparator());
        assertEquals("Object entry separator should remain unchanged.",
            initialSeparators.getObjectEntrySeparator(), updatedSeparators.getObjectEntrySeparator());
        assertEquals("Object entry spacing should remain unchanged.",
            initialSeparators.getObjectEntrySpacing(), updatedSeparators.getObjectEntrySpacing());
        assertEquals("Array value separator should remain unchanged.",
            initialSeparators.getArrayValueSeparator(), updatedSeparators.getArrayValueSeparator());
        assertEquals("Array value spacing should remain unchanged.",
            initialSeparators.getArrayValueSpacing(), updatedSeparators.getArrayValueSpacing());
        assertEquals("Array empty separator should remain unchanged.",
            initialSeparators.getArrayEmptySeparator(), updatedSeparators.getArrayEmptySeparator());

        // 4. The original instance should remain immutable.
        assertEquals("Original instance's spacing should not be modified.",
            initialSpacing, initialSeparators.getObjectFieldValueSpacing());
    }
}