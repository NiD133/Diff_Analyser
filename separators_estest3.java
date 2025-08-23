package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the Separators class, focusing on its immutability
 * and 'with' methods.
 *
 * Note: The original class name "Separators_ESTestTest3" is kept to match the
 * provided context. In a real-world scenario, it would be renamed to "SeparatorsTest".
 */
public class Separators_ESTestTest3 {

    /**
     * Verifies that the withObjectEntrySpacing() method correctly returns a new
     * Separators instance with the updated spacing, while leaving the original
     * instance unmodified.
     */
    @Test
    public void withObjectEntrySpacing_shouldReturnNewInstanceWithUpdatedSpacing() {
        // Arrange: Create a Separators instance with custom, non-default values.
        Separators.Spacing initialEntrySpacing = Separators.Spacing.BEFORE;
        Separators originalSeparators = new Separators(
            /* rootSeparator */ "",
            /* objectFieldValueSeparator */ ':',
            /* objectFieldValueSpacing */ Separators.Spacing.AFTER,
            /* objectEntrySeparator */ ',',
            /* objectEntrySpacing */ initialEntrySpacing,
            /* objectEmptySeparator */ "",
            /* arrayValueSeparator */ ',',
            /* arrayValueSpacing */ Separators.Spacing.AFTER,
            /* arrayEmptySeparator */ ""
        );

        Separators.Spacing newEntrySpacing = Separators.Spacing.AFTER;

        // Act: Call the method under test to create a new instance.
        Separators updatedSeparators = originalSeparators.withObjectEntrySpacing(newEntrySpacing);

        // Assert: Verify the behavior of the 'with' method.

        // 1. A new, distinct instance should be returned.
        assertNotSame("The 'with' method must return a new instance.", originalSeparators, updatedSeparators);

        // 2. The specific property should be updated on the new instance.
        assertEquals("The object entry spacing should be updated on the new instance.",
                newEntrySpacing, updatedSeparators.getObjectEntrySpacing());

        // 3. The original instance should remain unchanged (confirming immutability).
        assertEquals("The original instance must not be modified.",
                initialEntrySpacing, originalSeparators.getObjectEntrySpacing());

        // 4. All other properties of the new instance should be copied from the original.
        assertEquals(originalSeparators.getRootSeparator(), updatedSeparators.getRootSeparator());
        assertEquals(originalSeparators.getObjectFieldValueSeparator(), updatedSeparators.getObjectFieldValueSeparator());
        assertEquals(originalSeparators.getObjectFieldValueSpacing(), updatedSeparators.getObjectFieldValueSpacing());
        assertEquals(originalSeparators.getObjectEntrySeparator(), updatedSeparators.getObjectEntrySeparator());
        assertEquals(originalSeparators.getObjectEmptySeparator(), updatedSeparators.getObjectEmptySeparator());
        assertEquals(originalSeparators.getArrayValueSeparator(), updatedSeparators.getArrayValueSeparator());
        assertEquals(originalSeparators.getArrayValueSpacing(), updatedSeparators.getArrayValueSpacing());
        assertEquals(originalSeparators.getArrayEmptySeparator(), updatedSeparators.getArrayEmptySeparator());
    }
}