package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for exception handling in the QueryParser.
 */
public class QueryParserExceptionTest {

    /**
     * Verifies that parsing a query with an `:eq()` pseudo-selector that is missing its
     * required numeric index argument throws an IllegalStateException.
     */
    @Test
    public void parse_onEqPseudoSelectorWithMissingIndex_shouldThrowException() {
        // The query "I>tD:eq" is invalid because the :eq pseudo-selector requires a
        // numeric index in parentheses, for example: :eq(0).
        String invalidQuery = "I>tD:eq";

        try {
            QueryParser.parse(invalidQuery);
            fail("Expected an IllegalStateException for an :eq selector with a missing index, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // Verify that the exception is thrown for the correct reason.
            String expectedMessage = "Index must be numeric";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}