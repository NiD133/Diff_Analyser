package org.apache.ibatis.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for ParameterExpression which parses inline parameter expressions.
 * These tests focus on readability by:
 * - Using small helpers to remove repetitive assertion boilerplate
 * - Grouping related scenarios with @Nested classes
 * - Naming tests to clearly describe behavior
 */
class ParameterExpressionTest {

  private static final String PROPERTY = "property";
  private static final String EXPRESSION = "expression";
  private static final String JDBC_TYPE = "jdbcType";

  /**
   * Parses the input and asserts the map contains exactly the provided key-value pairs.
   * The expected map size is derived from the number of pairs.
   */
  private static void assertParsed(String input, String... keyValuePairs) {
    if (keyValuePairs.length % 2 != 0) {
      throw new IllegalArgumentException("keyValuePairs must be even length (key, value, ...)");
    }
    Map<String, String> result = new ParameterExpression(input);
    int expectedSize = keyValuePairs.length / 2;
    assertEquals(expectedSize, result.size(), "Unexpected number of parsed entries for: " + input);
    for (int i = 0; i < keyValuePairs.length; i += 2) {
      String key = keyValuePairs[i];
      String value = keyValuePairs[i + 1];
      assertEquals(value, result.get(key), "Unexpected value for key '" + key + "' for input: " + input);
    }
  }

  @Nested
  @DisplayName("Property parsing")
  class PropertyParsing {

    @Test
    @DisplayName("Parses a simple property")
    void parsesSimpleProperty() {
      assertParsed("id", PROPERTY, "id");
    }

    @Test
    @DisplayName("Trims outer whitespace but preserves inner spaces in property name")
    void parsesPropertyTrimmingOuterWhitespace() {
      assertParsed(" with spaces ", PROPERTY, "with spaces");
    }

    @Test
    @DisplayName("Parses property with attributes")
    void parsesPropertyWithOneAttribute() {
      assertParsed("id,name=value", PROPERTY, "id", "name", "value");
    }

    @Test
    @DisplayName("Parses property with multiple attributes")
    void parsesPropertyWithManyAttributes() {
      assertParsed("id, attr1=val1, attr2=val2, attr3=val3",
          PROPERTY, "id",
          "attr1", "val1",
          "attr2", "val2",
          "attr3", "val3");
    }

    @Test
    @DisplayName("Parses property containing spaces plus multiple attributes")
    void parsesPropertyWithSpaceAndManyAttributes() {
      assertParsed("user name, attr1=val1, attr2=val2, attr3=val3",
          PROPERTY, "user name",
          "attr1", "val1",
          "attr2", "val2",
          "attr3", "val3");
    }
  }

  @Nested
  @DisplayName("Expression parsing (parenthesized)")
  class ExpressionParsing {

    @Test
    @DisplayName("Parses expression with attributes")
    void parsesExpressionWithOneAttribute() {
      assertParsed("(id.toString()),name=value", EXPRESSION, "id.toString()", "name", "value");
    }

    @Test
    @DisplayName("Parses expression with multiple attributes")
    void parsesExpressionWithManyAttributes() {
      assertParsed("(id.toString()), attr1=val1, attr2=val2, attr3=val3",
          EXPRESSION, "id.toString()",
          "attr1", "val1",
          "attr2", "val2",
          "attr3", "val3");
    }
  }

  @Nested
  @DisplayName("Old style JDBC type parsing (colon syntax)")
  class OldStyleJdbcTypeParsing {

    @Test
    @DisplayName("Parses property and old style JDBC type")
    void parsesSimplePropertyWithOldStyleJdbcType() {
      assertParsed("id:VARCHAR", PROPERTY, "id", JDBC_TYPE, "VARCHAR");
    }

    @Test
    @DisplayName("Ignores extra whitespace around property and JDBC type")
    void parsesOldStyleJdbcTypeWithExtraWhitespace() {
      assertParsed(" id :  VARCHAR ", PROPERTY, "id", JDBC_TYPE, "VARCHAR");
    }

    @Test
    @DisplayName("Parses expression and old style JDBC type")
    void parsesExpressionWithOldStyleJdbcType() {
      assertParsed("(id.toString()):VARCHAR", EXPRESSION, "id.toString()", JDBC_TYPE, "VARCHAR");
    }

    @Test
    @DisplayName("Parses property, old style JDBC type and attributes")
    void parsesPropertyWithOldStyleJdbcTypeAndAttributes() {
      assertParsed("id:VARCHAR, attr1=val1, attr2=val2",
          PROPERTY, "id",
          JDBC_TYPE, "VARCHAR",
          "attr1", "val1",
          "attr2", "val2");
    }
  }

  @Nested
  @DisplayName("Whitespace handling")
  class WhitespaceHandling {

    @Test
    @DisplayName("Trims leading/trailing spaces around tokens and separators")
    void ignoresLeadingAndTrailingSpaces() {
      assertParsed(" id , jdbcType =  VARCHAR,  attr1 = val1 ,  attr2 = val2 ",
          PROPERTY, "id",
          JDBC_TYPE, "VARCHAR",
          "attr1", "val1",
          "attr2", "val2");
    }
  }

  @Nested
  @DisplayName("Invalid inputs")
  class InvalidInputs {

    @Test
    @DisplayName("Fails when old style JDBC type is missing the type")
    void invalidOldJdbcTypeFormat() {
      BuilderException e = assertThrows(BuilderException.class, () -> new ParameterExpression("id:"));
      assertTrue(e.getMessage().contains("Parsing error in {id:} in position 3"));
    }

    @Test
    @DisplayName("Fails when expression is followed by invalid jdbcType/options syntax")
    void invalidJdbcTypeOptUsingExpression() {
      BuilderException e = assertThrows(BuilderException.class, () -> new ParameterExpression("(expression)+"));
      assertTrue(e.getMessage().contains("Parsing error in {(expression)+} in position 12"));
    }
  }
}