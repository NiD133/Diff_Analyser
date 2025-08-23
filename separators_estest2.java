package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class, focusing on its immutability
 * and wither methods.
 */
public class SeparatorsTest {

    /**
     * Verifies that the {@code withArrayValueSeparator} method correctly creates a new
     * Separators instance with the updated array value separator, while leaving the
     * original instance unchanged.
     */
    @Test
    public void withArrayValueSeparator_shouldCreateNewInstanceWithUpdatedSeparator() {
        // Arrange: Set up the initial object and the new value to be applied.
        Separators originalSeparators = new Separators();
        char newArraySeparator = '(';

        // Act: Call the method under test.
        Separators updatedSeparators = originalSeparators.withArrayValueSeparator(newArraySeparator);

        // Assert: Verify the behavior and outcomes.

        // 1. A new, distinct instance should be returned.
        assertNotSame("The 'with' method should return a new instance.", originalSeparators, updatedSeparators);

        // 2. The new instance should have the updated property.
        assertEquals("The array separator should be updated in the new instance.",
                newArraySeparator, updatedSeparators.getArrayValueSeparator());

        // 3. Other properties in the new instance should remain the same as the original's.
        assertEquals(originalSeparators.getObjectFieldValueSeparator(), updatedSeparators.getObjectFieldValueSeparator());
        assertEquals(originalSeparators.getObjectEntrySeparator(), updatedSeparators.getObjectEntrySeparator());
        assertEquals(originalSeparators.getObjectFieldValueSpacing(), updatedSeparators.getObjectFieldValueSpacing());
        assertEquals(originalSeparators.getObjectEntrySpacing(), updatedSeparators.getObjectEntrySpacing());
        assertEquals(originalSeparators.getArrayValueSpacing(), updatedSeparators.getArrayValueSpacing());

        // 4. The original instance should be unchanged, confirming immutability.
        char defaultArraySeparator = ',';
        assertEquals("The original instance's array separator should not change.",
                defaultArraySeparator, originalSeparators.getArrayValueSeparator());
    }
}