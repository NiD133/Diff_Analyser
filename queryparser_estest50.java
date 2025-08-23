package org.jsoup.select;

import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Contains an improved test case for the {@link QueryParser} class,
 * focusing on understandability and maintainability.
 */
public class QueryParserImprovedTest {

    /**
     * Verifies that the parser correctly handles the universal namespace selector ('*|')
     * combined with a tag name. This syntax is used to select an element
     * regardless of its namespace.
     */
    @Test
    public void parseShouldHandleUniversalNamespaceForTag() {
        // Arrange: A CSS query with a universal namespace selector for the tag '9'.
        String queryWithUniversalNamespace = "*|9";

        // Act: Parse the query to create an evaluator.
        Evaluator evaluator = QueryParser.parse(queryWithUniversalNamespace);

        // Assert: The parser should return a non-null Tag evaluator for the specified tag name.
        assertNotNull("The parsed evaluator should not be null.", evaluator);
        assertTrue("The evaluator should be an instance of Evaluator.Tag.", evaluator instanceof Evaluator.Tag);
        
        // The toString() method of an Evaluator.Tag returns the tag name.
        assertEquals("The evaluator should represent the tag '9'.", "9", evaluator.toString());
    }
}