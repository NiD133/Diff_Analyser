package org.apache.ibatis.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test suite for the {@link ParameterExpression} parser.
 */
public class ParameterExpressionTest {

    /**
     * The constructor should throw a BuilderException when given an expression
     * with a syntax error, such as a misplaced semicolon inside parentheses.
     */
    @Test
    public void shouldThrowBuilderExceptionForInvalidExpressionSyntax() {
        // Arrange
        String invalidExpression = "(;)$ ";
        String expectedErrorMessage = "Parsing error in {" + invalidExpression + "} in position 3";

        // Act & Assert
        try {
            new ParameterExpression(invalidExpression);
            fail("Expected a BuilderException to be thrown due to a parsing error.");
        } catch (BuilderException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}