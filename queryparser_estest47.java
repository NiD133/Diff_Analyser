package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link QueryParser} class, focusing on exception handling.
 */
public class QueryParserTest {

    /**
     * Verifies that the combinator() method throws an IllegalStateException
     * when provided with an unrecognized combinator character.
     */
    @Test
    public void combinatorWithUnknownCharacterShouldThrowIllegalStateException() {
        char unknownCombinator = ']';

        try {
            // Attempt to parse with an invalid combinator. The Evaluator arguments are not
            // relevant for this test, so they can be null.
            QueryParser.combinator(null, unknownCombinator, null);
            fail("Expected an IllegalStateException to be thrown for an unknown combinator.");
        } catch (IllegalStateException e) {
            // Assert that the exception message is specific and helpful.
            String expectedMessage = "Unknown combinator '" + unknownCombinator + "'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}