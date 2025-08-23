package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the CSS selector parsing in {@link QueryParser}.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser can handle attribute selectors where the key and value
     * contain special characters, such as the percent sign ('%'). This ensures the parser
     * is robust and doesn't misinterpret these characters as special syntax.
     */
    @Test
    public void shouldParseAttributeSelectorWithSpecialCharactersInKeyAndValue() {
        // GIVEN a query with an attribute selector containing '%' in its key and value.
        String query = "[%s$=%s]";

        // WHEN the query is parsed.
        Evaluator evaluator = QueryParser.parse(query);

        // THEN the parser should successfully create a non-null evaluator.
        assertNotNull("The parsed evaluator should not be null.", evaluator);

        // AND the evaluator should be of the correct type for an "ends with" attribute selector.
        assertTrue("The evaluator should be an instance of AttributeWithValueEnding.",
                   evaluator instanceof Evaluator.AttributeWithValueEnding);
        
        // AND the evaluator's string representation should match the original query,
        // confirming that the key, operator, and value were all parsed correctly.
        assertEquals("The evaluator's string form should match the query.", "[%s$=%s]", evaluator.toString());
    }
}