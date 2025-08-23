package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link Separators} class, focusing on its constructor and property accessors.
 */
public class SeparatorsTest {

    /**
     * Verifies that the canonical constructor correctly initializes all properties
     * of the Separators object, and that the corresponding getter methods return
     * the expected values.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define a set of custom separator values for the test.
        final String rootSeparator = "";
        final String objectEmptySeparator = "";
        final String arrayEmptySeparator = "[]"; // Using a more semantic value
        final char customSeparatorChar = '|';    // Using a more distinct character
        final Separators.Spacing noSpacing = Separators.Spacing.NONE;

        // Act: Create a Separators instance using its full constructor.
        Separators separators = new Separators(
                rootSeparator,
                customSeparatorChar,    // objectFieldValueSeparator
                noSpacing,              // objectFieldValueSpacing
                customSeparatorChar,    // objectEntrySeparator
                noSpacing,              // objectEntrySpacing
                objectEmptySeparator,
                customSeparatorChar,    // arrayValueSeparator
                noSpacing,              // arrayValueSpacing
                arrayEmptySeparator
        );

        // Assert: Verify that each getter returns the value set in the constructor.
        // Group assertions by functionality for clarity.

        // Root separator
        assertEquals("The root separator should match the constructor argument",
                rootSeparator, separators.getRootSeparator());

        // Object-related separators
        assertEquals("The object field-value separator char should match",
                customSeparatorChar, separators.getObjectFieldValueSeparator());
        assertEquals("The object field-value spacing should match",
                noSpacing, separators.getObjectFieldValueSpacing());
        assertEquals("The object entry separator char should match",
                customSeparatorChar, separators.getObjectEntrySeparator());
        assertEquals("The object entry spacing should match",
                noSpacing, separators.getObjectEntrySpacing());
        assertEquals("The empty object separator string should match",
                objectEmptySeparator, separators.getObjectEmptySeparator());

        // Array-related separators
        assertEquals("The array value separator char should match",
                customSeparatorChar, separators.getArrayValueSeparator());
        assertEquals("The array value spacing should match",
                noSpacing, separators.getArrayValueSpacing());
        assertEquals("The empty array separator string should match",
                arrayEmptySeparator, separators.getArrayEmptySeparator());
    }
}