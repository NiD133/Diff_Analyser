package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Separators} class.
 */
public class SeparatorsTest {

    /**
     * Verifies that creating a default Separators instance sets the expected
     * separator characters and spacing configurations.
     */
    @Test
    public void createDefaultInstance_shouldHaveExpectedDefaultValues() {
        // Arrange & Act: Create a default instance of Separators.
        Separators defaultSeparators = Separators.createDefaultInstance();

        // Assert: Verify that all properties match the documented defaults.
        
        // Check object-related separators
        assertEquals("Default object field-value separator should be a colon",
                ':', defaultSeparators.getObjectFieldValueSeparator());
        assertEquals("Default object field-value spacing should be BOTH",
                Separators.Spacing.BOTH, defaultSeparators.getObjectFieldValueSpacing());
        
        assertEquals("Default object entry separator should be a comma",
                ',', defaultSeparators.getObjectEntrySeparator());
        assertEquals("Default object entry spacing should be NONE",
                Separators.Spacing.NONE, defaultSeparators.getObjectEntrySpacing());

        // Check array-related separators
        assertEquals("Default array value separator should be a comma",
                ',', defaultSeparators.getArrayValueSeparator());
        assertEquals("Default array value spacing should be NONE",
                Separators.Spacing.NONE, defaultSeparators.getArrayValueSpacing());
    }
}