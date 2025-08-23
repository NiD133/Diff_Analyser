package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Separators} class, focusing on its construction and property accessors.
 */
public class SeparatorsTest {

    /**
     * Verifies that the canonical constructor correctly initializes all separator and spacing properties,
     * and that the corresponding getter methods return the values passed during construction.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define the distinct, non-default values to be used for construction.
        // Using descriptive variables instead of "magic values" improves readability.
        final String rootSeparator = "";
        final char objectFieldSeparator = 'j';
        final char objectEntrySeparator = 'j';
        final String objectEmptySeparator = "nCF";
        final char arrayValueSeparator = 'W';
        final String arrayEmptySeparator = "nCF";
        final Separators.Spacing spacing = Separators.Spacing.NONE;

        // Act: Create a new Separators instance using the canonical constructor.
        Separators separators = new Separators(
            rootSeparator,
            objectFieldSeparator, spacing,
            objectEntrySeparator, spacing,
            objectEmptySeparator,
            arrayValueSeparator, spacing,
            arrayEmptySeparator
        );

        // Assert: Verify that each getter returns the exact value provided to the constructor.
        // The assertions are grouped logically for better comprehension.
        
        // General
        assertEquals(rootSeparator, separators.getRootSeparator());

        // Object-related properties
        assertEquals(objectFieldSeparator, separators.getObjectFieldValueSeparator());
        assertEquals(objectEntrySeparator, separators.getObjectEntrySeparator());
        assertEquals(objectEmptySeparator, separators.getObjectEmptySeparator());
        assertEquals(spacing, separators.getObjectFieldValueSpacing());
        assertEquals(spacing, separators.getObjectEntrySpacing());

        // Array-related properties
        assertEquals(arrayValueSeparator, separators.getArrayValueSeparator());
        assertEquals(arrayEmptySeparator, separators.getArrayEmptySeparator());
        assertEquals(spacing, separators.getArrayValueSpacing());
    }
}