package org.jsoup.select;

import org.jsoup.select.Evaluator.AttributeWithValueStarting;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link QueryParser}.
 * This class focuses on verifying the parsing of CSS attribute selectors.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser correctly handles an attribute selector
     * where the attribute name and value contain literal percent signs ('%').
     *
     * This test ensures that characters that might be mistaken for format specifiers
     * (like '%s') are parsed as part of the attribute's name and value.
     */
    @Test
    public void shouldParseAttributeSelectorWithPercentSignsInNameAndValue() {
        // Arrange: Define a CSS query with an attribute "starts with" selector.
        // The attribute name and value both contain '%s' as literal strings.
        String query = "[%s^=%s]";

        // Act: Parse the CSS query into an Evaluator object.
        Evaluator evaluator = QueryParser.parse(query);

        // Assert: Verify that the parser produced the correct type of evaluator
        // and that its internal state is as expected.
        assertNotNull("The parsed evaluator should not be null.", evaluator);

        // Check for the specific evaluator type for a "starts with" attribute selector.
        assertTrue("The evaluator should be an instance of AttributeWithValueStarting.",
            evaluator instanceof AttributeWithValueStarting);

        // A more robust check is to verify the string representation of the parsed evaluator.
        // This confirms the attribute key, operator, and value were all parsed correctly.
        assertEquals("The string representation of the evaluator should match the original query.",
            query, evaluator.toString());
    }
}