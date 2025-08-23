package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    private static final int NOT_FOUND = -1;

    /**
     * Tests that lastIndexOf returns -1 when the character to be found does not exist
     * in the CharSequence.
     */
    @Test
    public void testLastIndexOfWithNonExistentCharShouldReturnNotFound() {
        // Arrange
        final CharSequence text = "', is either of typ Map.Entry nor an Array";
        // The character to search for (1203 is the Unicode value for the Greek letter lambda 'λ') is not in the text.
        final int nonExistentChar = 1203;
        final int startIndex = 36;

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(text, nonExistentChar, startIndex);

        // Assert
        assertEquals("Expected -1 for a character that is not found", NOT_FOUND, actualIndex);
    }
}