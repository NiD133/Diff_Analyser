package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Separators} class.
 */
public class SeparatorsTest {

    /**
     * Verifies that the default instance of Separators is created with the
     * expected, conventional JSON separator characters and spacing settings.
     */
    @Test
    public void createDefaultInstance_shouldHaveCorrectDefaultValues() {
        // Arrange: Create a default Separators instance.
        Separators defaultSeparators = Separators.createDefaultInstance();

        // Assert: Verify all properties of the default instance.
        // The assertions are grouped by concern (object properties vs. array properties)
        // for better readability.

        // 1. Verify object-related separators and spacing
        assertEquals("Default object field/value separator should be a colon",
                ':', defaultSeparators.getObjectFieldValueSeparator());
        assertEquals("Default spacing for object field/value should be BOTH (e.g., \"key\" : \"value\")",
                Separators.Spacing.BOTH, defaultSeparators.getObjectFieldValueSpacing());
        assertEquals("Default object entry separator should be a comma",
                ',', defaultSeparators.getObjectEntrySeparator());
        assertEquals("Default spacing for object entries should be NONE (e.g., ...},{\"key...)",
                Separators.Spacing.NONE, defaultSeparators.getObjectEntrySpacing());

        // 2. Verify array-related separators and spacing
        assertEquals("Default array value separator should be a comma",
                ',', defaultSeparators.getArrayValueSeparator());
        assertEquals("Default spacing for array values should be NONE (e.g., [1,2,3])",
                Separators.Spacing.NONE, defaultSeparators.getArrayValueSpacing());
    }
}