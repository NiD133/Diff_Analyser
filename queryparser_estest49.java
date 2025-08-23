package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the CSS selector parsing in {@link QueryParser}.
 */
public class QueryParserTest {

    @Test
    public void parse_whenQueryIsUniversalSelector_shouldReturnAllElementsEvaluator() {
        // Arrange
        String universalSelectorQuery = "*";
        String expectedStringRepresentation = "*";

        // Act
        Evaluator evaluator = QueryParser.parse(universalSelectorQuery);

        // Assert
        assertEquals(
            "The string representation of the parsed universal selector should be '*'.",
            expectedStringRepresentation,
            evaluator.toString()
        );
    }
}