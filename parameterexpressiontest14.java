package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the ParameterExpression parser.
 */
class ParameterExpressionTest {

    @Test
    @DisplayName("Should throw BuilderException for invalid characters after an expression")
    void shouldThrowExceptionForInvalidCharacterAfterExpression() {
        // Arrange: Define an input string where an invalid character '+' follows a valid expression.
        // The parser expects a comma, a colon, or the end of the string after the closing parenthesis.
        String invalidExpression = "(expression)+";

        // Act & Assert: Verify that parsing the invalid expression throws a BuilderException.
        BuilderException exception = assertThrows(BuilderException.class, () -> {
            new ParameterExpression(invalidExpression);
        });

        // Assert: Check that the exception message is precise and informative.
        String expectedMessage = "Parsing error in {(expression)+} in position 12";
        assertEquals(expectedMessage, exception.getMessage());
    }
}