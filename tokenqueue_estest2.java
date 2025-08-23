package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static utility methods in {@link TokenQueue}.
 */
public class TokenQueueTest {

    /**
     * Verifies that the unescape method correctly removes a single backslash
     * that is used to escape a subsequent character.
     */
    @Test
    public void unescapeRemovesSingleBackslash() {
        // Arrange: Define an input string with an escaped character ('\P')
        // and the expected result after unescaping.
        String inputWithEscape = "H<b:oK|jCxQ\\P4C3U.";
        String expectedOutput = "H<b:oK|jCxQP4C3U.";

        // Act: Call the method under test.
        String actualOutput = TokenQueue.unescape(inputWithEscape);

        // Assert: Verify that the backslash was removed and the rest of the string is unchanged.
        assertEquals(expectedOutput, actualOutput);
    }
}