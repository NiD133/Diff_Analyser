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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("EnumOrdinalTypeHandler: Maps Java Enums to their integer ordinal value")
class EnumOrdinalTypeHandlerTest extends BaseTypeHandlerTest {

  // The enum used for testing purposes.
  enum TestEnum {
    ONE, TWO
  }

  private static final TypeHandler<TestEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(TestEnum.class);

  @Nested
  @DisplayName("Setting Parameters")
  class SetParameterTests {

    @ParameterizedTest
    @EnumSource(TestEnum.class)
    @DisplayName("Should set the enum's ordinal for a non-null parameter")
    void shouldSetEnumOrdinalForNonNullParameter(TestEnum parameter) throws Exception {
      // Act
      TYPE_HANDLER.setParameter(ps, 1, parameter, null);

      // Assert: Verify that setInt is called with the enum's ordinal value.
      verify(ps).setInt(1, parameter.ordinal());
    }

    @Test
    @DisplayName("Should set SQL NULL for a null parameter")
    void shouldSetSqlNullForNullParameter() throws Exception {
      // Act
      TYPE_HANDLER.setParameter(ps, 1, null, JdbcType.INTEGER);

      // Assert
      verify(ps).setNull(1, JdbcType.INTEGER.TYPE_CODE);
    }
  }

  @Nested
  @DisplayName("Getting Results from ResultSet")
  class GetResultFromResultSetTests {

    @Nested
    @DisplayName("by column name")
    class ByName {
      @ParameterizedTest
      @EnumSource(TestEnum.class)
      @DisplayName("Should return the correct enum for a given ordinal")
      void shouldReturnCorrectEnum(TestEnum expectedEnum) throws Exception {
        // Arrange
        when(rs.getInt("column")).thenReturn(expectedEnum.ordinal());
        when(rs.wasNull()).thenReturn(false);

        // Act & Assert
        assertEquals(expectedEnum, TYPE_HANDLER.getResult(rs, "column"));
      }

      @Test
      @DisplayName("Should return null when the database value is null")
      void shouldReturnNullWhenDbValueIsNull() throws Exception {
        // Arrange
        when(rs.getInt("column")).thenReturn(0); // This value is ignored when wasNull is true
        when(rs.wasNull()).thenReturn(true);

        // Act & Assert
        assertNull(TYPE_HANDLER.getResult(rs, "column"));
      }
    }

    @Nested
    @DisplayName("by column position")
    class ByPosition {
      @ParameterizedTest
      @EnumSource(TestEnum.class)
      @DisplayName("Should return the correct enum for a given ordinal")
      void shouldReturnCorrectEnum(TestEnum expectedEnum) throws Exception {
        // Arrange
        when(rs.getInt(1)).thenReturn(expectedEnum.ordinal());
        when(rs.wasNull()).thenReturn(false);

        // Act & Assert
        assertEquals(expectedEnum, TYPE_HANDLER.getResult(rs, 1));
      }

      @Test
      @DisplayName("Should return null when the database value is null")
      void shouldReturnNullWhenDbValueIsNull() throws Exception {
        // Arrange
        when(rs.getInt(1)).thenReturn(0); // This value is ignored when wasNull is true
        when(rs.wasNull()).thenReturn(true);

        // Act & Assert
        assertNull(TYPE_HANDLER.getResult(rs, 1));
      }
    }
  }

  @Nested
  @DisplayName("Getting Results from CallableStatement by column position")
  class GetResultFromCallableStatementTests {

    @ParameterizedTest
    @EnumSource(TestEnum.class)
    @DisplayName("Should return the correct enum for a given ordinal")
    void shouldReturnCorrectEnum(TestEnum expectedEnum) throws Exception {
      // Arrange
      when(cs.getInt(1)).thenReturn(expectedEnum.ordinal());
      when(cs.wasNull()).thenReturn(false);

      // Act & Assert
      assertEquals(expectedEnum, TYPE_HANDLER.getResult(cs, 1));
    }

    @Test
    @DisplayName("Should return null when the database value is null")
    void shouldReturnNullWhenDbValueIsNull() throws Exception {
      // Arrange
      when(cs.getInt(1)).thenReturn(0); // This value is ignored when wasNull is true
      when(cs.wasNull()).thenReturn(true);

      // Act & Assert
      assertNull(TYPE_HANDLER.getResult(cs, 1));
    }
  }
}