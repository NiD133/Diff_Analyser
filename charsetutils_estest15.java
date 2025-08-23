package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that CharSetUtils.delete() correctly removes all characters
     * specified by a character range set (e.g., "A-Z").
     */
    @Test
    public void testDeleteWithCharacterRangeSet() {
        // Arrange
        // The input string contains characters that are part of the range, plus a hyphen.
        final String inputString = "A-Z";

        // The set "A-Z" is a special syntax representing all uppercase letters from A to Z.
        final String[] charsToDelete = {"A-Z"};

        final String expectedResult = "-";

        // Act
        final String actualResult = CharSetUtils.delete(inputString, charsToDelete);

        // Assert
        // The method should delete 'A' and 'Z' from the input string because they
        // fall within the "A-Z" range, leaving only the hyphen.
        assertEquals(expectedResult, actualResult);
    }
}