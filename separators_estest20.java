package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link Separators} class, focusing on its immutability
 * when creating new instances with updated values.
 */
public class SeparatorsTest {

    @Test
    public void withArrayEmptySeparator_shouldCreateNewInstanceWithUpdatedValue() {
        // Arrange: Create an initial Separators instance with known, non-default values.
        Separators originalSeparators = new Separators(
            " ",           // rootSeparator
            ':',           // objectFieldValueSeparator
            Separators.Spacing.BOTH, // objectFieldValueSpacing
            ',',           // objectEntrySeparator
            Separators.Spacing.NONE, // objectEntrySpacing
            "{}",          // objectEmptySeparator
            ',',           // arrayValueSeparator
            Separators.Spacing.NONE, // arrayValueSpacing
            "[]"           // arrayEmptySeparator (the value we will change)
        );
        String newArrayEmptySeparator = " ";

        // Act: Create a new instance by changing the array empty separator.
        Separators updatedSeparators = originalSeparators.withArrayEmptySeparator(newArrayEmptySeparator);

        // Assert: Verify the new instance is correct and the original is unchanged.

        // 1. A new, distinct instance should be returned (verifying immutability).
        assertNotSame("The 'with...' method should return a new instance.", originalSeparators, updatedSeparators);

        // 2. The specific property should be updated on the new instance.
        assertEquals("The array empty separator should be updated.", newArrayEmptySeparator, updatedSeparators.getArrayEmptySeparator());

        // 3. All other properties should be copied from the original instance without modification.
        assertEquals("Root separator should not change.", originalSeparators.getRootSeparator(), updatedSeparators.getRootSeparator());
        assertEquals("Object field value separator should not change.", originalSeparators.getObjectFieldValueSeparator(), updatedSeparators.getObjectFieldValueSeparator());
        assertEquals("Object field value spacing should not change.", originalSeparators.getObjectFieldValueSpacing(), updatedSeparators.getObjectFieldValueSpacing());
        assertEquals("Object entry separator should not change.", originalSeparators.getObjectEntrySeparator(), updatedSeparators.getObjectEntrySeparator());
        assertEquals("Object entry spacing should not change.", originalSeparators.getObjectEntrySpacing(), updatedSeparators.getObjectEntrySpacing());
        assertEquals("Object empty separator should not change.", originalSeparators.getObjectEmptySeparator(), updatedSeparators.getObjectEmptySeparator());
        assertEquals("Array value separator should not change.", originalSeparators.getArrayValueSeparator(), updatedSeparators.getArrayValueSeparator());
        assertEquals("Array value spacing should not change.", originalSeparators.getArrayValueSpacing(), updatedSeparators.getArrayValueSpacing());
    }
}