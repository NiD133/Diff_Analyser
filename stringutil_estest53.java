package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link StringUtil}.
 */
public class StringUtilTest {

    @Test
    public void isAsciiLetter_shouldReturnFalse_forNonAsciiLetterCharacter() {
        // Arrange: A character that is not an ASCII letter (a-z, A-Z).
        char symbol = '}';

        // Act: Check if the character is an ASCII letter.
        boolean isLetter = StringUtil.isAsciiLetter(symbol);

        // Assert: The result should be false.
        assertFalse("The symbol '}' should not be considered an ASCII letter.", isLetter);
    }
}