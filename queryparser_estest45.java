package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link QueryParser} focusing on error handling for invalid syntax.
 */
// Note: The original class name "QueryParser_ESTestTest45" was renamed for clarity.
// The base class "QueryParser_ESTest_scaffolding" is retained as its contents are unknown.
public class QueryParserTest extends QueryParser_ESTest_scaffolding {

    /**
     * Verifies that parsing a query containing an invalid character ('$') throws an
     * IllegalStateException with a descriptive error message.
     */
    @Test
    public void parseShouldThrowExceptionForQueryWithInvalidCharacter() {
        // Arrange: Define the invalid query and the expected error message.
        String invalidQuery = "org.jsoup.select.StructuralEvaluator$Not";
        String expectedErrorMessage = "Could not parse query 'org.jsoup.select.StructuralEvaluator$Not': unexpected token at '$Not'";

        // Act & Assert: Attempt to parse the invalid query and verify the exception.
        try {
            QueryParser.parse(invalidQuery);
            fail("Expected an IllegalStateException to be thrown for a query with an invalid character.");
        } catch (IllegalStateException e) {
            // Verify that the exception message is correct and informative.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}