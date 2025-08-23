package org.apache.ibatis.builder;

import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for {@link ParameterExpression}.
 */
public class ParameterExpressionTest {

    /**
     * Verifies that a string enclosed in parentheses is parsed as a single "expression" property.
     * Any special characters within the parentheses, such as a colon, should be treated as
     * part of the expression's value, not as a delimiter for a JDBC type.
     */
    @Test
    public void shouldParseParenthesizedTermAsSingleExpression() {
        // Arrange
        String input = "(Vx5:BD)";

        // Act
        Map<String, String> parsedExpression = new ParameterExpression(input);

        // Assert
        assertEquals("Should parse exactly one property", 1, parsedExpression.size());
        
        String expressionValue = parsedExpression.get("expression");
        assertNotNull("The 'expression' property should exist", expressionValue);
        assertEquals("The value of the expression should be the full content within parentheses",
                "Vx5:BD", expressionValue);
    }
}