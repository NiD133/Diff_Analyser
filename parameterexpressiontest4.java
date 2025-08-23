package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link ParameterExpression}.
 */
// The class name was simplified for clarity (from ParameterExpressionTestTest4).
class ParameterExpressionTest {

    @Test
    @DisplayName("Should parse property and JDBC type correctly when expression has extra whitespace")
    void shouldParsePropertyAndJdbcTypeEvenWithExtraWhitespace() {
        // Arrange: Define an expression with inconsistent whitespace.
        // The parser is expected to handle leading, trailing, and internal extra spaces.
        String expressionWithWhitespace = " id :  VARCHAR ";

        // Act: Parse the expression by creating a new ParameterExpression instance.
        Map<String, String> parameterMap = new ParameterExpression(expressionWithWhitespace);

        // Assert: Verify that the parser correctly extracted and trimmed the components.
        assertAll("The parsed map should contain a correctly trimmed property and jdbcType",
            () -> assertEquals(2, parameterMap.size(), "Map should contain exactly two entries"),
            () -> assertEquals("id", parameterMap.get("property"), "Property name should be 'id'"),
            () -> assertEquals("VARCHAR", parameterMap.get("jdbcType"), "JDBC type should be 'VARCHAR'")
        );
    }
}