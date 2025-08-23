package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for invalid query handling in {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that parsing a query with a dangling meta-character ('?')
     * throws an IllegalStateException. The '?' is not a valid character
     * in a pseudo-selector name and should be rejected by the parser.
     */
    @Test
    public void parseShouldThrowExceptionForDanglingMetaCharacter() {
        // Arrange: Define an invalid CSS query with a '?' character, which is a dangling meta-character.
        String invalidQuery = ":matches?holeText(1s)";

        try {
            // Act: Attempt to parse the invalid query.
            QueryParser.parse(invalidQuery);
            fail("Expected an IllegalStateException to be thrown due to the dangling meta-character.");
        } catch (IllegalStateException e) {
            // Assert: Verify that the exception message correctly identifies the error.
            String expectedErrorMessage = "Dangling meta character '?' near index 0";
            assertTrue(
                "Exception message should clearly indicate the dangling meta-character error.",
                e.getMessage().contains(expectedErrorMessage)
            );
        }
    }
}