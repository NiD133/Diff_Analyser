package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link Separators} class, focusing on its immutable "with-er" methods.
 */
public class SeparatorsTest {

    /**
     * Verifies that calling {@link Separators#withObjectEmptySeparator(String)} with the
     * current value does not create a new object but returns the same instance.
     * This behavior is an important optimization for immutable objects.
     */
    @Test
    public void withObjectEmptySeparator_whenSeparatorIsUnchanged_shouldReturnSameInstance() {
        // Arrange: Create a Separators instance with a specific object empty separator.
        final String initialEmptySeparator = " ";
        final Separators initialSeparators = new Separators()
                .withObjectEmptySeparator(initialEmptySeparator);

        // Act: Call the 'with' method using the exact same value.
        Separators resultSeparators = initialSeparators.withObjectEmptySeparator(initialEmptySeparator);

        // Assert: The returned object should be the same instance as the original.
        assertSame("Expected the same instance to be returned when the separator value is not changed.",
                initialSeparators, resultSeparators);
    }
}