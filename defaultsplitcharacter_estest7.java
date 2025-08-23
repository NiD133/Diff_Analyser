package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Unit tests for the {@link DefaultSplitCharacter} class.
 */
public class DefaultSplitCharacterTest {

    /**
     * Verifies that isSplitCharacter throws a NullPointerException when the character array is null.
     * <p>
     * The method's implementation attempts to access the character array when the PdfChunk array is null,
     * which should result in an NPE if the character array is also null.
     */
    @Test(expected = NullPointerException.class)
    public void isSplitCharacter_whenCharacterArrayIsNull_throwsNullPointerException() {
        // Arrange
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        int start = 0;
        int current = 0;
        int end = 1;
        char[] nullCharArray = null;
        PdfChunk[] nullChunkArray = null;

        // Act & Assert
        // This call is expected to throw a NullPointerException.
        splitCharacter.isSplitCharacter(start, current, end, nullCharArray, nullChunkArray);
    }
}