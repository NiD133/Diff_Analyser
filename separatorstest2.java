package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the immutability and modification logic of the Separators class.
 */
class SeparatorsTest {

    @Test
    @DisplayName("withObjectEntrySeparator should return a new instance when the separator is changed")
    void withObjectEntrySeparator_whenSeparatorIsDifferent_shouldReturnNewInstance() {
        // Arrange: Create an initial Separators instance with a known state.
        final char initialObjectEntrySep = ',';
        final char otherSep = ':';
        Separators originalSeparators = new Separators(otherSep, initialObjectEntrySep, otherSep);

        final char newObjectEntrySep = ';';

        // Act: Call the method with a different separator character.
        Separators modifiedSeparators = originalSeparators.withObjectEntrySeparator(newObjectEntrySep);

        // Assert: Verify that a new instance was created with the correct changes.
        assertNotSame(originalSeparators, modifiedSeparators,
                "A new instance should be returned when the separator is modified.");

        // Check that the target separator is updated in the new instance.
        assertEquals(newObjectEntrySep, modifiedSeparators.getObjectEntrySeparator());

        // Check that other separators remain unchanged in the new instance.
        assertEquals(otherSep, modifiedSeparators.getObjectFieldValueSeparator());
        assertEquals(otherSep, modifiedSeparators.getArrayValueSeparator());

        // Ensure the original instance remains unmodified.
        assertEquals(initialObjectEntrySep, originalSeparators.getObjectEntrySeparator());
    }

    @Test
    @DisplayName("withObjectEntrySeparator should return the same instance when the separator is unchanged")
    void withObjectEntrySeparator_whenSeparatorIsSame_shouldReturnSameInstance() {
        // Arrange: Create an initial Separators instance.
        final char objectEntrySep = ',';
        Separators originalSeparators = new Separators(':', objectEntrySep, ',');

        // Act: Call the method with the exact same separator character.
        Separators resultSeparators = originalSeparators.withObjectEntrySeparator(objectEntrySep);

        // Assert: Verify that the original instance is returned.
        assertSame(originalSeparators, resultSeparators,
                "The same instance should be returned if the separator is not changed (immutability optimization).");
    }
}