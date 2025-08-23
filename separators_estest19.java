package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link Separators} class.
 */
public class SeparatorsTest {

    /**
     * Verifies that the `with...` methods in the immutable Separators class
     * return the same object instance if the new value is identical to the
     * existing one. This is an important optimization to avoid unnecessary
     * object allocations.
     */
    @Test
    public void withArrayEmptySeparator_whenSeparatorIsUnchanged_shouldReturnSameInstance() {
        // Arrange: Create a Separators instance with a specific value (null)
        // for the array empty separator.
        final String currentArrayEmptySeparator = null;
        Separators initialSeparators = new Separators(
            /* rootSeparator */ null,
            /* objectFieldValueSeparator */ '6',
            /* objectFieldValueSpacing */ Separators.Spacing.NONE,
            /* objectEntrySeparator */ 'E',
            /* objectEntrySpacing */ Separators.Spacing.NONE,
            /* objectEmptySeparator */ null,
            /* arrayValueSeparator */ '3',
            /* arrayValueSpacing */ Separators.Spacing.NONE,
            /* arrayEmptySeparator */ currentArrayEmptySeparator
        );

        // Act: Call the corresponding `with...` method, passing the same value.
        Separators result = initialSeparators.withArrayEmptySeparator(currentArrayEmptySeparator);

        // Assert: The method should return the original instance, not a new one.
        assertSame(
            "Expected the same instance when the separator value is not changed",
            initialSeparators,
            result
        );

        // Sanity check: Ensure other properties were not accidentally modified.
        assertEquals('6', result.getObjectFieldValueSeparator());
        assertEquals('E', result.getObjectEntrySeparator());
        assertEquals('3', result.getArrayValueSeparator());
    }
}