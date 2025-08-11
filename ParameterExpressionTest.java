/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.builder;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.apache.ibatis.builder.BuilderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ParameterExpression Parser")
class ParameterExpressionTest {

  @Nested
  @DisplayName("when parsing core components")
  class CoreParsing {

    @Test
    @DisplayName("should parse a simple property")
    void shouldParseSimpleProperty() {
      // Given
      String expression = "id";
      Map<String, String> expected = Map.of("property", "id");

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should parse a property with internal spaces")
    void shouldParsePropertyWithInternalSpaces() {
      // Given
      String expression = "user name";
      Map<String, String> expected = Map.of("property", "user name");

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should parse a simple expression")
    void shouldParseSimpleExpression() {
      // Given
      String expression = "(id.toString())";
      Map<String, String> expected = Map.of("expression", "id.toString()");

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }
  }

  @Nested
  @DisplayName("when parsing with attributes")
  class WithAttributes {

    @Test
    @DisplayName("should parse a property with a single attribute")
    void shouldParsePropertyWithSingleAttribute() {
      // Given
      String expression = "id,name=value";
      Map<String, String> expected = Map.of("property", "id", "name", "value");

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should parse an expression with a single attribute")
    void shouldParseExpressionWithSingleAttribute() {
      // Given
      String expression = "(id.toString()),name=value";
      Map<String, String> expected = Map.of("expression", "id.toString()", "name", "value");

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should parse a property with multiple attributes")
    void shouldParsePropertyWithMultipleAttributes() {
      // Given
      String expression = "id, attr1=val1, attr2=val2, attr3=val3";
      Map<String, String> expected = Map.ofEntries(
          entry("property", "id"),
          entry("attr1", "val1"),
          entry("attr2", "val2"),
          entry("attr3", "val3")
      );

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should parse an expression with multiple attributes")
    void shouldParseExpressionWithMultipleAttributes() {
      // Given
      String expression = "(id.toString()), attr1=val1, attr2=val2, attr3=val3";
      Map<String, String> expected = Map.ofEntries(
          entry("expression", "id.toString()"),
          entry("attr1", "val1"),
          entry("attr2", "val2"),
          entry("attr3", "val3")
      );

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }
  }

  @Nested
  @DisplayName("when parsing with JDBC type")
  class WithJdbcType {

    @Test
    @DisplayName("should parse a property with a JDBC type")
    void shouldParsePropertyWithJdbcType() {
      // Given
      String expression = "id:VARCHAR";
      Map<String, String> expected = Map.of("property", "id", "jdbcType", "VARCHAR");

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should parse an expression with a JDBC type")
    void shouldParseExpressionWithJdbcType() {
      // Given
      String expression = "(id.toString()):VARCHAR";
      Map<String, String> expected = Map.of("expression", "id.toString()", "jdbcType", "VARCHAR");

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }
  }

  @Nested
  @DisplayName("when parsing with JDBC type and attributes")
  class WithJdbcTypeAndAttributes {

    @Test
    @DisplayName("should parse a property with JDBC type and multiple attributes")
    void shouldParsePropertyWithJdbcTypeAndAttributes() {
      // Given
      String expression = "id:VARCHAR, attr1=val1, attr2=val2";
      Map<String, String> expected = Map.ofEntries(
          entry("property", "id"),
          entry("jdbcType", "VARCHAR"),
          entry("attr1", "val1"),
          entry("attr2", "val2")
      );

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }
  }

  @Nested
  @DisplayName("when handling whitespace")
  class WhitespaceHandling {

    @Test
    @DisplayName("should ignore whitespace around all parts of the expression")
    void shouldIgnoreWhitespaceAroundAllParts() {
      // Given
      String expression = " id , jdbcType =  VARCHAR,  attr1 = val1 ,  attr2 = val2 ";
      Map<String, String> expected = Map.ofEntries(
          entry("property", "id"),
          entry("jdbcType", "VARCHAR"),
          entry("attr1", "val1"),
          entry("attr2", "val2")
      );

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should trim leading and trailing whitespace from the entire expression")
    void shouldTrimWhitespaceFromEntireExpression() {
      // Given
      String expression = " with spaces ";
      Map<String, String> expected = Map.of("property", "with spaces");

      // When
      Map<String, String> actual = new ParameterExpression(expression);

      // Then
      assertEquals(expected, actual);
    }
  }

  @Nested
  @DisplayName("when parsing invalid expressions")
  class InvalidExpressions {

    @Test
    @DisplayName("should throw exception for incomplete JDBC type")
    void shouldThrowExceptionForIncompleteJdbcType() {
      // Given
      String invalidExpression = "id:";

      // When & Then
      BuilderException exception = assertThrows(BuilderException.class, () -> new ParameterExpression(invalidExpression));
      assertTrue(exception.getMessage().contains("Parsing error in {id:} in position 3"));
    }

    @Test
    @DisplayName("should throw exception for malformed expression")
    void shouldThrowExceptionForMalformedExpression() {
      // Given
      String invalidExpression = "(expression)+";

      // When & Then
      BuilderException exception = assertThrows(BuilderException.class, () -> new ParameterExpression(invalidExpression));
      assertTrue(exception.getMessage().contains("Parsing error in {(expression)+} in position 12"));
    }
  }
}