package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class contains tests for the Separators class.
 * This specific test focuses on the immutability optimization of the 'with' methods.
 */
public class Separators_ESTestTest29 extends Separators_ESTest_scaffolding {

    /**
     * Verifies that calling withObjectFieldValueSpacing() with the existing value
     * returns the same instance, as an optimization for immutable objects.
     */
    @Test
    public void withObjectFieldValueSpacing_whenSpacingIsUnchanged_shouldReturnSameInstance() {
        // Arrange: Create a default Separators instance.
        // By default, it uses Spacing.BOTH for object field values.
        Separators initialSeparators = Separators.createDefaultInstance();
        Separators.Spacing existingSpacing = Separators.Spacing.BOTH;

        // Pre-condition check: Verify our assumption about the default state.
        assertEquals("Pre-condition failed: Default spacing is not as expected.",
                existingSpacing, initialSeparators.getObjectFieldValueSpacing());

        // Act: Call the 'with' method using the value it already has.
        Separators resultSeparators = initialSeparators.withObjectFieldValueSpacing(existingSpacing);

        // Assert: The method should return the exact same instance, not a new one.
        assertSame("Expected the same instance to be returned when the spacing value is not changed.",
                initialSeparators, resultSeparators);
    }
}