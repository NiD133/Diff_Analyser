package org.jsoup.select;

import org.jsoup.select.Selector.SelectorParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link QueryParser} focusing on error handling for invalid syntax.
 */
public class QueryParserErrorTest {

    /**
     * Verifies that parsing a query with invalid syntax throws a SelectorParseException.
     *
     * The original test used a cryptic query ("37m~T/SMo)=E[BHj8|{") and a non-standard
     * assertion framework. This version uses a clear, minimal example of invalid syntax
     * and standard JUnit assertions to improve readability and maintainability.
     */
    @Test
    public void parseShouldThrowExceptionForInvalidQuerySyntax() {
        // Arrange: A CSS query with an invalid character ('/') where a selector component is expected.
        String invalidQuery = "a > / b";

        try {
            // Act: Attempt to parse the invalid query.
            QueryParser.parse(invalidQuery);
            fail("Expected a SelectorParseException to be thrown for the invalid query syntax.");
        } catch (SelectorParseException e) {
            // Assert: Verify that the exception is of the correct type and the message
            // clearly indicates the location of the parsing error.
            String expectedMessage = "Could not parse query 'a > / b': unexpected token at '/ b'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}