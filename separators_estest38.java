package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link Separators} class, focusing on its factory methods and default state.
 */
public class SeparatorsTest {

    @Test
    public void createDefaultInstance_shouldReturnInstanceWithDefaultValues() {
        // Arrange: No specific setup is needed for this test.
        // The expected default values are defined by the Separators class constants and constructor.

        // Act: Create a Separators instance using the default factory method.
        Separators defaultSeparators = Separators.createDefaultInstance();

        // Assert: Verify that all properties of the instance match the documented defaults.
        assertNotNull("The default instance should not be null.", defaultSeparators);

        // Verify root-level separator
        assertEquals("Default root separator should be a single space.",
                " ", defaultSeparators.getRootSeparator());

        // Verify object-related separators and spacing
        assertEquals("Default object field-value separator should be a colon.",
                ':', defaultSeparators.getObjectFieldValueSeparator());
        assertEquals("Default spacing for object field-value should be BOTH.",
                Separators.Spacing.BOTH, defaultSeparators.getObjectFieldValueSpacing());
        assertEquals("Default object entry separator should be a comma.",
                ',', defaultSeparators.getObjectEntrySeparator());
        assertEquals("Default spacing for object entries should be NONE.",
                Separators.Spacing.NONE, defaultSeparators.getObjectEntrySpacing());
        assertEquals("Default separator for an empty object should be a single space.",
                " ", defaultSeparators.getObjectEmptySeparator());

        // Verify array-related separators and spacing
        assertEquals("Default array value separator should be a comma.",
                ',', defaultSeparators.getArrayValueSeparator());
        assertEquals("Default spacing for array values should be NONE.",
                Separators.Spacing.NONE, defaultSeparators.getArrayValueSpacing());
        assertEquals("Default separator for an empty array should be a single space.",
                " ", defaultSeparators.getArrayEmptySeparator());
    }
}