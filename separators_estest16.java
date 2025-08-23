package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link Separators} class.
 */
public class SeparatorsTest {

    /**
     * Tests that the constructor correctly assigns a null value to the arrayEmptySeparator
     * and that the corresponding getter returns it.
     */
    @Test
    public void shouldReturnNullForArrayEmptySeparatorWhenConstructedWithNull() {
        // Arrange: Define arguments for the Separators constructor.
        // The key value for this test is setting arrayEmptySeparator to null.
        final String rootSeparator = null;
        final char separatorChar = 'A';
        final Separators.Spacing spacing = Separators.Spacing.BOTH;
        final String objectEmptySeparator = null;
        final String arrayEmptySeparator = null; // This is the specific value under test.

        Separators separators = new Separators(
            rootSeparator,
            separatorChar, spacing, // for object field-value
            separatorChar, spacing, // for object entry
            objectEmptySeparator,
            separatorChar, spacing, // for array value
            arrayEmptySeparator
        );

        // Act: Retrieve the value using the getter.
        String actualArrayEmptySeparator = separators.getArrayEmptySeparator();

        // Assert: Verify that the getter returns the expected null value.
        assertNull("The array empty separator should be null as provided in the constructor.", actualArrayEmptySeparator);

        // Sanity checks to ensure other properties were also set correctly.
        assertEquals("Object entry separator should be set correctly.", separatorChar, separators.getObjectEntrySeparator());
        assertEquals("Object field-value separator should be set correctly.", separatorChar, separators.getObjectFieldValueSeparator());
        assertEquals("Array value separator should be set correctly.", separatorChar, separators.getArrayValueSeparator());
    }
}