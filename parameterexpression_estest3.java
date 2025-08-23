package org.apache.ibatis.builder;

import org.junit.Test;

/**
 * Tests for the {@link ParameterExpression} parser.
 */
public class ParameterExpressionTest {

    /**
     * Verifies that the constructor throws StringIndexOutOfBoundsException
     * when parsing an expression with an unclosed opening parenthesis.
     *
     * According to the class documentation, an expression is denoted by parentheses,
     * e.g., "(user.name)". This test ensures the parser handles cases where the
     * closing parenthesis is missing.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void shouldThrowExceptionWhenExpressionParenthesisIsNotClosed() {
        // Define an expression string that is intentionally malformed
        // by omitting the closing parenthesis.
        String malformedExpression = "(an_expression_without_a_closing_paren";

        // The constructor should fail while parsing this string because it will
        // search for a ')' and run past the end of the string.
        new ParameterExpression(malformedExpression);
    }
}