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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

/**
 * Tests for EnumOrdinalTypeHandler that verify:
 * - Setting a non-null enum parameter writes its ordinal.
 * - Setting a null parameter uses the provided JDBC type.
 * - Reading by column name/position or from CallableStatement maps ordinals back to the enum.
 * - 'wasNull' results in a null return value.
 */
class EnumOrdinalTypeHandlerTest extends BaseTypeHandlerTest {

  enum MyEnum {
    ONE, TWO
  }

  private static final TypeHandler<MyEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(MyEnum.class);
  private static final String COLUMN = "column";
  private static final int ORDINAL_ONE = MyEnum.ONE.ordinal();
  private static final int ORDINAL_TWO = MyEnum.TWO.ordinal();

  @Override
  @Test
  public void shouldSetParameter() throws Exception {
    // Arrange & Act
    TYPE_HANDLER.setParameter(ps, 1, MyEnum.ONE, null);

    // Assert
    verify(ps).setInt(1, ORDINAL_ONE);
  }

  @Test
  void shouldSetNullParameter() throws Exception {
    // Arrange & Act
    TYPE_HANDLER.setParameter(ps, 1, null, JdbcType.VARCHAR);

    // Assert
    verify(ps).setNull(1, JdbcType.VARCHAR.TYPE_CODE);
  }

  @Override
  @Test
  public void shouldGetResultFromResultSetByName() throws Exception {
    // Arrange
    mockResultSetByName(COLUMN, ORDINAL_ONE, false);

    // Act & Assert
    assertSame(MyEnum.ONE, TYPE_HANDLER.getResult(rs, COLUMN));
  }

  @Override
  @Test
  public void shouldGetResultNullFromResultSetByName() throws Exception {
    // Arrange
    mockResultSetByName(COLUMN, ORDINAL_ONE, true); // value ignored because wasNull = true

    // Act & Assert
    assertNull(TYPE_HANDLER.getResult(rs, COLUMN));
  }

  @Override
  @Test
  public void shouldGetResultFromResultSetByPosition() throws Exception {
    // Arrange
    mockResultSetByPosition(1, ORDINAL_ONE, false);

    // Act & Assert
    assertSame(MyEnum.ONE, TYPE_HANDLER.getResult(rs, 1));
  }

  @Override
  @Test
  public void shouldGetResultNullFromResultSetByPosition() throws Exception {
    // Arrange
    mockResultSetByPosition(1, ORDINAL_ONE, true); // value ignored because wasNull = true

    // Act & Assert
    assertNull(TYPE_HANDLER.getResult(rs, 1));
  }

  @Override
  @Test
  public void shouldGetResultFromCallableStatement() throws Exception {
    // Arrange
    mockCallableStatement(1, ORDINAL_ONE, false);

    // Act & Assert
    assertSame(MyEnum.ONE, TYPE_HANDLER.getResult(cs, 1));
  }

  @Override
  @Test
  public void shouldGetResultNullFromCallableStatement() throws Exception {
    // Arrange
    mockCallableStatement(1, ORDINAL_ONE, true); // value ignored because wasNull = true

    // Act & Assert
    assertNull(TYPE_HANDLER.getResult(cs, 1));
  }

  // Additional sanity check to make the ordinal-to-enum mapping explicit.
  @Test
  void shouldMapSecondOrdinalToSecondEnumConstant() throws Exception {
    // Arrange
    mockResultSetByName(COLUMN, ORDINAL_TWO, false);

    // Act & Assert
    assertSame(MyEnum.TWO, TYPE_HANDLER.getResult(rs, COLUMN));
  }

  // -- Helpers to keep the tests focused and avoid repetition --

  private void mockResultSetByName(String column, int value, boolean wasNull) throws Exception {
    when(rs.getInt(column)).thenReturn(value);
    when(rs.wasNull()).thenReturn(wasNull);
  }

  private void mockResultSetByPosition(int index, int value, boolean wasNull) throws Exception {
    when(rs.getInt(index)).thenReturn(value);
    when(rs.wasNull()).thenReturn(wasNull);
  }

  private void mockCallableStatement(int index, int value, boolean wasNull) throws Exception {
    when(cs.getInt(index)).thenReturn(value);
    when(cs.wasNull()).thenReturn(wasNull);
  }
}