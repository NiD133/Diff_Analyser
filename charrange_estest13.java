package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CharRange} class.
 */
public class CharRangeTest {

    /**
     * Tests that the toString() method returns the correct string representation
     * for a standard character range, formatted as "start-end".
     */
    @Test
    public void toStringShouldReturnCorrectFormatForStandardRange() {
        // Arrange
        // Create a character range from '8' to 'A'.
        final CharRange range = CharRange.isIn('8', 'A');
        final String expectedString = "8-A";

        // Act
        final String actualString = range.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}