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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ParameterExpressionTest {

    @Test
    void shouldParseSimpleProperty() {
        // Given
        String input = "id";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(1, result.size(), "Should have exactly one entry"),
            () -> Assertions.assertEquals("id", result.get("property"), "Should parse property correctly")
        );
    }

    @Test
    void shouldParsePropertyWithInternalSpaces() {
        // Given
        String input = " with spaces ";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(1, result.size(), "Should have exactly one entry"),
            () -> Assertions.assertEquals("with spaces", result.get("property"), "Should preserve internal spaces")
        );
    }

    @Test
    void shouldParsePropertyWithJdbcType() {
        // Given
        String input = "id:VARCHAR";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(2, result.size(), "Should have two entries"),
            () -> Assertions.assertEquals("id", result.get("property"), "Should parse property correctly"),
            () -> Assertions.assertEquals("VARCHAR", result.get("jdbcType"), "Should parse JDBC type correctly")
        );
    }

    @Test
    void shouldParsePropertyWithJdbcTypeAndWhitespace() {
        // Given
        String input = " id :  VARCHAR ";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(2, result.size(), "Should have two entries"),
            () -> Assertions.assertEquals("id", result.get("property"), "Should trim property"),
            () -> Assertions.assertEquals("VARCHAR", result.get("jdbcType"), "Should trim JDBC type")
        );
    }

    @Test
    void shouldParseExpressionWithJdbcType() {
        // Given
        String input = "(id.toString()):VARCHAR";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(2, result.size(), "Should have two entries"),
            () -> Assertions.assertEquals("id.toString()", result.get("expression"), "Should parse expression correctly"),
            () -> Assertions.assertEquals("VARCHAR", result.get("jdbcType"), "Should parse JDBC type correctly")
        );
    }

    @Test
    void shouldParsePropertyWithSingleAttribute() {
        // Given
        String input = "id,name=value";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(2, result.size(), "Should have two entries"),
            () -> Assertions.assertEquals("id", result.get("property"), "Should parse property correctly"),
            () -> Assertions.assertEquals("value", result.get("name"), "Should parse attribute correctly")
        );
    }

    @Test
    void shouldParseExpressionWithSingleAttribute() {
        // Given
        String input = "(id.toString()),name=value";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(2, result.size(), "Should have two entries"),
            () -> Assertions.assertEquals("id.toString()", result.get("expression"), "Should parse expression correctly"),
            () -> Assertions.assertEquals("value", result.get("name"), "Should parse attribute correctly")
        );
    }

    @Test
    void shouldParsePropertyWithMultipleAttributes() {
        // Given
        String input = "id, attr1=val1, attr2=val2, attr3=val3";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(4, result.size(), "Should have four entries"),
            () -> Assertions.assertEquals("id", result.get("property"), "Should parse property correctly"),
            () -> Assertions.assertEquals("val1", result.get("attr1"), "Should parse attr1 correctly"),
            () -> Assertions.assertEquals("val2", result.get("attr2"), "Should parse attr2 correctly"),
            () -> Assertions.assertEquals("val3", result.get("attr3"), "Should parse attr3 correctly")
        );
    }

    @Test
    void shouldParseExpressionWithMultipleAttributes() {
        // Given
        String input = "(id.toString()), attr1=val1, attr2=val2, attr3=val3";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(4, result.size(), "Should have four entries"),
            () -> Assertions.assertEquals("id.toString()", result.get("expression"), "Should parse expression correctly"),
            () -> Assertions.assertEquals("val1", result.get("attr1"), "Should parse attr1 correctly"),
            () -> Assertions.assertEquals("val2", result.get("attr2"), "Should parse attr2 correctly"),
            () -> Assertions.assertEquals("val3", result.get("attr3"), "Should parse attr3 correctly")
        );
    }

    @Test
    void shouldParsePropertyWithJdbcTypeAndAttributes() {
        // Given
        String input = "id:VARCHAR, attr1=val1, attr2=val2";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(4, result.size(), "Should have four entries"),
            () -> Assertions.assertEquals("id", result.get("property"), "Should parse property correctly"),
            () -> Assertions.assertEquals("VARCHAR", result.get("jdbcType"), "Should parse JDBC type correctly"),
            () -> Assertions.assertEquals("val1", result.get("attr1"), "Should parse attr1 correctly"),
            () -> Assertions.assertEquals("val2", result.get("attr2"), "Should parse attr2 correctly")
        );
    }

    @Test
    void shouldParsePropertyWithSpacesAndAttributes() {
        // Given
        String input = "user name, attr1=val1, attr2=val2, attr3=val3";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(4, result.size(), "Should have four entries"),
            () -> Assertions.assertEquals("user name", result.get("property"), "Should preserve spaces in property"),
            () -> Assertions.assertEquals("val1", result.get("attr1"), "Should parse attr1 correctly"),
            () -> Assertions.assertEquals("val2", result.get("attr2"), "Should parse attr2 correctly"),
            () -> Assertions.assertEquals("val3", result.get("attr3"), "Should parse attr3 correctly")
        );
    }

    @Test
    void shouldIgnoreWhitespaceAroundEntries() {
        // Given
        String input = " id , jdbcType =  VARCHAR,  attr1 = val1 ,  attr2 = val2 ";

        // When
        Map<String, String> result = new ParameterExpression(input);

        // Then
        Assertions.assertAll(
            () -> Assertions.assertEquals(4, result.size(), "Should have four entries"),
            () -> Assertions.assertEquals("id", result.get("property"), "Should trim property"),
            () -> Assertions.assertEquals("VARCHAR", result.get("jdbcType"), "Should trim JDBC type"),
            () -> Assertions.assertEquals("val1", result.get("attr1"), "Should trim attribute value"),
            () -> Assertions.assertEquals("val2", result.get("attr2"), "Should trim attribute value")
        );
    }

    @Test
    void shouldThrowExceptionForInvalidJdbcTypeFormat() {
        // Given
        String input = "id:";
        String expectedError = "Parsing error in {id:} in position 3";

        // When & Then
        BuilderException exception = Assertions.assertThrows(BuilderException.class,
            () -> new ParameterExpression(input),
            "Should throw BuilderException for invalid format"
        );
        Assertions.assertTrue(exception.getMessage().contains(expectedError),
            "Exception message should contain parsing details");
    }

    @Test
    void shouldThrowExceptionForInvalidExpressionFormat() {
        // Given
        String input = "(expression)+";
        String expectedError = "Parsing error in {(expression)+} in position 12";

        // When & Then
        BuilderException exception = Assertions.assertThrows(BuilderException.class,
            () -> new ParameterExpression(input),
            "Should throw BuilderException for invalid expression"
        );
        Assertions.assertTrue(exception.getMessage().contains(expectedError),
            "Exception message should contain parsing details");
    }
}