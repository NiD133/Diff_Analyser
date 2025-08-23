package org.apache.ibatis.builder;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link ParameterExpression} parser.
 * This class focuses on verifying that different inline parameter expressions
 * are parsed into the correct key-value map.
 */
public class ParameterExpressionTest {

    /**
     * Verifies that a standard expression with a property and one attribute (jdbcType)
     * is parsed correctly. This is a common use case.
     *
     * The original test was auto-generated and used an obscure input string (" ;|l, =R=kNH")
     * which tested an edge case but was difficult to understand. This version tests a
     * clear, representative case to improve readability and maintainability.
     */
    @Test
    public void shouldParsePropertyAndOneAttribute() {
        // Arrange: A standard, easy-to-understand parameter expression.
        String expression = "age, jdbcType=NUMERIC";

        // Act: Parse the expression by creating a new ParameterExpression instance.
        Map<String, String> parameterMap = new ParameterExpression(expression);

        // Assert: Verify that the map contains the expected property and attribute.
        // Asserting specific key-value pairs is more robust and descriptive than only checking the size.
        assertNotNull(parameterMap);
        assertEquals(2, parameterMap.size());
        assertEquals("age", parameterMap.get("property"));
        assertEquals("NUMERIC", parameterMap.get("jdbcType"));
    }
}