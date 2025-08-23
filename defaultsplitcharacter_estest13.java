package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the DefaultSplitCharacter class.
 */
public class DefaultSplitCharacterTest {

    /**
     * Verifies that isSplitCharacter returns true when the character being checked
     * was explicitly provided as a custom split character in the constructor.
     */
    @Test
    public void isSplitCharacter_whenUsingCustomSplitChar_returnsTrueForThatChar() {
        // Arrange
        // 1. Define the text to be analyzed for splitting.
        char[] text = "word-another".toCharArray();
        int indexOfHyphen = 4; // The position of the hyphen '-'.

        // 2. Create a DefaultSplitCharacter instance configured to treat a hyphen
        // as a custom split character.
        SplitCharacter customSplitCharacter = new DefaultSplitCharacter('-');

        // 3. The isSplitCharacter method requires an array of PdfChunk objects.
        // For this test, its content is not relevant, so we create a minimal dummy array.
        PdfChunk[] dummyChunks = {new PdfChunk(new Chunk(""), null)};

        // 4. Define the bounds of the text segment being analyzed.
        int textStart = 0;
        int textEnd = text.length - 1;

        // Act
        // Check if the character at the specified index is a split character.
        boolean isSplit = customSplitCharacter.isSplitCharacter(textStart, indexOfHyphen, textEnd, text, dummyChunks);

        // Assert
        // The method should return true because the character at the index is a hyphen,
        // which we explicitly defined as a custom split character.
        assertTrue("A character should be identified as a split character if it was provided to the constructor.", isSplit);
    }
}