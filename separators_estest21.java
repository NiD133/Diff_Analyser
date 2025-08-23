package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Separators} class.
 */
public class SeparatorsTest {

    /**
     * Verifies that calling {@link Separators#withArrayValueSpacing(Separators.Spacing)}
     * with the current spacing value returns the original instance, avoiding
     * unnecessary object creation.
     */
    @Test
    public void withArrayValueSpacing_WhenSpacingIsUnchanged_ShouldReturnSameInstance() {
        // Arrange: Create a Separators instance with a specific array value spacing.
        final Separators.Spacing initialSpacing = Separators.Spacing.AFTER;
        final Separators initialSeparators = new Separators()
                .withArrayValueSpacing(initialSpacing);

        // Act: Call the 'with' method again, providing the same spacing value.
        Separators resultSeparators = initialSeparators.withArrayValueSpacing(initialSpacing);

        // Assert: The method should return the exact same instance, not a new one.
        assertSame("Expected the same instance when the spacing value is not changed",
                initialSeparators, resultSeparators);
    }
}