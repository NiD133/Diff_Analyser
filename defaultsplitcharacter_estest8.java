package com.itextpdf.text.pdf;

import org.junit.Test;

/**
 * Unit tests for the {@link DefaultSplitCharacter} class.
 */
public class DefaultSplitCharacterTest {

    /**
     * Verifies that isSplitCharacter() throws an ArrayIndexOutOfBoundsException
     * when the 'current' index parameter is negative.
     * <p>
     * The method internally attempts to access the character array at the 'current' index,
     * which is an invalid operation for a negative value.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void isSplitCharacter_shouldThrowException_whenCurrentIndexIsNegative() {
        // Arrange: Create an instance of the class under test and prepare arguments.
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        char[] emptyCharArray = new char[0];
        PdfChunk[] emptyChunkArray = new PdfChunk[0];
        
        int negativeIndex = -1;
        int anyStartIndex = 0;
        int anyEndIndex = 0;

        // Act: Call the method with a negative 'current' index.
        // This is expected to throw the exception.
        splitCharacter.isSplitCharacter(anyStartIndex, negativeIndex, anyEndIndex, emptyCharArray, emptyChunkArray);

        // Assert: The @Test(expected) annotation handles the exception verification.
        // If the expected exception is not thrown, the test will fail.
    }
}