package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for exception handling within the {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that parsing a pseudo-selector requiring a numeric index (e.g., :lt(n))
     * throws an IllegalStateException when a non-numeric argument is provided.
     */
    @Test
    public void parseWithNonNumericIndexShouldThrowIllegalStateException() {
        // GIVEN a query with a pseudo-selector that requires a numeric index,
        // but is provided with a non-numeric string like "%d".
        String invalidQuery = ":lt(%d)";

        try {
            // WHEN the invalid query is parsed
            QueryParser.parse(invalidQuery);

            // THEN an exception should have been thrown, so this line should not be reached.
            fail("Expected an IllegalStateException for a non-numeric index, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // THEN verify that the caught exception has the expected, informative message.
            assertEquals("Index must be numeric", e.getMessage());
        }
    }
}