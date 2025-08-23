package org.apache.ibatis.builder;

import org.junit.Test;

/**
 * Test suite for the {@link ParameterExpression} parser.
 */
public class ParameterExpressionTest {

    /**
     * Verifies that the constructor throws a StringIndexOutOfBoundsException
     * when parsing a malformed expression with an unclosed opening parenthesis.
     *
     * The parser expects a matching ')' for every '('. When the closing
     * parenthesis is missing, the parsing logic attempts to read past the end
     * of the string, resulting in this exception. This test confirms that this
     * specific syntax error is handled by throwing the expected exception.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void constructorShouldThrowExceptionForUnclosedParenthesis() {
        // This input is the minimal case to trigger the error: an expression
        // that starts with '(' but is never closed.
        new ParameterExpression("(expression");
    }
}