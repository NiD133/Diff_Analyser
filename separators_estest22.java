package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class, focusing on its immutability
 * and "with" methods.
 */
public class SeparatorsTest {

    /**
     * Verifies that the withArrayValueSpacing() method creates a new Separators instance
     * with the specified array value spacing, leaving the original instance unmodified.
     */
    @Test
    public void withArrayValueSpacing_shouldReturnNewInstance_withModifiedArraySpacing() {
        // Arrange: Create an initial Separators instance with known values.
        final Separators.Spacing initialSpacing = Separators.Spacing.NONE;
        final Separators initialSeparators = new Separators(
            /* rootSeparator */ "",
            /* objectFieldValueSeparator */ ':',
            /* objectFieldValueSpacing */ Separators.Spacing.NONE,
            /* objectEntrySeparator */ ',',
            /* objectEntrySpacing */ Separators.Spacing.NONE,
            /* objectEmptySeparator */ " ",
            /* arrayValueSeparator */ ',',
            /* arrayValueSpacing */ initialSpacing,
            /* arrayEmptySeparator */ " "
        );

        // Act: Call the method under test to create a new instance with updated spacing.
        final Separators.Spacing newSpacing = Separators.Spacing.AFTER;
        final Separators updatedSeparators = initialSeparators.withArrayValueSpacing(newSpacing);

        // Assert: Verify the state of both the new and original instances.

        // 1. The method must return a new instance, not modify the original.
        assertNotSame("A new instance should be created.", initialSeparators, updatedSeparators);

        // 2. The new instance should have the updated array value spacing.
        assertEquals("The array value spacing should be updated in the new instance.",
            newSpacing, updatedSeparators.getArrayValueSpacing());

        // 3. The original instance must remain unchanged (test for immutability).
        assertEquals("The original instance should be immutable.",
            initialSpacing, initialSeparators.getArrayValueSpacing());

        // 4. Verify that other properties were correctly copied to the new instance.
        assertEquals("Root separator should be unchanged.",
            initialSeparators.getRootSeparator(), updatedSeparators.getRootSeparator());
        assertEquals("Object entry separator should be unchanged.",
            initialSeparators.getObjectEntrySeparator(), updatedSeparators.getObjectEntrySeparator());
        assertEquals("Array empty separator should be unchanged.",
            initialSeparators.getArrayEmptySeparator(), updatedSeparators.getArrayEmptySeparator());
    }
}