package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link Separators} class, focusing on its constructors.
 */
public class SeparatorsTest {

    /**
     * Tests that the three-argument constructor {@code Separators(char, char, char)}
     * correctly initializes the separator characters and applies the documented
     * default spacing rules.
     * <p>
     * The expected default spacing is:
     * <ul>
     *   <li>{@code Spacing.BOTH} for the object field-value separator.</li>
     *   <li>{@code Spacing.NONE} for the object entry separator.</li>
     *   <li>{@code Spacing.NONE} for the array value separator.</li>
     * </ul>
     */
    @Test
    public void constructorWithThreeCharsShouldSetSeparatorsAndDefaultSpacing() {
        // Arrange
        final char testSeparator = 'f';

        // Act
        Separators separators = new Separators(testSeparator, testSeparator, testSeparator);

        // Assert
        // 1. Verify Object Field-Value separator and its default spacing
        assertEquals("Object field-value separator should be set correctly.",
                testSeparator, separators.getObjectFieldValueSeparator());
        assertEquals("Object field-value spacing should default to BOTH.",
                Separators.Spacing.BOTH, separators.getObjectFieldValueSpacing());

        // 2. Verify Object Entry separator and its default spacing
        assertEquals("Object entry separator should be set correctly.",
                testSeparator, separators.getObjectEntrySeparator());
        assertEquals("Object entry spacing should default to NONE.",
                Separators.Spacing.NONE, separators.getObjectEntrySpacing());

        // 3. Verify Array Value separator and its default spacing
        assertEquals("Array value separator should be set correctly.",
                testSeparator, separators.getArrayValueSeparator());
        assertEquals("Array value spacing should default to NONE.",
                Separators.Spacing.NONE, separators.getArrayValueSpacing());
    }
}