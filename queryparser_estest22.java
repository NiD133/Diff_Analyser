package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link QueryParser} class.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser correctly handles an attribute selector
     * where the attribute name and value contain percent signs.
     * The parser should treat these as literal characters, not format specifiers.
     */
    @Test
    public void shouldParseAttributeSelectorWithPercentSigns() {
        // Arrange
        String queryWithPercentSigns = "[%s=%s]";

        // Act
        Evaluator evaluator = QueryParser.parse(queryWithPercentSigns);

        // Assert
        assertNotNull("The parser should return a non-null evaluator for a valid query.", evaluator);
        assertEquals("The string representation of the evaluator should match the original query.",
                "[%s=%s]", evaluator.toString());
    }
}