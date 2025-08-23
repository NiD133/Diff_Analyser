package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ParameterExpression} parser.
 */
@DisplayName("ParameterExpression Parser")
class ParameterExpressionTest {

    /**
     * Verifies that the parser correctly handles an expression in parentheses
     * followed by a single key-value attribute.
     *
     * The parser should identify:
     * 1. The content within the parentheses as the 'expression'.
     * 2. The subsequent part as a 'name' attribute.
     */
    @Test
    @DisplayName("should parse an expression in parentheses with one attribute")
    void shouldParseExpressionInParenthesesWithOneAttribute() {
        // Arrange: Define the input string to be parsed.
        // This format represents an expression '(id.toString())' and an attribute 'name=value'.
        String input = "(id.toString()),name=value";

        // Act: Parse the expression string.
        // The ParameterExpression class extends HashMap, so its constructor populates the map.
        Map<String, String> parsedAttributes = new ParameterExpression(input);

        // Assert: Verify that the parsed map contains the correct keys and values.
        // Using assertAll groups related assertions and ensures all are checked before the test fails.
        assertAll("Parsed attributes should be correct",
            () -> assertEquals(2, parsedAttributes.size(), "Should contain two entries: expression and name"),
            () -> assertEquals("id.toString()", parsedAttributes.get("expression"), "The 'expression' part should be extracted correctly"),
            () -> assertEquals("value", parsedAttributes.get("name"), "The 'name' attribute should be parsed correctly")
        );
    }
}