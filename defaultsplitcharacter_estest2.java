package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link DefaultSplitCharacter} class.
 */
public class DefaultSplitCharacterTest {

    /**
     * Verifies that the default split character logic identifies characters with
     * a value less than or equal to a space as split characters.
     *
     * This test specifically checks the null character ('\u0000'), which is the
     * lowest possible character value.
     */
    @Test
    public void isSplitCharacter_withNullCharacter_returnsTrue() {
        // Arrange: Get the default split character implementation and define the input.
        // The default logic considers any character <= ' ' as a split character.
        SplitCharacter defaultSplitCharacter = DefaultSplitCharacter.DEFAULT;
        char[] textContainingNullChar = { '\u0000' };
        int characterIndex = 0;

        // Act: Check if the null character is considered a split character.
        boolean isSplit = defaultSplitCharacter.isSplitCharacter(
                characterIndex, // start
                characterIndex, // current
                characterIndex, // end
                textContainingNullChar,
                null // PdfChunk array is not needed for this test case
        );

        // Assert: The result should be true.
        assertTrue("The null character ('\\u0000') should be a split character.", isSplit);
    }
}