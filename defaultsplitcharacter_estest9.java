package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Unit tests for the {@link DefaultSplitCharacter} class.
 */
public class DefaultSplitCharacterTest {

    /**
     * Verifies that getCurrentCharacter throws a NullPointerException when the
     * character array is null and the PdfChunk array is also null.
     *
     * The underlying implementation first checks the PdfChunk array. If it's null,
     * it attempts to access the character array, which will cause the NPE.
     */
    @Test(expected = NullPointerException.class)
    public void getCurrentCharacter_shouldThrowNullPointerException_whenCharacterArrayIsNull() {
        // Arrange
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        int arbitraryCurrentIndex = 1522;
        char[] nullCharacterArray = null;
        PdfChunk[] nullChunkArray = null;

        // Act
        // This call is expected to throw a NullPointerException because the character array is null.
        // The assertion is handled by the @Test(expected=...) annotation.
        splitCharacter.getCurrentCharacter(arbitraryCurrentIndex, nullCharacterArray, nullChunkArray);
    }
}