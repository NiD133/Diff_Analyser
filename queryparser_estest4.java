package org.jsoup.select;

import org.junit.Test;
import java.lang.IllegalStateException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link QueryParser}.
 * This class focuses on parsing invalid query syntax.
 */
public class QueryParser_ESTestTest4 extends QueryParser_ESTest_scaffolding {

    /**
     * Verifies that parsing a CSS query containing an unexpected token (e.g., '@')
     * results in an IllegalStateException with a descriptive error message.
     */
    @Test
    public void parseShouldThrowIllegalStateExceptionOnUnexpectedToken() {
        // Arrange: A query with an invalid character '@' which is not a valid CSS selector token.
        String invalidQuery = "#P>hfIV>e@0fe";
        String expectedErrorMessage = "Could not parse query '#P>hfIV>e@0fe': unexpected token at '@0fe'";

        // Act & Assert
        try {
            QueryParser.parse(invalidQuery);
            fail("Expected an IllegalStateException to be thrown due to the invalid token in the query.");
        } catch (IllegalStateException e) {
            // Assert that the exception message clearly indicates the problem and its location.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}