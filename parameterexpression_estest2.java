package org.apache.ibatis.builder;

import org.apache.ibatis.builder.BuilderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link ParameterExpression} parser.
 */
public class ParameterExpressionTest {

    /**
     * Verifies that the parser throws a BuilderException when it encounters
     * invalid characters immediately following a valid parenthesized expression.
     * According to the supported grammar, only a comma (for options) or a colon
     * (for jdbcType) is permitted after the closing parenthesis.
     */
    @Test
    public void shouldThrowExceptionForInvalidCharactersAfterExpression() {
        // Arrange: An expression that is well-formed up to the closing parenthesis
        // but is followed by illegal characters ("wo7C47").
        String invalidExpression = "(V5BD)wo7C47";

        try {
            // Act: Attempt to parse the invalid expression.
            new ParameterExpression(invalidExpression);
            fail("Should have thrown BuilderException for an invalid expression.");
        } catch (BuilderException e) {
            // Assert: Verify the exception message correctly identifies the error and its location.
            // The parser should fail at position 6, which is the character 'w'.
            String expectedMessage = "Parsing error in '" + invalidExpression + "' in position 6";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}