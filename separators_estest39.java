package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class.
 */
public class SeparatorsTest {

    /**
     * Verifies that the canonical constructor correctly initializes all properties
     * of the Separators instance and that the corresponding getter methods
     * return the expected values.
     */
    @Test
    public void shouldCorrectlyInitializeSeparatorsViaConstructor() {
        // Arrange: Define clear, named variables for the constructor arguments.
        final String rootSeparator = "";
        final char objectFieldValueSeparator = ':';
        final Separators.Spacing spacing = Separators.Spacing.NONE;
        final char objectEntrySeparator = ',';
        final String objectEmptySeparator = "";
        final char arrayValueSeparator = ',';
        final String arrayEmptySeparator = "[]";

        // Act: Create a Separators instance using the canonical constructor.
        Separators separators = new Separators(
            rootSeparator,
            objectFieldValueSeparator,
            spacing, // for object field value
            objectEntrySeparator,
            spacing, // for object entry
            objectEmptySeparator,
            arrayValueSeparator,
            spacing, // for array value
            arrayEmptySeparator
        );

        // Assert: Verify that each property was set correctly.
        // Group assertions by related functionality for better readability.

        // Root separator
        assertEquals("Root separator should match", rootSeparator, separators.getRootSeparator());

        // Object-related separators
        assertEquals("Object field value separator should match", objectFieldValueSeparator, separators.getObjectFieldValueSeparator());
        assertEquals("Object field value spacing should match", spacing, separators.getObjectFieldValueSpacing());
        assertEquals("Object entry separator should match", objectEntrySeparator, separators.getObjectEntrySeparator());
        assertEquals("Object entry spacing should match", spacing, separators.getObjectEntrySpacing());
        assertEquals("Object empty separator should match", objectEmptySeparator, separators.getObjectEmptySeparator());

        // Array-related separators
        assertEquals("Array value separator should match", arrayValueSeparator, separators.getArrayValueSeparator());
        assertEquals("Array value spacing should match", spacing, separators.getArrayValueSpacing());
        assertEquals("Array empty separator should match", arrayEmptySeparator, separators.getArrayEmptySeparator());
    }
}