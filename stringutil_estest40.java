package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that the space character is correctly identified as "actual whitespace".
     * The method {@code isActuallyWhitespace} is used to identify characters that
     * visually represent whitespace, such as space, tab, or newline.
     */
    @Test
    public void isActuallyWhitespaceShouldReturnTrueForSpaceCharacter() {
        // Arrange: The integer 32 represents the ASCII code for the space character.
        // Using the character literal ' ' is more self-documenting and readable than the magic number 32.
        int spaceCharacterCode = ' ';

        // Act: Call the method under test.
        boolean result = StringUtil.isActuallyWhitespace(spaceCharacterCode);

        // Assert: Verify that the space character is considered whitespace.
        assertTrue("The space character should be identified as actual whitespace.", result);
    }
}