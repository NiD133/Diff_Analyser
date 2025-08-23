package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ParameterExpression} parser.
 */
@DisplayName("ParameterExpression Parser")
class ParameterExpressionTest {

    @Test
    @DisplayName("Should parse an expression with a property and a single attribute")
    void shouldParsePropertyAndSingleAttribute() {
        // Arrange: Define the input string representing a property and one attribute.
        // The format is 'property,key=value'.
        String expression = "id,name=value";

        // Act: Parse the expression by creating a new ParameterExpression.
        Map<String, String> parameterMap = new ParameterExpression(expression);

        // Assert: Verify the resulting map contains the correct property and attribute.
        // The property is stored under the key "property", and attributes are stored by their name.
        assertAll("The parsed map should contain the correct property and attribute",
            () -> assertEquals(2, parameterMap.size(), "Should contain two entries"),
            () -> assertEquals("id", parameterMap.get("property"), "The 'property' part should be correctly extracted"),
            () -> assertEquals("value", parameterMap.get("name"), "The 'name' attribute should be correctly extracted")
        );
    }
}