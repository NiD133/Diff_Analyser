package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that {@code checkIndexOf} with {@code IOCase.INSENSITIVE}
     * finds a substring even when the casing does not match.
     */
    @Test
    public void checkIndexOfWithInsensitiveShouldFindSubstringWithDifferentCase() {
        // Arrange
        final String text = "Apache Commons IO";
        final String searchString = "commons"; // Substring with different case
        final int startIndex = 0;
        final int expectedIndex = 7;

        // Act
        final int actualIndex = IOCase.INSENSITIVE.checkIndexOf(text, startIndex, searchString);

        // Assert
        assertEquals("Expected to find the substring at index 7, ignoring case.",
                     expectedIndex, actualIndex);
    }
}