package org.apache.ibatis.builder;

import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link ParameterExpression} parser.
 *
 * This test suite verifies that parameter expression strings are correctly parsed
 * into a map of their constituent parts, such as property, jdbcType, and other attributes.
 */
public class ParameterExpressionTest {

    @Test
    public void shouldParseSimplePropertyName() {
        // Arrange
        String expression = "username";

        // Act
        Map<String, String> params = new ParameterExpression(expression);

        // Assert
        assertEquals(1, params.size());
        assertEquals("username", params.get("property"));
    }

    @Test
    public void shouldParsePropertyWithJdbcType() {
        // Arrange
        String expression = "username:VARCHAR";

        // Act
        Map<String, String> params = new ParameterExpression(expression);

        // Assert
        assertEquals(2, params.size());
        assertEquals("username", params.get("property"));
        assertEquals("VARCHAR", params.get("jdbcType"));
    }

    @Test
    public void shouldParsePropertyWithMultipleAttributes() {
        // Arrange
        String expression = "user,javaType=com.example.User,jdbcType=VARCHAR";

        // Act
        Map<String, String> params = new ParameterExpression(expression);

        // Assert
        assertEquals(3, params.size());
        assertEquals("user", params.get("property"));
        assertEquals("com.example.User", params.get("javaType"));
        assertEquals("VARCHAR", params.get("jdbcType"));
    }

    @Test
    public void shouldParseParenthesizedExpressionWithAttributes() {
        // Arrange
        String expression = "(user.name),javaType=String,jdbcType=VARCHAR";

        // Act
        Map<String, String> params = new ParameterExpression(expression);

        // Assert
        assertEquals(3, params.size());
        // Note: When parentheses are used, the key is "expression", not "property".
        assertEquals("user.name", params.get("expression"));
        assertEquals("String", params.get("javaType"));
        assertEquals("VARCHAR", params.get("jdbcType"));
    }

    @Test
    public void shouldHandleWhitespaceInExpression() {
        // Arrange
        String expression = "  user , javaType = String , jdbcType = VARCHAR  ";

        // Act
        Map<String, String> params = new ParameterExpression(expression);

        // Assert
        assertEquals(3, params.size());
        assertEquals("user", params.get("property"));
        assertEquals("String", params.get("javaType"));
        assertEquals("VARCHAR", params.get("jdbcType"));
    }
}