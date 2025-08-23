package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Test suite for the {@link DefaultSplitCharacter} class.
 */
public class DefaultSplitCharacterTest {

    /**
     * Verifies that calling {@code getCurrentCharacter} with an index that is outside
     * the bounds of the character array correctly throws an {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getCurrentCharacter_withOutOfBoundsIndex_throwsArrayIndexOutOfBoundsException() {
        // Arrange: Set up the test instance and input data.
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        char[] characters = new char[10];
        int outOfBoundsIndex = 10; // The first invalid index for a 10-element array is 10.

        // The PdfChunk array is not relevant for this specific exception scenario.
        PdfChunk[] chunks = null;

        // Act: Call the method with the out-of-bounds index.
        // The @Test(expected=...) annotation will handle the assertion.
        splitCharacter.getCurrentCharacter(outOfBoundsIndex, characters, chunks);

        // Assert: The test succeeds if an ArrayIndexOutOfBoundsException is thrown,
        // and fails otherwise.
    }
}