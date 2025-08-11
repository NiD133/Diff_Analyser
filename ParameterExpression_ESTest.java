package org.apache.ibatis.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readable tests for ParameterExpression parsing.
 *
 * The parser accepts strings of the form:
 *   (propertyName | (expression)) [:oldJdbcType] [, name=value]*
 *
 * Examples:
 *   "id"
 *   "(a + b)"
 *   "id:VARCHAR"
 *   "id, jdbcType=NUMERIC, javaType=int"
 *   "(x+y):INTEGER, scale=2"
 */
class ParameterExpressionTest {

  @Test
  @DisplayName("Parses a simple property name")
  void parsesSimpleProperty() {
    ParameterExpression pe = new ParameterExpression("username");

    assertEquals(1, pe.size());
    assertEquals("username", pe.get("property"));
  }

  @Test
  @DisplayName("Parses an expression wrapped in parentheses")
  void parsesExpression() {
    ParameterExpression pe = new ParameterExpression("(a + b)");

    assertEquals(1, pe.size());
    assertEquals("a + b", pe.get("expression"));
  }

  @Test
  @DisplayName("Parses old-style JDBC type specified with ':'")
  void parsesOldStyleJdbcType() {
    ParameterExpression pe = new ParameterExpression("id:INTEGER");

    assertEquals(2, pe.size());
    assertEquals("id", pe.get("property"));
    assertEquals("INTEGER", pe.get("jdbcType"));
  }

  @Test
  @DisplayName("Parses attributes written as name=value")
  void parsesAttributes() {
    ParameterExpression pe = new ParameterExpression("id, javaType=int, jdbcType=NUMERIC, mode=IN");

    assertEquals(4, pe.size());
    assertEquals("id", pe.get("property"));
    assertEquals("int", pe.get("javaType"));
    assertEquals("NUMERIC", pe.get("jdbcType"));
    assertEquals("IN", pe.get("mode"));
  }

  @Test
  @DisplayName("Trims whitespace around tokens")
  void trimsWhitespace() {
    ParameterExpression pe = new ParameterExpression("  id  :  VARCHAR  ,  mode  =  OUT  ");

    assertEquals(3, pe.size());
    assertEquals("id", pe.get("property"));
    assertEquals("VARCHAR", pe.get("jdbcType"));
    assertEquals("OUT", pe.get("mode"));
  }

  @Test
  @DisplayName("Attribute values may be single-quoted or double-quoted")
  void supportsQuotedAttributeValues() {
    ParameterExpression singleQuoted = new ParameterExpression("id, typeHandler='com.example.MyHandler'");
    assertEquals("com.example.MyHandler", singleQuoted.get("typeHandler"));

    ParameterExpression doubleQuoted = new ParameterExpression("id, typeHandler=\"com.example.MyHandler\"");
    assertEquals("com.example.MyHandler", doubleQuoted.get("typeHandler"));
  }

  @Test
  @DisplayName("Combines expression with JDBC type and additional attributes")
  void expressionWithJdbcTypeAndAttributes() {
    ParameterExpression pe = new ParameterExpression("(x + y):INTEGER, scale=2");

    assertEquals(3, pe.size());
    assertEquals("x + y", pe.get("expression"));
    assertEquals("INTEGER", pe.get("jdbcType"));
    assertEquals("2", pe.get("scale"));
  }

  @Test
  @DisplayName("Null expression is not allowed")
  void nullExpressionThrowsNpe() {
    assertThrows(NullPointerException.class, () -> new ParameterExpression(null));
  }

  @Test
  @DisplayName("Unclosed parenthesis yields a parsing error")
  void unclosedParenthesis() {
    RuntimeException ex = assertThrows(RuntimeException.class, () -> new ParameterExpression("(abc"));
    // Message format may change; only check that it's a parsing error indicator if present.
    if (ex.getMessage() != null) {
      assertTrue(ex.getMessage().contains("Parsing error"));
    }
  }

  @Test
  @DisplayName("Missing '=' in an attribute yields a parsing error")
  void missingEqualsInAttribute() {
    assertThrows(RuntimeException.class, () -> new ParameterExpression("id, jdbcType"));
  }

  @Test
  @DisplayName("Trailing comma yields a parsing error")
  void trailingComma() {
    assertThrows(RuntimeException.class, () -> new ParameterExpression("id,"));
  }
}