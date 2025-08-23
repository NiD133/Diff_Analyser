package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Separators} class, focusing on its default state.
 */
public class SeparatorsTest {

    /**
     * Verifies that the default no-argument constructor correctly initializes
     * all separator characters and spacing settings to their expected default values.
     */
    @Test
    public void defaultConstructorShouldSetDefaultSeparatorsAndSpacing() {
        // Arrange: Create a Separators instance using the default constructor.
        Separators separators = new Separators();

        // Act: The constructor is the action being tested.

        // Assert: Check that the instance has the expected default values.
        // These defaults are defined by the constructor chain:
        // new Separators() -> new Separators(':', ',', ',')

        // Verify object-related separators and spacing
        assertEquals("Default object field/value separator should be a colon",
                ':', separators.getObjectFieldValueSeparator());
        assertEquals("Default spacing for object field/value should be BOTH",
                Separators.Spacing.BOTH, separators.getObjectFieldValueSpacing());

        assertEquals("Default object entry separator should be a comma",
                ',', separators.getObjectEntrySeparator());
        assertEquals("Default spacing for object entries should be NONE",
                Separators.Spacing.NONE, separators.getObjectEntrySpacing());

        // Verify array-related separators and spacing
        assertEquals("Default array value separator should be a comma",
                ',', separators.getArrayValueSeparator());
        assertEquals("Default spacing for array values should be NONE",
                Separators.Spacing.NONE, separators.getArrayValueSpacing());
    }
}