package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link ParameterExpression}.
 */
class ParameterExpressionTest {

    /**
     * Tests that the parser throws a BuilderException for an expression
     * where a colon is not followed by a JDBC type.
     */
    @Test
    void shouldThrowExceptionForIncompleteJdbcType() {
        // Arrange: Define an invalid parameter expression where the JDBC type is missing after the colon.
        String invalidExpression = "id:";

        // Act & Assert: Verify that parsing the invalid expression throws a BuilderException.
        // Using assertThrows is more concise and declarative than a try-catch block.
        BuilderException exception = assertThrows(BuilderException.class,
            () -> new ParameterExpression(invalidExpression));

        // Assert: Check if the exception message contains the expected error details,
        // confirming that the parser failed at the correct location for the right reason.
        String expectedError = "Parsing error in {id:} in position 3";
        assertTrue(exception.getMessage().contains(expectedError),
            () -> "Exception message should pinpoint the parsing error. Was: " + exception.getMessage());
    }
}