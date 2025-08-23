package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that checkIndexOf() returns -1 when both the string to search in and
     * the string to search for are null, regardless of the starting index.
     */
    @Test
    public void checkIndexOfShouldReturnMinusOneForNullInputStrings() {
        // Arrange
        final IOCase insensitiveSearch = IOCase.INSENSITIVE;
        final String stringToSearchIn = null;
        final String stringToSearchFor = null;
        final int irrelevantStartIndex = 6;

        // Act
        final int actualIndex = insensitiveSearch.checkIndexOf(stringToSearchIn, irrelevantStartIndex, stringToSearchFor);

        // Assert
        assertEquals("Expected -1 for null input strings", -1, actualIndex);
    }
}