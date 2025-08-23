package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link QueryParser}, focusing on handling invalid query syntax.
 */
// The class name is inherited from the original EvoSuite-generated test.
public class QueryParser_ESTestTest26 extends QueryParser_ESTest_scaffolding {

    /**
     * Verifies that QueryParser.parse() throws an IllegalStateException
     * when it encounters an unexpected token in the query string.
     * The exception message should clearly indicate the location of the error.
     */
    @Test
    public void parseFailsOnQueryWithUnexpectedToken() {
        // Arrange: A CSS query with an invalid character '}' where a combinator or end-of-query is expected.
        String invalidQuery = "1+RZ-2|k}9kC<q";
        String expectedMessage = "Could not parse query '1+RZ-2|k}9kC<q': unexpected token at '}9kC<q'";

        // Act & Assert
        try {
            QueryParser.parse(invalidQuery);
            fail("Expected an IllegalStateException to be thrown due to the invalid query syntax.");
        } catch (IllegalStateException e) {
            // Verify that the exception message is informative and correct.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}