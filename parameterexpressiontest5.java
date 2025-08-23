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
     * Verifies that the parser correctly handles an expression that includes
     * a legacy-style JDBC type, formatted as `(expression):jdbcType`.
     */
    @Test
    @DisplayName("Should parse expression with old-style JDBC type")
    void shouldParseExpressionWithOldStyleJdbcType() {
        // Arrange
        // This format includes a parenthesized expression followed by a colon and a JDBC type.
        String inputExpression = "(id.toString()):VARCHAR";
        String expectedExpressionPart = "id.toString()";
        String expectedJdbcType = "VARCHAR";

        // Act
        // The ParameterExpression constructor parses the input string into a map.
        Map<String, String> parsedParameters = new ParameterExpression(inputExpression);

        // Assert
        // Verify that the map contains the correctly parsed components.
        assertAll("Parsed parameters should be correct",
            () -> assertEquals(expectedExpressionPart, parsedParameters.get("expression"), "The expression part should be extracted."),
            () -> assertEquals(expectedJdbcType, parsedParameters.get("jdbcType"), "The JDBC type part should be extracted."),
            () -> assertEquals(2, parsedParameters.size(), "The map should contain exactly two entries.")
        );
    }
}