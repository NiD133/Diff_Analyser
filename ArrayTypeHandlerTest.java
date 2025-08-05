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
import java.sql.SQLException;
import java.sql.Types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

  private static final ArrayTypeHandler ARRAY_TYPE_HANDLER = new ArrayTypeHandler();

  @Mock
  private Array mockSqlArray;

  @Nested
  @DisplayName("setParameter tests")
  class SetParameterTests {

    @Test
    @DisplayName("should set a non-null java.sql.Array parameter directly")
    void shouldSetSqlArrayDirectly() throws Exception {
      // Act
      ARRAY_TYPE_HANDLER.setParameter(ps, 1, mockSqlArray, null);

      // Assert
      verify(ps).setArray(1, mockSqlArray);
    }

    @Test
    @DisplayName("should convert a Java array to a java.sql.Array parameter")
    void shouldConvertJavaArrayToSqlArray() throws Exception {
      // Arrange
      Connection mockConnection = mock(Connection.class);
      Array mockCreatedArray = mock(Array.class);
      String[] inputJavaArray = { "Hello", "World" };

      when(ps.getConnection()).thenReturn(mockConnection);
      when(mockConnection.createArrayOf(anyString(), any(Object[].class))).thenReturn(mockCreatedArray);

      // Act
      ARRAY_TYPE_HANDLER.setParameter(ps, 1, inputJavaArray, JdbcType.ARRAY);

      // Assert
      verify(ps).setArray(1, mockCreatedArray);
      verify(mockCreatedArray).free();
    }

    @Test
    @DisplayName("should set a null parameter")
    void shouldSetNull() throws Exception {
      // Act
      ARRAY_TYPE_HANDLER.setParameter(ps, 1, null, JdbcType.ARRAY);

      // Assert
      verify(ps).setNull(1, Types.ARRAY);
    }

    @Test
    @DisplayName("should throw TypeException for an unsupported parameter type")
    void shouldThrowExceptionForUnsupportedParameterType() {
      // Arrange
      Object unsupportedParameter = "this is not an array";

      // Act & Assert
      assertThrows(TypeException.class,
          () -> ARRAY_TYPE_HANDLER.setParameter(ps, 1, unsupportedParameter, null));
    }
  }

  @Nested
  @DisplayName("getResult tests")
  class GetResultTests {

    @Nested
    @DisplayName("when database value is not null")
    class WhenResultIsNotNull {

      private final String[] expectedArray = { "a", "b" };

      @BeforeEach
      void setup() throws SQLException {
        // This setup is common for all non-null getResult scenarios.
        when(mockSqlArray.getArray()).thenReturn(expectedArray);
      }

      @Test
      @DisplayName("should return array from ResultSet by column name")
      void fromResultSetByName() throws Exception {
        // Arrange
        when(rs.getArray("column")).thenReturn(mockSqlArray);

        // Act
        Object result = ARRAY_TYPE_HANDLER.getResult(rs, "column");

        // Assert
        assertEquals(expectedArray, result);
        verify(mockSqlArray).free();
      }

      @Test
      @DisplayName("should return array from ResultSet by column index")
      void fromResultSetByIndex() throws Exception {
        // Arrange
        when(rs.getArray(1)).thenReturn(mockSqlArray);

        // Act
        Object result = ARRAY_TYPE_HANDLER.getResult(rs, 1);

        // Assert
        assertEquals(expectedArray, result);
        verify(mockSqlArray).free();
      }

      @Test
      @DisplayName("should return array from CallableStatement by column index")
      void fromCallableStatementByIndex() throws Exception {
        // Arrange
        when(cs.getArray(1)).thenReturn(mockSqlArray);

        // Act
        Object result = ARRAY_TYPE_HANDLER.getResult(cs, 1);

        // Assert
        assertEquals(expectedArray, result);
        verify(mockSqlArray).free();
      }
    }

    @Nested
    @DisplayName("when database value is null")
    class WhenResultIsNull {

      @Test
      @DisplayName("should return null from ResultSet by column name")
      void fromResultSetByName() throws Exception {
        // Arrange
        when(rs.getArray("column")).thenReturn(null);

        // Act & Assert
        assertNull(ARRAY_TYPE_HANDLER.getResult(rs, "column"));
      }

      @Test
      @DisplayName("should return null from ResultSet by column index")
      void fromResultSetByIndex() throws Exception {
        // Arrange
        when(rs.getArray(1)).thenReturn(null);

        // Act & Assert
        assertNull(ARRAY_TYPE_HANDLER.getResult(rs, 1));
      }

      @Test
      @DisplayName("should return null from CallableStatement by column index")
      void fromCallableStatementByIndex() throws Exception {
        // Arrange
        when(cs.getArray(1)).thenReturn(null);

        // Act & Assert
        assertNull(ARRAY_TYPE_HANDLER.getResult(cs, 1));
      }
    }
  }
}