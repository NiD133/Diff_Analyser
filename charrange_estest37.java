package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link CharRange} class.
 * This class focuses on verifying the behavior of the factory methods.
 */
public class CharRangeTest {

    /**
     * Tests that the {@code CharRange.isIn(start, end)} factory method correctly creates
     * a non-negated range with the specified start and end characters when start < end.
     */
    @Test
    public void isIn_whenStartIsLessThanEnd_shouldCreateCorrectNonNegatedRange() {
        // Arrange
        final char startChar = '8';
        final char endChar = 'A'; // Note: ASCII('8') < ASCII('A')

        // Act
        final CharRange range = CharRange.isIn(startChar, endChar);

        // Assert
        // The factory method should produce a non-negated range.
        assertFalse("The range created by isIn() should not be negated.", range.isNegated());
        
        // The start and end characters should be correctly assigned.
        assertEquals("The start character should match the input.", startChar, range.getStart());
        assertEquals("The end character should match the input.", endChar, range.getEnd());
    }
}