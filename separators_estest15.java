package com.fasterxml.jackson.core.util;

import org.junit.Test;
import com.fasterxml.jackson.core.util.Separators.Spacing;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Separators} class, focusing on its constructors and initial state.
 */
public class SeparatorsTest {

    /**
     * Verifies that the three-argument constructor {@code Separators(char, char, char)}
     * correctly initializes the separator characters while applying the documented default spacing.
     * <p>
     * According to the source, this constructor should default to:
     * <ul>
     *   <li>{@code Spacing.BOTH} for the object field-value separator.</li>
     *   <li>{@code Spacing.NONE} for the object entry separator.</li>
     *   <li>{@code Spacing.NONE} for the array value separator.</li>
     * </ul>
     */
    @Test
    public void whenConstructedWithThreeChars_thenSetsSeparatorsAndDefaultSpacing() {
        // Arrange: Define the separator characters to be used for construction.
        final char objectFieldValueSeparator = 'J';
        final char objectEntrySeparator = 'J';
        final char arrayValueSeparator = 'N';

        // Act: Create a new Separators instance using the three-argument constructor.
        Separators separators = new Separators(objectFieldValueSeparator, objectEntrySeparator, arrayValueSeparator);

        // Assert: Check that all separators and their corresponding spacing are set as expected.
        // 1. Verify object-related separators and their default spacing.
        assertEquals("Object field-value separator should match constructor argument",
                objectFieldValueSeparator, separators.getObjectFieldValueSeparator());
        assertEquals("Object field-value spacing should default to BOTH",
                Spacing.BOTH, separators.getObjectFieldValueSpacing());

        assertEquals("Object entry separator should match constructor argument",
                objectEntrySeparator, separators.getObjectEntrySeparator());
        assertEquals("Object entry spacing should default to NONE",
                Spacing.NONE, separators.getObjectEntrySpacing());

        // 2. Verify array-related separator and its default spacing.
        assertEquals("Array value separator should match constructor argument",
                arrayValueSeparator, separators.getArrayValueSeparator());
        assertEquals("Array value spacing should default to NONE",
                Spacing.NONE, separators.getArrayValueSpacing());
    }
}