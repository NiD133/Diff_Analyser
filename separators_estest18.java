package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Separators} class, focusing on its construction and default state.
 */
public class SeparatorsTest {

    /**
     * Verifies that creating a {@link Separators} instance with its default constructor
     * correctly initializes all properties to their expected default values.
     */
    @Test
    public void shouldInitializeWithDefaultValuesOnDefaultConstruction() {
        // Arrange: Create a Separators instance using the default constructor.
        Separators separators = new Separators();

        // Act: No action is needed, as we are testing the state immediately after construction.

        // Assert: Verify that all properties match the documented defaults.
        // Assertions are grouped by functionality for improved readability.

        // --- Object Separators ---
        assertEquals(':', separators.getObjectFieldValueSeparator());
        assertEquals(Separators.Spacing.BOTH, separators.getObjectFieldValueSpacing());
        assertEquals(',', separators.getObjectEntrySeparator());
        assertEquals(Separators.Spacing.NONE, separators.getObjectEntrySpacing());
        assertEquals(" ", separators.getObjectEmptySeparator());

        // --- Array Separators ---
        assertEquals(',', separators.getArrayValueSeparator());
        assertEquals(Separators.Spacing.NONE, separators.getArrayValueSpacing());
        assertEquals(" ", separators.getArrayEmptySeparator());

        // --- Root Separator ---
        assertEquals(" ", separators.getRootSeparator());
    }
}