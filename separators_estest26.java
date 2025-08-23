package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class.
 */
public class SeparatorsTest {

    /**
     * Tests that calling a 'with...' method with the current value
     * returns the same instance, avoiding unnecessary object allocation.
     */
    @Test
    public void withObjectEntrySpacing_shouldReturnSameInstance_whenSpacingIsUnchanged() {
        // Arrange: Create a default Separators instance.
        // By default, object entry spacing is Spacing.NONE.
        Separators initialSeparators = Separators.createDefaultInstance();
        Separators.Spacing currentSpacing = Separators.Spacing.NONE;

        // Act: Call the method with the same spacing value that the instance already has.
        Separators resultSeparators = initialSeparators.withObjectEntrySpacing(currentSpacing);

        // Assert: The method should return the original instance for efficiency,
        // as no change was needed.
        assertSame("Expected the same instance when the spacing value is not changed",
                initialSeparators, resultSeparators);

        // Verify that other properties remain unchanged as a sanity check.
        assertEquals(',', resultSeparators.getObjectEntrySeparator());
        assertEquals(':', resultSeparators.getObjectFieldValueSeparator());
        assertEquals(Separators.Spacing.BOTH, resultSeparators.getObjectFieldValueSpacing());
        assertEquals(',', resultSeparators.getArrayValueSeparator());
        assertEquals(Separators.Spacing.NONE, resultSeparators.getArrayValueSpacing());
    }
}