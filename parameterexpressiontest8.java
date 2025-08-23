package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link ParameterExpression}.
 */
// The class was renamed from ParameterExpressionTestTest8 to follow standard naming conventions.
class ParameterExpressionTest {

    @Test
    @DisplayName("Should parse an expression with a property and multiple attributes")
    void shouldParsePropertyAndMultipleAttributes() {
        // Arrange: Define the input expression and the expected output map.
        // This makes the test's objective clear from the start.
        String expression = "id, attr1=val1, attr2=val2, attr3=val3";
        Map<String, String> expectedParameters = Map.of(
            "property", "id",
            "attr1", "val1",
            "attr2", "val2",
            "attr3", "val3"
        );

        // Act: Create a new ParameterExpression, which parses the string in its constructor.
        Map<String, String> actualParameters = new ParameterExpression(expression);

        // Assert: Verify that the parsed map matches the expected map.
        // A single assertion on the entire map is more atomic and readable than multiple
        // assertions on individual elements. If the test fails, the IDE will show a
        // clear diff between the expected and actual maps.
        assertEquals(expectedParameters, actualParameters);
    }
}