package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link Separators} class, focusing on its immutability
 * and fluent "with" methods.
 */
public class SeparatorsTest {

    /**
     * Tests that `withRootSeparator` creates a new instance with the specified
     * root separator while leaving the original instance unchanged.
     */
    @Test
    public void withRootSeparator_shouldReturnNewInstanceWithUpdatedValue() {
        // Arrange: Create a default Separators instance.
        final Separators originalSeparators = Separators.createDefaultInstance();
        final String newRootSeparator = null;

        // Act: Call the method under test to create a modified instance.
        final Separators modifiedSeparators = originalSeparators.withRootSeparator(newRootSeparator);

        // Assert: Verify the behavior.

        // 1. A new, distinct instance should be returned.
        assertNotSame("The 'with' method should return a new instance.",
                originalSeparators, modifiedSeparators);

        // 2. The new instance should have the updated root separator.
        assertNull("The new instance's root separator should be updated.",
                modifiedSeparators.getRootSeparator());

        // 3. The original instance must remain unchanged (verifying immutability).
        assertEquals("The original instance should be immutable.",
                Separators.DEFAULT_ROOT_VALUE_SEPARATOR, originalSeparators.getRootSeparator());

        // 4. All other properties should be copied from the original to the new instance.
        assertEquals(originalSeparators.getObjectFieldValueSeparator(), modifiedSeparators.getObjectFieldValueSeparator());
        assertEquals(originalSeparators.getObjectEntrySeparator(), modifiedSeparators.getObjectEntrySeparator());
        assertEquals(originalSeparators.getArrayValueSeparator(), modifiedSeparators.getArrayValueSeparator());
        assertEquals(originalSeparators.getObjectFieldValueSpacing(), modifiedSeparators.getObjectFieldValueSpacing());
        assertEquals(originalSeparators.getObjectEntrySpacing(), modifiedSeparators.getObjectEntrySpacing());
        assertEquals(originalSeparators.getArrayValueSpacing(), modifiedSeparators.getArrayValueSpacing());
    }
}