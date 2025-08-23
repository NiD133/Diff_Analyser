package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Unit tests for the {@link DefaultSplitCharacter} class.
 */
public class DefaultSplitCharacterTest {

    /**
     * Verifies that getCurrentCharacter() throws an ArrayIndexOutOfBoundsException
     * when the provided index is outside the bounds of the character array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getCurrentCharacter_withOutOfBoundsIndex_shouldThrowException() {
        // Arrange: Create a DefaultSplitCharacter instance and test data.
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        char[] charArray = {'a', 'b'};
        PdfChunk[] chunks = null; // The chunk array is not relevant for this test path and can be null.
        int outOfBoundsIndex = charArray.length; // The first invalid index is the array's length.

        // Act: Call the method with an index that is out of bounds.
        // The @Test(expected=...) annotation will assert that the correct exception is thrown.
        splitCharacter.getCurrentCharacter(outOfBoundsIndex, charArray, chunks);
    }
}