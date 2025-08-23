package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Separators} class.
 */
public class SeparatorsTest {

    /**
     * Verifies that the constructor correctly initializes all separator properties,
     * including using the default values for empty object/array separators which are
     * not specified in this deprecated constructor signature.
     */
    @Test
    public void constructorWithSpacingShouldSetSeparatorsAndUseDefaultsForEmpty() {
        // Arrange
        final String expectedRootSeparator = "IS:B2Ea";
        final char expectedObjectFieldValueSeparator = 'V';
        final char expectedObjectEntrySeparator = '9';
        final char expectedArrayValueSeparator = '9';
        final Separators.Spacing spacing = Separators.Spacing.AFTER;

        // Act
        // This test uses a deprecated constructor to ensure backward compatibility.
        Separators separators = new Separators(
            expectedRootSeparator,
            expectedObjectFieldValueSeparator, spacing,
            expectedObjectEntrySeparator, spacing,
            expectedArrayValueSeparator, spacing
        );

        // Assert
        // Check that all values, including the implicitly set defaults, are correct.
        assertEquals("Root separator should match the constructor argument",
            expectedRootSeparator, separators.getRootSeparator());

        assertEquals("Object field-value separator should match the constructor argument",
            expectedObjectFieldValueSeparator, separators.getObjectFieldValueSeparator());
        assertEquals("Object entry separator should match the constructor argument",
            expectedObjectEntrySeparator, separators.getObjectEntrySeparator());
        assertEquals("Array value separator should match the constructor argument",
            expectedArrayValueSeparator, separators.getArrayValueSeparator());

        // This constructor uses default values for empty separators.
        assertEquals("Empty object separator should be the default space",
            " ", separators.getObjectEmptySeparator());
        assertEquals("Empty array separator should be the default space",
            " ", separators.getArrayEmptySeparator());
    }
}