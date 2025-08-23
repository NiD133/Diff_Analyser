package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains improved, more understandable tests for the {@link DefaultSplitCharacter} class.
 * The original tests were auto-generated, leading to non-descriptive names.
 */
public class DefaultSplitCharacter_ESTestTest4 extends DefaultSplitCharacter_ESTest_scaffolding {

    /**
     * Tests that getCurrentCharacter() correctly retrieves a character from the provided
     * character array at a specific index, especially when the PdfChunk array is null.
     * This verifies the method's basic functionality as an accessor for the character array.
     */
    @Test
    public void getCurrentCharacter_whenChunkArrayIsNull_returnsCharacterFromCharArrayAtIndex() {
        // Arrange: Set up the test objects and data.
        // The method under test is protected, so we cast the DEFAULT instance to access it.
        DefaultSplitCharacter splitCharacter = (DefaultSplitCharacter) DefaultSplitCharacter.DEFAULT;
        
        char[] textCharacters = {'H', 'e', 'l', 'l', 'o'};
        int targetIndex = 2; // The index for the character 'l'
        char expectedCharacter = 'l';
        
        // The PdfChunk array is intentionally null to test this specific path.
        PdfChunk[] chunks = null;

        // Act: Execute the method under test.
        char actualCharacter = splitCharacter.getCurrentCharacter(targetIndex, textCharacters, chunks);

        // Assert: Verify the outcome.
        assertEquals(
            "The character at the specified index should be returned when the chunk array is null.",
            expectedCharacter,
            actualCharacter
        );
    }
}