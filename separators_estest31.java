package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link Separators} class, focusing on its immutability and
 * fluent API design.
 */
public class SeparatorsTest {

    /**
     * Verifies that calling a 'with' method (e.g., {@link Separators#withObjectFieldValueSeparator(char)})
     * with the same value that the object already possesses returns the original instance.
     * This is a common and important optimization for immutable objects to avoid
     * unnecessary object creation.
     */
    @Test
    public void withObjectFieldValueSeparator_shouldReturnSameInstance_whenSeparatorIsUnchanged() {
        // Arrange
        final char separatorChar = 'J';
        final Separators.Spacing noSpacing = Separators.Spacing.NONE;

        // Create a Separators instance with a specific, non-default configuration.
        final Separators originalSeparators = new Separators(
            /* rootSeparator */ "",
            /* objectFieldValueSeparator */ separatorChar,
            /* objectFieldValueSpacing */ noSpacing,
            /* objectEntrySeparator */ 'J',
            /* objectEntrySpacing */ noSpacing,
            /* objectEmptySeparator */ "",
            /* arrayValueSeparator */ 'J',
            /* arrayValueSpacing */ noSpacing,
            /* arrayEmptySeparator */ "2U#AD9"
        );

        // Act
        // Call the 'with' method using the same separator character that is already set.
        Separators resultSeparators = originalSeparators.withObjectFieldValueSeparator(separatorChar);

        // Assert
        // The primary assertion is that the method returns the exact same instance, not a new one.
        assertSame("Expected the same instance when the separator value is not changed",
                     originalSeparators, resultSeparators);

        // Additionally, verify that other properties remain unchanged to ensure no side effects.
        assertEquals("Root separator should be unchanged", "", resultSeparators.getRootSeparator());
        assertEquals("Array empty separator should be unchanged", "2U#AD9", resultSeparators.getArrayEmptySeparator());
        assertEquals("Array value separator should be unchanged", 'J', resultSeparators.getArrayValueSeparator());
        assertEquals("Object empty separator should be unchanged", "", resultSeparators.getObjectEmptySeparator());
        assertEquals("Object entry separator should be unchanged", 'J', resultSeparators.getObjectEntrySeparator());
    }
}