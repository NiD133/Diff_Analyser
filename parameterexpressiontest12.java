package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ParameterExpression}.
 */
class ParameterExpressionTest {

    /**
     * Verifies that the parser correctly handles and trims leading/trailing whitespace
     * from the expression, its parts, and its key-value pairs.
     */
    @Test
    void shouldIgnoreLeadingAndTrailingSpaces() {
        // Arrange
        // An expression with extra spaces around the property, keys, values, and separators.
        final String expressionWithSpaces = " id , jdbcType =  VARCHAR,  attr1 = val1 ,  attr2 = val2 ";
        
        final Map<String, String> expectedParameters = Map.of(
            "property", "id",
            "jdbcType", "VARCHAR",
            "attr1", "val1",
            "attr2", "val2"
        );

        // Act
        ParameterExpression actualParameters = new ParameterExpression(expressionWithSpaces);

        // Assert
        assertEquals(expectedParameters, actualParameters);
    }
}