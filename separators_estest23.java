package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Separators} class, focusing on its immutability and optimization.
 */
public class SeparatorsTest {

    /**
     * Tests that calling a 'with' method with the same value that is already set
     * returns the original instance, avoiding unnecessary object creation.
     */
    @Test
    public void withArrayValueSeparator_shouldReturnSameInstance_whenSeparatorIsUnchanged() {
        // Arrange: Create a Separators instance with a specific array value separator.
        final char arraySeparator = ',';
        final Separators originalSeparators = new Separators()
                .withArrayValueSeparator(arraySeparator);

        // Act: Call the 'with' method using the exact same separator character.
        Separators resultSeparators = originalSeparators.withArrayValueSeparator(arraySeparator);

        // Assert: The method should return the identical instance, not a new one.
        // This is an important optimization for immutable objects.
        assertSame("Expected the same instance when the value is not changed",
                originalSeparators, resultSeparators);
        
        // Verify that the property itself is correct, as a sanity check.
        assertEquals(arraySeparator, resultSeparators.getArrayValueSeparator());
    }
}