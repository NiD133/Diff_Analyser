package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link QueryParser} class.
 */
public class QueryParserTest {

    /**
     * Verifies that the :containsWholeText pseudo-selector can be parsed correctly,
     * even when its argument contains characters that might be interpreted specially,
     * such as a format specifier ("%s"). The parser should treat "%s" as a
     * literal string to search for.
     */
    @Test
    public void shouldParseContainsWholeTextWithFormatSpecifierAsLiteralText() {
        // Arrange: Define a CSS query with a literal "%s" string.
        String query = ":containsWholeText(%s)";

        // Act: Parse the query into an Evaluator.
        Evaluator evaluator = QueryParser.parse(query);

        // Assert: Check that the parsing was successful and correct.
        assertNotNull("The parsed evaluator should not be null.", evaluator);
        
        // Verify the specific type of the evaluator.
        // This confirms the parser correctly identified the pseudo-selector.
        assertTrue("The evaluator should be an instance of ContainsWholeText.",
                   evaluator instanceof Evaluator.ContainsWholeText);
                   
        // Verify the internal state by checking its string representation.
        // This confirms the argument "%s" was captured correctly.
        assertEquals("The string representation of the evaluator should match the original query.",
                     query, evaluator.toString());
    }
}