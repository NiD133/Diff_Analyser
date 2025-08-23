package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

// Renamed class from ParameterExpressionTestTest2 for better naming convention.
class ParameterExpressionTest {

    @Test
    // Renamed test to clearly describe the behavior under test:
    // the trimming of surrounding whitespace from the property name, while preserving internal spaces.
    void shouldParsePropertyAndTrimSurroundingWhitespace() {
        // Arrange: Clearly define the input and the expected result.
        // This makes the test's purpose immediately obvious.
        String inputExpression = " with spaces ";
        Map<String, String> expectedProperties = Map.of("property", "with spaces");

        // Act: The ParameterExpression constructor is the action being tested.
        // It parses the input string and populates the map.
        ParameterExpression actualProperties = new ParameterExpression(inputExpression);

        // Assert: A single assertion comparing the actual map to the expected map
        // is more concise and expressive than checking size and elements separately.
        // It verifies the entire state of the object at once.
        assertEquals(expectedProperties, actualProperties);
    }
}