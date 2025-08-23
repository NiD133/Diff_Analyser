package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class.
 *
 * This class has been refactored from a machine-generated test file
 * to improve clarity and maintainability.
 */
public class SeparatorsTest {

    /**
     * Verifies that the three-argument constructor {@code Separators(char, char, char)}
     * correctly assigns the specified separator characters and applies the documented
     * default spacing rules.
     */
    @Test
    public void constructorWithCustomSeparatorsShouldSetDefaultSpacing() {
        // Arrange: Define a custom separator character to test assignment.
        final char customSeparator = '(';

        // Act: Create a Separators instance using the constructor under test.
        Separators separators = new Separators(customSeparator, customSeparator, customSeparator);

        // Assert: Verify the state of the newly created object.

        // 1. Confirm that the custom separator characters were assigned correctly.
        assertEquals("Object field value separator should be set by the constructor.",
                customSeparator, separators.getObjectFieldValueSeparator());
        assertEquals("Object entry separator should be set by the constructor.",
                customSeparator, separators.getObjectEntrySeparator());
        assertEquals("Array value separator should be set by the constructor.",
                customSeparator, separators.getArrayValueSeparator());

        // 2. Confirm that spacing is set to the documented defaults for this constructor.
        // The contract is to use Spacing.BOTH for object field values and Spacing.NONE for others.
        assertEquals("Default spacing for object field values should be BOTH.",
                Separators.Spacing.BOTH, separators.getObjectFieldValueSpacing());
        assertEquals("Default spacing for object entries should be NONE.",
                Separators.Spacing.NONE, separators.getObjectEntrySpacing());
        assertEquals("Default spacing for array values should be NONE.",
                Separators.Spacing.NONE, separators.getArrayValueSpacing());
    }
}