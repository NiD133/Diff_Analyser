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
import java.sql.Types;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Tests for {@link ArrayTypeHandler} which handles conversion between Java arrays
 * and SQL ARRAY types.
 */
class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

  private static final TypeHandler<Object> TYPE_HANDLER = new ArrayTypeHandler();
  
  // Test data constants
  private static final String[] SAMPLE_STRING_ARRAY = { "a", "b" };
  private static final String[] HELLO_WORLD_ARRAY = { "Hello World" };
  private static final String TEST_COLUMN_NAME = "column";
  private static final int TEST_COLUMN_INDEX = 1;

  @Mock
  Array mockArray;

  // ========== Parameter Setting Tests ==========

  @Override
  @Test
  public void shouldSetParameter() throws Exception {
    // Given: a SQL Array parameter
    // When: setting the parameter on prepared statement
    TYPE_HANDLER.setParameter(ps, TEST_COLUMN_INDEX, mockArray, null);
    
    // Then: the array should be set directly
    verify(ps).setArray(TEST_COLUMN_INDEX, mockArray);
  }

  @Test
  void shouldSetStringArrayParameter_AndFreeResourcesAfterwards() throws Exception {
    // Given: a connection that can create arrays
    Connection connection = mock(Connection.class);
    when(ps.getConnection()).thenReturn(connection);

    Array createdArray = mock(Array.class);
    when(connection.createArrayOf(anyString(), any(String[].class))).thenReturn(createdArray);

    // When: setting a Java String array as parameter
    TYPE_HANDLER.setParameter(ps, TEST_COLUMN_INDEX, HELLO_WORLD_ARRAY, JdbcType.ARRAY);
    
    // Then: should create SQL Array from Java array and free resources
    verify(ps).setArray(TEST_COLUMN_INDEX, createdArray);
    verify(createdArray).free();
  }

  @Test
  void shouldSetNullParameter_WhenValueIsNull() throws Exception {
    // When: setting null as array parameter
    TYPE_HANDLER.setParameter(ps, TEST_COLUMN_INDEX, null, JdbcType.ARRAY);
    
    // Then: should set NULL with ARRAY type
    verify(ps).setNull(TEST_COLUMN_INDEX, Types.ARRAY);
  }

  @Test
  void shouldFailForNonArrayParameter_WhenInvalidTypeProvided() {
    // Given: a non-array parameter (String)
    String invalidParameter = "unsupported parameter type";
    
    // When & Then: should throw TypeException
    assertThrows(TypeException.class, 
        () -> TYPE_HANDLER.setParameter(ps, TEST_COLUMN_INDEX, invalidParameter, null));
  }

  // ========== ResultSet Retrieval Tests (by name) ==========

  @Override
  @Test
  public void shouldGetResultFromResultSetByName() throws Exception {
    // Given: ResultSet returns a SQL Array containing string data
    when(rs.getArray(TEST_COLUMN_NAME)).thenReturn(mockArray);
    when(mockArray.getArray()).thenReturn(SAMPLE_STRING_ARRAY);
    
    // When: retrieving array by column name
    Object result = TYPE_HANDLER.getResult(rs, TEST_COLUMN_NAME);
    
    // Then: should return the Java array and free SQL Array resources
    assertEquals(SAMPLE_STRING_ARRAY, result);
    verify(mockArray).free();
  }

  @Override
  @Test
  public void shouldGetResultNullFromResultSetByName() throws Exception {
    // Given: ResultSet returns null for the column
    when(rs.getArray(TEST_COLUMN_NAME)).thenReturn(null);
    
    // When: retrieving array by column name
    Object result = TYPE_HANDLER.getResult(rs, TEST_COLUMN_NAME);
    
    // Then: should return null
    assertNull(result);
  }

  // ========== ResultSet Retrieval Tests (by index) ==========

  @Override
  @Test
  public void shouldGetResultFromResultSetByPosition() throws Exception {
    // Given: ResultSet returns a SQL Array at position 1
    when(rs.getArray(TEST_COLUMN_INDEX)).thenReturn(mockArray);
    when(mockArray.getArray()).thenReturn(SAMPLE_STRING_ARRAY);
    
    // When: retrieving array by column index
    Object result = TYPE_HANDLER.getResult(rs, TEST_COLUMN_INDEX);
    
    // Then: should return the Java array and free SQL Array resources
    assertEquals(SAMPLE_STRING_ARRAY, result);
    verify(mockArray).free();
  }

  @Override
  @Test
  public void shouldGetResultNullFromResultSetByPosition() throws Exception {
    // Given: ResultSet returns null at position 1
    when(rs.getArray(TEST_COLUMN_INDEX)).thenReturn(null);
    
    // When: retrieving array by column index
    Object result = TYPE_HANDLER.getResult(rs, TEST_COLUMN_INDEX);
    
    // Then: should return null
    assertNull(result);
  }

  // ========== CallableStatement Retrieval Tests ==========

  @Override
  @Test
  public void shouldGetResultFromCallableStatement() throws Exception {
    // Given: CallableStatement returns a SQL Array
    when(cs.getArray(TEST_COLUMN_INDEX)).thenReturn(mockArray);
    when(mockArray.getArray()).thenReturn(SAMPLE_STRING_ARRAY);
    
    // When: retrieving array from callable statement
    Object result = TYPE_HANDLER.getResult(cs, TEST_COLUMN_INDEX);
    
    // Then: should return the Java array and free SQL Array resources
    assertEquals(SAMPLE_STRING_ARRAY, result);
    verify(mockArray).free();
  }

  @Override
  @Test
  public void shouldGetResultNullFromCallableStatement() throws Exception {
    // Given: CallableStatement returns null
    when(cs.getArray(TEST_COLUMN_INDEX)).thenReturn(null);
    
    // When: retrieving array from callable statement
    Object result = TYPE_HANDLER.getResult(cs, TEST_COLUMN_INDEX);
    
    // Then: should return null
    assertNull(result);
  }
}