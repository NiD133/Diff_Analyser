package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test cases for the Jsoup {@link QueryParser}.
 * This class focuses on ensuring the parser correctly handles invalid CSS selector syntax.
 */
public class QueryParserTest {

    /**
     * Tests that the parser throws an exception when encountering a malformed attribute selector.
     * The query "[^-...]" is invalid because an attribute name cannot start with the '^' character.
     * The '^' is a prefix match operator and must follow an attribute name (e.g., [attr^=val]).
     */
    @Test
    public void parseThrowsExceptionForMalformedAttributeSelector() {
        // Arrange: Define an invalid CSS query.
        String malformedQuery = "[^-a-zA-Z0-9_:.]+";

        try {
            // Act: Attempt to parse the invalid query.
            QueryParser.parse(malformedQuery);
            fail("Expected an IllegalStateException to be thrown due to the malformed attribute selector.");
        } catch (IllegalStateException e) {
            // Assert: Verify that the exception message is correct and informative.
            String expectedMessage = "Could not parse query '[^-a-zA-Z0-9_:.]+': unexpected token at ''";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}