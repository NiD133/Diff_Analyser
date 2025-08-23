package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DefaultSplitCharacter} class.
 *
 * Note: The original test class name "DefaultSplitCharacter_ESTestTest6" and its
 * inheritance from a scaffolding class are artifacts of a test generation tool.
 * In a real-world scenario, this would be part of a single, comprehensive
 * "DefaultSplitCharacterTest" class.
 */
public class DefaultSplitCharacter_ESTestTest6 {

    /**
     * Verifies that getCurrentCharacter correctly retrieves the character from a
     * character array at a specified index when the PdfChunk array is null.
     * This tests the method's basic functionality.
     */
    @Test
    public void getCurrentCharacter_withNullChunkArray_returnsCharacterAtIndex() {
        // Arrange
        // The specific split character configuration is irrelevant for this method,
        // so we use the default constructor to keep the setup simple.
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        
        char[] textChars = {'H', 'e', 'l', 'l', 'o'};
        int targetIndex = 2; // The index of the character to retrieve ('l')
        char expectedChar = 'l';

        // Act
        // The third argument (PdfChunk[]) is null, which triggers the simplest code path.
        char actualChar = splitCharacter.getCurrentCharacter(targetIndex, textChars, null);

        // Assert
        assertEquals("The method should return the character at the specified index.", expectedChar, actualChar);
    }
}