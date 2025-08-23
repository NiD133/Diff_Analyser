package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ParameterExpression} parser.
 */
class ParameterExpressionTest {

  @Test
  @DisplayName("Should parse an expression in parentheses followed by multiple attributes")
  void shouldParseExpressionWithMultipleAttributes() {
    // Arrange
    // The input string contains an expression part '(id.toString())' and three key-value attributes.
    String inputString = "(id.toString()), attr1=val1, attr2=val2, attr3=val3";

    // The expected map after parsing the input string.
    Map<String, String> expectedMap = Map.of(
        "expression", "id.toString()",
        "attr1", "val1",
        "attr2", "val2",
        "attr3", "val3"
    );

    // Act
    // The ParameterExpression constructor performs the parsing.
    Map<String, String> actualMap = new ParameterExpression(inputString);

    // Assert
    // Verify that the parsed map is identical to the expected map.
    // This single assertion checks size, keys, and values, making the test more concise.
    assertEquals(expectedMap, actualMap);
  }
}