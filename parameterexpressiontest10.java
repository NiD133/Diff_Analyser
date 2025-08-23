package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

class ParameterExpressionTest {

    /**
     * Tests that the parser correctly handles an expression containing a property,
     * a JDBC type, and multiple key-value attributes.
     * The format tested is: "property:jdbcType, key1=value1, key2=value2"
     */
    @Test
    void shouldParsePropertyWithJdbcTypeAndAttributes() {
        // Arrange
        String expression = "id:VARCHAR, attr1=val1, attr2=val2";
        Map<String, String> expectedParameters = Map.of(
            "property", "id",
            "jdbcType", "VARCHAR",
            "attr1", "val1",
            "attr2", "val2"
        );

        // Act
        Map<String, String> actualParameters = new ParameterExpression(expression);

        // Assert
        assertEquals(expectedParameters, actualParameters);
    }
}