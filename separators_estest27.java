package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link Separators} class, focusing on the behavior of its
 * fluent "with" methods.
 */
public class SeparatorsTest {

    /**
     * This test verifies an optimization in the {@code withObjectEntrySeparator} method.
     * When the method is called with the same separator character that the object
     * already has, it should return the same instance rather than creating a new one.
     * This is a common pattern in immutable, fluent-style builders.
     */
    @Test
    public void withObjectEntrySeparatorShouldReturnSameInstanceWhenSeparatorIsUnchanged() {
        // Arrange: Create a Separators instance with a specific object entry separator.
        final char existingSeparator = 'J';
        final Separators initialSeparators = new Separators(
            "",                      // rootSeparator
            'J',                     // objectFieldValueSeparator
            Separators.Spacing.NONE, // objectFieldValueSpacing
            existingSeparator,       // objectEntrySeparator (the character under test)
            Separators.Spacing.NONE, // objectEntrySpacing
            "",                      // objectEmptySeparator
            'J',                     // arrayValueSeparator
            Separators.Spacing.NONE, // arrayValueSpacing
            "2U#AD9"                 // arrayEmptySeparator
        );

        // Act: Call the method with the exact same separator character.
        Separators result = initialSeparators.withObjectEntrySeparator(existingSeparator);

        // Assert: The returned object should be the same instance as the original.
        assertSame("Expected the same instance when the separator is not changed",
                initialSeparators, result);
    }
}