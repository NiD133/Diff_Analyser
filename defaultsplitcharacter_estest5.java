package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DefaultSplitCharacter} class.
 */
public class DefaultSplitCharacterTest {

    /**
     * Verifies that getCurrentCharacter returns the correct character from the character array
     * at the specified index when the PdfChunk array is null.
     */
    @Test
    public void getCurrentCharacter_shouldReturnCharacterAtIndex_whenChunkArrayIsNull() {
        // Arrange: Set up the test objects and data.
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        char[] textChars = {'a', 'b', 'c', 'd', '1', 'f', 'g'};
        int targetIndex = 4;
        char expectedCharacter = '1';

        // Act: Call the method under test.
        // The PdfChunk array is intentionally null to test the simple path.
        char actualCharacter = splitCharacter.getCurrentCharacter(targetIndex, textChars, null);

        // Assert: Verify the result is as expected.
        assertEquals(expectedCharacter, actualCharacter);
    }
}