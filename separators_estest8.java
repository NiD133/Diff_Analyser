package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Separators} class.
 */
public class SeparatorsTest {

    /**
     * Verifies that the canonical constructor correctly initializes all properties
     * and that the corresponding getters return the expected values.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define the expected values for the Separators object.
        final String expectedRootSeparator = "";
        final char expectedObjectFieldValueSeparator = ':';
        final Separators.Spacing expectedObjectFieldValueSpacing = Separators.Spacing.BOTH;
        final char expectedObjectEntrySeparator = ',';
        final Separators.Spacing expectedObjectEntrySpacing = Separators.Spacing.NONE;
        final String expectedObjectEmptySeparator = " ";
        final char expectedArrayValueSeparator = ',';
        final Separators.Spacing expectedArrayValueSpacing = Separators.Spacing.AFTER;
        final String expectedArrayEmptySeparator = " ";

        // Act: Create a new Separators instance using the canonical constructor.
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
        // The order of assertions matches the constructor parameter order for clarity.
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