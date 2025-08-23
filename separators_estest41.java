package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link Separators} class, focusing on its constructor and getters.
 */
public class SeparatorsTest {

    /**
     * Verifies that the canonical constructor correctly initializes all separator properties
     * and that the corresponding getter methods return the expected values.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define the expected separator and spacing values for clarity.
        final String expectedRootSeparator = "";
        final char expectedObjectFieldValueSeparator = ':';
        final Separators.Spacing expectedObjectFieldValueSpacing = Separators.Spacing.AFTER;
        final char expectedObjectEntrySeparator = ',';
        final Separators.Spacing expectedObjectEntrySpacing = Separators.Spacing.NONE;
        final String expectedObjectEmptySeparator = "";
        final char expectedArrayValueSeparator = ',';
        final Separators.Spacing expectedArrayValueSpacing = Separators.Spacing.BOTH;
        final String expectedArrayEmptySeparator = "";

        // Act: Create a Separators instance using the canonical constructor.
        Separators separators = new Separators(
                expectedRootSeparator,
                expectedObjectFieldValueSeparator,
                expectedObjectFieldValueSpacing,
                expectedObjectEntrySeparator,
                expectedObjectEntrySpacing,
                expectedObjectEmptySeparator,
                expectedArrayValueSeparator,
                expectedArrayValueSpacing,
                expectedArrayEmptySeparator
        );

        // Assert: Verify that each getter returns the value set in the constructor.
        assertEquals(expectedRootSeparator, separators.getRootSeparator());
        assertEquals(expectedObjectFieldValueSeparator, separators.getObjectFieldValueSeparator());
        assertEquals(expectedObjectFieldValueSpacing, separators.getObjectFieldValueSpacing());
        assertEquals(expectedObjectEntrySeparator, separators.getObjectEntrySeparator());
        assertEquals(expectedObjectEntrySpacing, separators.getObjectEntrySpacing());
        assertEquals(expectedObjectEmptySeparator, separators.getObjectEmptySeparator());
        assertEquals(expectedArrayValueSeparator, separators.getArrayValueSeparator());
        assertEquals(expectedArrayValueSpacing, separators.getArrayValueSpacing());
        assertEquals(expectedArrayEmptySeparator, separators.getArrayEmptySeparator());
    }
}