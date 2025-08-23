package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class, focusing on its constructor and getters.
 */
public class SeparatorsTest {

    /**
     * Tests that the constructor correctly initializes the separator characters
     * and that the corresponding getter methods return the expected values.
     */
    @Test
    public void gettersShouldReturnValuesSetByConstructor() {
        // Arrange: Define meaningful and conventional separator values.
        final String rootSeparator = " ";
        final char objectFieldValueSeparator = ':';
        final char objectEntrySeparator = ',';
        final char arrayValueSeparator = ';';
        final Separators.Spacing spacing = Separators.Spacing.AFTER;

        // Act: Create a Separators instance using the constructor under test.
        Separators separators = new Separators(
            rootSeparator,
            objectFieldValueSeparator, spacing,
            objectEntrySeparator, spacing,
            arrayValueSeparator, spacing
        );

        // Assert: Verify that each getter returns the value provided to the constructor.
        assertEquals("Object field-value separator should match constructor argument",
            objectFieldValueSeparator, separators.getObjectFieldValueSeparator());

        assertEquals("Object entry separator should match constructor argument",
            objectEntrySeparator, separators.getObjectEntrySeparator());

        assertEquals("Array value separator should match constructor argument",
            arrayValueSeparator, separators.getArrayValueSeparator());
    }
}