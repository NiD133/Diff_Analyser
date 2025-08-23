package org.apache.ibatis.builder;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link ParameterExpression} parser.
 */
public class ParameterExpressionTest {

    /**
     * Tests that a simple string without any special delimiters (like ',' or ':')
     * is correctly parsed as a single property name.
     */
    @Test
    public void shouldParseExpressionWithOnlyPropertyName() {
        // Arrange: Define a simple expression that only contains a property name.
        // Using a realistic name like "userId" is more understandable than a random string.
        String expression = "userId";

        // Act: Parse the expression by creating a new ParameterExpression instance.
        // The ParameterExpression class extends HashMap, so we can treat it as a Map.
        Map<String, String> parsedExpression = new ParameterExpression(expression);

        // Assert: Verify that the expression was parsed correctly into its components.
        // 1. The map should contain exactly one entry.
        assertEquals("The map should contain a single parsed component.", 1, parsedExpression.size());

        // 2. The map should contain the key "property" with the correct value.
        // This makes the test more specific and robust than only checking the size.
        assertTrue("The map should contain the 'property' key.", parsedExpression.containsKey("property"));
        assertEquals("The value of 'property' should be the original expression string.",
                expression, parsedExpression.get("property"));
    }
}