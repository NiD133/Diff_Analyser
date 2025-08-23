package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Separators} class, focusing on its immutable "with" methods.
 */
public class SeparatorsTest {

    /**
     * Tests that withObjectFieldValueSeparator() creates a new instance
     * with the updated separator, while preserving all other properties.
     */
    @Test
    public void withObjectFieldValueSeparator_should_returnNewInstanceWithUpdatedSeparator() {
        // Arrange: Create a default Separators instance.
        Separators defaultSeparators = Separators.createDefaultInstance();
        char newSeparator = '8';

        // Act: Call the method under test to create a modified instance.
        Separators modifiedSeparators = defaultSeparators.withObjectFieldValueSeparator(newSeparator);

        // Assert: Verify the new instance has the correct state.

        // 1. A new instance should be returned (verifying immutability).
        assertNotSame("A new instance should be created", defaultSeparators, modifiedSeparators);

        // 2. The specific property should be updated.
        assertEquals("The object field value separator should be updated",
                newSeparator, modifiedSeparators.getObjectFieldValueSeparator());

        // 3. The original instance should remain unchanged.
        assertEquals("The original instance should not be mutated",
                ':', defaultSeparators.getObjectFieldValueSeparator());

        // 4. All other properties on the new instance should be preserved from the original.
        assertEquals("Object entry separator should be preserved",
                defaultSeparators.getObjectEntrySeparator(), modifiedSeparators.getObjectEntrySeparator());
        assertEquals("Array value separator should be preserved",
                defaultSeparators.getArrayValueSeparator(), modifiedSeparators.getArrayValueSeparator());
        assertEquals("Object field value spacing should be preserved",
                defaultSeparators.getObjectFieldValueSpacing(), modifiedSeparators.getObjectFieldValueSpacing());
        assertEquals("Object entry spacing should be preserved",
                defaultSeparators.getObjectEntrySpacing(), modifiedSeparators.getObjectEntrySpacing());
        assertEquals("Array value spacing should be preserved",
                defaultSeparators.getArrayValueSpacing(), modifiedSeparators.getArrayValueSpacing());
    }
}