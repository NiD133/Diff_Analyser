package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Separators} class, focusing on its constructor and getters.
 */
public class SeparatorsTest {

    /**
     * Verifies that the canonical constructor correctly initializes all properties
     * of a Separators instance, and that the corresponding getter methods
     * return the values provided to the constructor.
     */
    @Test
    public void constructorShouldCorrectlyInitializeAllProperties() {
        // Arrange: Define the custom separator values for the test.
        // Using named constants makes the constructor call and assertions much clearer.
        final String expectedRootSeparator = "oRb)u^~$[*+b+7&Gu";
        final char expectedObjectFieldSeparator = 'D';
        final Separators.Spacing expectedSpacing = Separators.Spacing.AFTER;
        final char expectedObjectEntrySeparator = 'y';
        final String expectedObjectEmptySeparator = "oRb)u^~$[*+b+7&Gu";
        final char expectedArrayValueSeparator = '1';
        final String expectedArrayEmptySeparator = "com.fasterxml.jackson.core.util.Separators$Spacing";

        // Act: Create a Separators instance using the canonical constructor.
        Separators separators = new Separators(
                expectedRootSeparator,
                expectedObjectFieldSeparator,
                expectedSpacing, // for object field-value
                expectedObjectEntrySeparator,
                expectedSpacing, // for object entry
                expectedObjectEmptySeparator,
                expectedArrayValueSeparator,
                expectedSpacing, // for array value
                expectedArrayEmptySeparator
        );

        // Assert: Verify that each getter returns the value set in the constructor.
        // Assertions are grouped by concern (root, object, array) for readability.
        assertEquals("Root separator should match the constructor argument",
                expectedRootSeparator, separators.getRootSeparator());

        assertEquals("Object field-value separator should match the constructor argument",
                expectedObjectFieldSeparator, separators.getObjectFieldValueSeparator());
        assertEquals("Object field-value spacing should match the constructor argument",
                expectedSpacing, separators.getObjectFieldValueSpacing());
        assertEquals("Object entry separator should match the constructor argument",
                expectedObjectEntrySeparator, separators.getObjectEntrySeparator());
        assertEquals("Object entry spacing should match the constructor argument",
                expectedSpacing, separators.getObjectEntrySpacing());
        assertEquals("Object empty separator should match the constructor argument",
                expectedObjectEmptySeparator, separators.getObjectEmptySeparator());

        assertEquals("Array value separator should match the constructor argument",
                expectedArrayValueSeparator, separators.getArrayValueSeparator());
        assertEquals("Array value spacing should match the constructor argument",
                expectedSpacing, separators.getArrayValueSpacing());
        assertEquals("Array empty separator should match the constructor argument",
                expectedArrayEmptySeparator, separators.getArrayEmptySeparator());
    }
}