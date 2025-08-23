package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that checkIndexOf returns -1 when the starting search index is out of bounds.
     * This holds true even when searching for an empty string, which differs from
     * String.indexOf() behavior in some JDK versions.
     */
    @Test
    public void checkIndexOfShouldReturnNegativeOneForOutOfBoundsStartIndex() {
        // Arrange
        final IOCase ioCase = IOCase.SYSTEM;
        final String text = "some arbitrary text";
        final String searchString = "";
        // An index safely outside the bounds of the 'text' string.
        final int outOfBoundsIndex = text.length() + 1;

        // Act
        final int actualIndex = ioCase.checkIndexOf(text, outOfBoundsIndex, searchString);

        // Assert
        assertEquals("Expected -1 for an out-of-bounds start index", -1, actualIndex);
    }
}