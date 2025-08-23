package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ParameterExpression}.
 */
class ParameterExpressionTest {

  @Test
  @DisplayName("Should parse a property with spaces and multiple attributes")
  void shouldParsePropertyWithSpaceAndMultipleAttributes() {
    // Arrange
    String expression = "user name, attr1=val1, attr2=val2, attr3=val3";
    Map<String, String> expectedParameters = Map.of(
        "property", "user name",
        "attr1", "val1",
        "attr2", "val2",
        "attr3", "val3"
    );

    // Act
    ParameterExpression actualParameters = new ParameterExpression(expression);

    // Assert
    assertEquals(expectedParameters, actualParameters);
  }
}