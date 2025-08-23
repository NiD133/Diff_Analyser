package org.apache.ibatis.builder;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link ParameterExpression}.
 */
public class ParameterExpressionTest {

    /**
     * Tests that the parser correctly handles an expression containing a property
     * followed by a single key-value attribute.
     */
    @Test
    public void shouldParseExpressionWithPropertyAndOneAttribute() {
        // Arrange: Define an expression with a property ('wLz5Rg') and one attribute ('g').
        // The parser should handle the complex value associated with the 'g' attribute.
        String expression = "wLz5Rg,g= +b1h^([5";

        // Act: Create a new ParameterExpression, which parses the string in its constructor.
        Map<String, String> parsedExpression = new ParameterExpression(expression);

        // Assert: Verify that the expression was parsed into the correct components.
        // The map should contain two entries: the main property and the specified attribute.
        assertEquals(2, parsedExpression.size());
        assertEquals("wLz5Rg", parsedExpression.get("property"));
        assertEquals("+b1h^([5", parsedExpression.get("g"));
    }
}