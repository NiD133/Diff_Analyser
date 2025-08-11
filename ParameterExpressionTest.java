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

import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ParameterExpression parsing functionality.
 * 
 * ParameterExpression parses MyBatis parameter expressions with the following grammar:
 * - Simple properties: "propertyName"
 * - Expressions: "(expression)"
 * - Old-style JDBC types: "property:JDBC_TYPE"
 * - Attributes: "property,attr1=val1,attr2=val2"
 */
class ParameterExpressionTest {

  // Constants for better readability
  private static final String PROPERTY_KEY = "property";
  private static final String EXPRESSION_KEY = "expression";
  private static final String JDBC_TYPE_KEY = "jdbcType";

  @Test
  void shouldParseSimpleProperty() {
    // Given
    String parameterExpression = "id";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(parameterExpression);
    
    // Then
    assertThat(parsedResult)
        .hasSize(1)
        .containsEntry(PROPERTY_KEY, "id");
  }

  @Test
  void shouldTrimWhitespaceFromProperty() {
    // Given
    String parameterExpressionWithSpaces = " with spaces ";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(parameterExpressionWithSpaces);
    
    // Then
    assertThat(parsedResult)
        .hasSize(1)
        .containsEntry(PROPERTY_KEY, "with spaces");
  }

  @Test
  void shouldParsePropertyWithOldStyleJdbcType() {
    // Given
    String propertyWithJdbcType = "id:VARCHAR";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(propertyWithJdbcType);
    
    // Then
    assertThat(parsedResult)
        .hasSize(2)
        .containsEntry(PROPERTY_KEY, "id")
        .containsEntry(JDBC_TYPE_KEY, "VARCHAR");
  }

  @Test
  void shouldHandleWhitespaceInOldStyleJdbcType() {
    // Given
    String jdbcTypeWithWhitespace = " id :  VARCHAR ";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(jdbcTypeWithWhitespace);
    
    // Then
    assertThat(parsedResult)
        .hasSize(2)
        .containsEntry(PROPERTY_KEY, "id")
        .containsEntry(JDBC_TYPE_KEY, "VARCHAR");
  }

  @Test
  void shouldParseExpressionWithOldStyleJdbcType() {
    // Given
    String expressionWithJdbcType = "(id.toString()):VARCHAR";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(expressionWithJdbcType);
    
    // Then
    assertThat(parsedResult)
        .hasSize(2)
        .containsEntry(EXPRESSION_KEY, "id.toString()")
        .containsEntry(JDBC_TYPE_KEY, "VARCHAR");
  }

  @Test
  void shouldParsePropertyWithSingleAttribute() {
    // Given
    String propertyWithAttribute = "id,name=value";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(propertyWithAttribute);
    
    // Then
    assertThat(parsedResult)
        .hasSize(2)
        .containsEntry(PROPERTY_KEY, "id")
        .containsEntry("name", "value");
  }

  @Test
  void shouldParseExpressionWithSingleAttribute() {
    // Given
    String expressionWithAttribute = "(id.toString()),name=value";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(expressionWithAttribute);
    
    // Then
    assertThat(parsedResult)
        .hasSize(2)
        .containsEntry(EXPRESSION_KEY, "id.toString()")
        .containsEntry("name", "value");
  }

  @Test
  void shouldParsePropertyWithMultipleAttributes() {
    // Given
    String propertyWithMultipleAttributes = "id, attr1=val1, attr2=val2, attr3=val3";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(propertyWithMultipleAttributes);
    
    // Then
    assertThat(parsedResult)
        .hasSize(4)
        .containsEntry(PROPERTY_KEY, "id")
        .containsEntry("attr1", "val1")
        .containsEntry("attr2", "val2")
        .containsEntry("attr3", "val3");
  }

  @Test
  void shouldParseExpressionWithMultipleAttributes() {
    // Given
    String expressionWithMultipleAttributes = "(id.toString()), attr1=val1, attr2=val2, attr3=val3";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(expressionWithMultipleAttributes);
    
    // Then
    assertThat(parsedResult)
        .hasSize(4)
        .containsEntry(EXPRESSION_KEY, "id.toString()")
        .containsEntry("attr1", "val1")
        .containsEntry("attr2", "val2")
        .containsEntry("attr3", "val3");
  }

  @Test
  void shouldParseComplexExpressionWithJdbcTypeAndAttributes() {
    // Given
    String complexExpression = "id:VARCHAR, attr1=val1, attr2=val2";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(complexExpression);
    
    // Then
    assertThat(parsedResult)
        .hasSize(4)
        .containsEntry(PROPERTY_KEY, "id")
        .containsEntry(JDBC_TYPE_KEY, "VARCHAR")
        .containsEntry("attr1", "val1")
        .containsEntry("attr2", "val2");
  }

  @Test
  void shouldHandlePropertyNamesWithSpaces() {
    // Given
    String propertyNameWithSpaces = "user name, attr1=val1, attr2=val2, attr3=val3";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(propertyNameWithSpaces);
    
    // Then
    assertThat(parsedResult)
        .hasSize(4)
        .containsEntry(PROPERTY_KEY, "user name")
        .containsEntry("attr1", "val1")
        .containsEntry("attr2", "val2")
        .containsEntry("attr3", "val3");
  }

  @Test
  void shouldIgnoreLeadingAndTrailingWhitespace() {
    // Given
    String expressionWithExcessiveWhitespace = " id , jdbcType =  VARCHAR,  attr1 = val1 ,  attr2 = val2 ";
    
    // When
    Map<String, String> parsedResult = new ParameterExpression(expressionWithExcessiveWhitespace);
    
    // Then
    assertThat(parsedResult)
        .hasSize(4)
        .containsEntry(PROPERTY_KEY, "id")
        .containsEntry(JDBC_TYPE_KEY, "VARCHAR")
        .containsEntry("attr1", "val1")
        .containsEntry("attr2", "val2");
  }

  @Test
  void shouldThrowExceptionForInvalidOldJdbcTypeFormat() {
    // Given
    String invalidJdbcTypeExpression = "id:";
    
    // When & Then
    BuilderException exception = assertThrows(BuilderException.class, 
        () -> new ParameterExpression(invalidJdbcTypeExpression));
    
    assertThat(exception.getMessage())
        .contains("Parsing error in {id:} in position 3");
  }

  @Test
  void shouldThrowExceptionForInvalidExpressionSyntax() {
    // Given
    String invalidExpressionSyntax = "(expression)+";
    
    // When & Then
    BuilderException exception = assertThrows(BuilderException.class, 
        () -> new ParameterExpression(invalidExpressionSyntax));
    
    assertThat(exception.getMessage())
        .contains("Parsing error in {(expression)+} in position 12");
  }

  // Helper method to create more fluent assertions
  private static ParameterExpressionAssert assertThat(Map<String, String> actual) {
    return new ParameterExpressionAssert(actual);
  }

  // Custom assertion class for better readability
  private static class ParameterExpressionAssert {
    private final Map<String, String> actual;

    public ParameterExpressionAssert(Map<String, String> actual) {
      this.actual = actual;
    }

    public ParameterExpressionAssert hasSize(int expectedSize) {
      assertEquals(expectedSize, actual.size(), 
          () -> String.format("Expected size <%d> but was <%d>", expectedSize, actual.size()));
      return this;
    }

    public ParameterExpressionAssert containsEntry(String key, String value) {
      assertEquals(value, actual.get(key), 
          () -> String.format("Expected entry <%s=%s> but was <%s=%s>", 
              key, value, key, actual.get(key)));
      return this;
    }
  }
}