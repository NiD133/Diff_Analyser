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
package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ArrayTypeHandlerTest {

  private TypeHandler<Object> typeHandler;

  @Mock
  PreparedStatement preparedStatement;
  @Mock
  ResultSet resultSet;
  @Mock
  CallableStatement callableStatement;
  @Mock
  Array mockArray;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    typeHandler = new ArrayTypeHandler();
  }

  @Test
  void shouldSetParameterWithArray() throws Exception {
    // When
    typeHandler.setParameter(preparedStatement, 1, mockArray, null);

    // Then
    verify(preparedStatement).setArray(1, mockArray);
  }

  @Test
  void shouldSetParameterWithStringArray() throws Exception {
    // Given
    Connection connection = mock(Connection.class);
    when(preparedStatement.getConnection()).thenReturn(connection);

    Array array = mock(Array.class);
    when(connection.createArrayOf(anyString(), any(String[].class))).thenReturn(array);

    String[] testArray = { "Hello World" };

    // When
    typeHandler.setParameter(preparedStatement, 1, testArray, JdbcType.ARRAY);

    // Then
    verify(preparedStatement).setArray(1, array);
    verify(array).free();
  }

  @Test
  void shouldSetParameterWithNull() throws Exception {
    // When
    typeHandler.setParameter(preparedStatement, 1, null, JdbcType.ARRAY);

    // Then
    verify(preparedStatement).setNull(1, Types.ARRAY);
  }

  @Test
  void shouldThrowExceptionForNonArrayParameter() {
    // When
    assertThrows(TypeException.class, () -> typeHandler.setParameter(preparedStatement, 1, "unsupported parameter type", null));
  }

  @Test
  void shouldGetResultFromStringColumnName() throws Exception {
    // Given
    String columnName = "column";
    String[] expectedArray = { "a", "b" };
    when(resultSet.getArray(columnName)).thenReturn(mockArray);
    when(mockArray.getArray()).thenReturn(expectedArray);

    // When
    Object actualArray = typeHandler.getResult(resultSet, columnName);

    // Then
    assertEquals(expectedArray, actualArray);
    verify(mockArray).free();
  }

  @Test
  void shouldGetNullResultFromStringColumnName() throws Exception {
    // Given
    String columnName = "column";
    when(resultSet.getArray(columnName)).thenReturn(null);

    // When
    Object result = typeHandler.getResult(resultSet, columnName);

    // Then
    assertNull(result);
  }

  @Test
  void shouldGetResultFromIntColumnIndex() throws Exception {
    // Given
    int columnIndex = 1;
    String[] expectedArray = { "a", "b" };
    when(resultSet.getArray(columnIndex)).thenReturn(mockArray);
    when(mockArray.getArray()).thenReturn(expectedArray);

    // When
    Object actualArray = typeHandler.getResult(resultSet, columnIndex);

    // Then
    assertEquals(expectedArray, actualArray);
    verify(mockArray).free();
  }

  @Test
  void shouldGetNullResultFromIntColumnIndex() throws Exception {
    // Given
    int columnIndex = 1;
    when(resultSet.getArray(columnIndex)).thenReturn(null);

    // When
    Object result = typeHandler.getResult(resultSet, columnIndex);

    // Then
    assertNull(result);
  }

  @Test
  void shouldGetResultFromCallableStatementIndex() throws Exception {
      // Given
      int columnIndex = 1;
      String[] expectedArray = { "a", "b" };
      when(callableStatement.getArray(columnIndex)).thenReturn(mockArray);
      when(mockArray.getArray()).thenReturn(expectedArray);

      // When
      Object actualArray = typeHandler.getResult(callableStatement, columnIndex);

      // Then
      assertEquals(expectedArray, actualArray);
      verify(mockArray).free();
  }

  @Test
  void shouldGetNullResultFromCallableStatementIndex() throws Exception {
      // Given
      int columnIndex = 1;
      when(callableStatement.getArray(columnIndex)).thenReturn(null);

      // When
      Object result = typeHandler.getResult(callableStatement, columnIndex);

      // Then
      assertNull(result);
  }
}