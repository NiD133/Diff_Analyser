package org.apache.ibatis.builder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * Tests for {@link ParameterExpression}.
 */
class ParameterExpressionTest {

    @Test
    void shouldParseSimpleProperty() {
        // Arrange
        String expression = "id";
        Map<String, String> expectedMap = Map.of("property", "id");

        // Act
        Map<String, String> actualMap = new ParameterExpression(expression);

        // Assert
        Assertions.assertEquals(expectedMap, actualMap);
    }
}