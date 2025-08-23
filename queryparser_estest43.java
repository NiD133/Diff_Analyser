package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for exception handling within the {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that parsing an :nth-child pseudo-selector with an invalid, non-numeric
     * argument (like a format specifier "%d") results in an IllegalStateException.
     * The parser should reject arguments that do not conform to the expected "An+B" syntax.
     */
    @Test
    public void parsingInvalidNthChildArgumentThrowsException() {
        // Arrange
        String invalidQuery = ":nth-child(%d)";
        String expectedErrorMessage = "Could not parse nth-index '%d': unexpected format";

        // Act & Assert
        try {
            QueryParser.parse(invalidQuery);
            fail("Expected an IllegalStateException to be thrown due to the invalid :nth-child format.");
        } catch (IllegalStateException e) {
            // Verify that the correct exception with the expected message was thrown.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}