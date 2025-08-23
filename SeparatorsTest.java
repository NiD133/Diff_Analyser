package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link Separators}.
 *
 * @date 2017-07-31
 * @see Separators
 **/
class SeparatorsTest {

    @Test
    void testWithArrayValueSeparator() {
        // Arrange: Create a Separators instance with initial separators
        Separators initialSeparators = new Separators('5', '5', '5');

        // Act & Assert: Verify that setting the same array value separator returns the same instance
        Separators sameSeparatorInstance = initialSeparators.withArrayValueSeparator('5');
        assertSame(initialSeparators, sameSeparatorInstance, "Expected the same instance when setting the same separator");
        assertEquals('5', sameSeparatorInstance.getArrayValueSeparator(), "Array value separator should remain '5'");

        // Act & Assert: Verify that setting a different array value separator returns a new instance
        Separators differentSeparatorInstance = initialSeparators.withArrayValueSeparator('6');
        assertNotSame(initialSeparators, differentSeparatorInstance, "Expected a new instance when setting a different separator");
        assertEquals('6', differentSeparatorInstance.getArrayValueSeparator(), "Array value separator should be updated to '6'");
    }

    @Test
    void testWithObjectEntrySeparator() {
        // Arrange: Create a Separators instance with initial separators
        Separators initialSeparators = new Separators('5', '5', '5');

        // Act: Change the object entry separator
        Separators updatedSeparators = initialSeparators.withObjectEntrySeparator('!');

        // Assert: Verify that the object entry separator is updated and other separators remain unchanged
        assertEquals('!', updatedSeparators.getObjectEntrySeparator(), "Object entry separator should be updated to '!'");
        assertEquals('5', updatedSeparators.getObjectFieldValueSeparator(), "Object field value separator should remain '5'");
        assertEquals('5', updatedSeparators.getArrayValueSeparator(), "Array value separator should remain '5'");
    }

    @Test
    void testWithObjectFieldValueSeparator() {
        // Arrange: Create a Separators instance with initial separators
        Separators initialSeparators = new Separators('5', '5', '5');

        // Act & Assert: Verify that setting the same object field value separator returns the same instance
        Separators sameSeparatorInstance = initialSeparators.withObjectFieldValueSeparator('5');
        assertSame(initialSeparators, sameSeparatorInstance, "Expected the same instance when setting the same separator");
        assertEquals('5', sameSeparatorInstance.getObjectFieldValueSeparator(), "Object field value separator should remain '5'");

        // Act & Assert: Verify that setting a different object field value separator returns a new instance
        Separators differentSeparatorInstance = initialSeparators.withObjectFieldValueSeparator('6');
        assertNotSame(initialSeparators, differentSeparatorInstance, "Expected a new instance when setting a different separator");
        assertEquals('6', differentSeparatorInstance.getObjectFieldValueSeparator(), "Object field value separator should be updated to '6'");
    }
}