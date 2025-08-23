package com.itextpdf.text.pdf;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * This test suite evaluates the functionality of the DefaultSplitCharacter class.
 */
public class DefaultSplitCharacterTest {

    /**
     * Verifies that getCurrentCharacter() correctly retrieves a character from the
     * source character array at a given index when the PdfChunk array is null.
     *
     * This scenario tests the simplest path of the method, where it should directly
     * access the character array without considering chunk-specific logic.
     */
    @Test
    public void getCurrentCharacter_shouldReturnCharacterFromSourceArray_whenChunkArrayIsNull() {
        // Arrange: Set up the test instance and input data.
        // We instantiate the class directly to call the method under test.
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        char[] sourceCharacters = {'H', 'e', 'l', 'l', 'o'};
        int characterIndex = 2; // The index for the second 'l'
        char expectedCharacter = 'l';

        // Act: Call the method being tested.
        // The PdfChunk array is passed as null to test this specific execution path.
        char retrievedCharacter = splitCharacter.getCurrentCharacter(characterIndex, sourceCharacters, null);

        // Assert: Verify that the result matches the expected outcome.
        assertEquals("The method should return the character at the specified index.", expectedCharacter, retrievedCharacter);
    }
}